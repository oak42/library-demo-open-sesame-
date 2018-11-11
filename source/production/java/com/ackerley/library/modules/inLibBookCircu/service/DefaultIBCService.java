package com.ackerley.library.modules.inLibBookCircu.service;

import com.ackerley.library.modules.inLibBookCircu.entity.*;
import com.ackerley.library.modules.inLibBookCircu.repository.BiblioMapper;
import com.ackerley.library.modules.inLibBookCircu.repository.InLibBookMapper;
import com.ackerley.library.modules.priorBookCircu.entity.PBCProcInstc;
import com.ackerley.library.modules.priorBookCircu.service.PBCService;
import com.ackerley.library.modules.sys.entity.Bookshelf;
import com.ackerley.library.modules.sys.service.LibCrdService;
import com.ackerley.library.modules.sys.service.SysRuleService;
import com.ackerley.library.modules.sys.service.UserService;
import com.ackerley.library.modules.sys.utils.UserUtil;
import com.github.pagehelper.PageHelper;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class DefaultIBCService implements IBCService, ServletContextAware{
    private ServletContext servletContext;

    @Autowired
    private PBCService pbcs;
    @Autowired
    private BiblioService bs;
    @Autowired
    private InLibBookService inLibBookService;

    @Autowired
    private SysRuleService sysRuleService;
    @Autowired
    private UserService userService;
    @Autowired
    private LibCrdService libCrdService;
    @Autowired
    private OverdueFineService overdueFineService;
    @Autowired
    private BorrowReturnRecordService brrService;

    @Autowired
    private BiblioMapper biblioMapper;
    @Autowired
    private InLibBookMapper inLibBookMapper;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

//编目■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
    //
    @Transactional
    public int doCataloging(PBCProcInstc procInstc, Biblio biblio, String bookshelfID, Integer quantity, Integer start) {
        String userID = UserUtil.getCurrentUser().getID();
        Timestamp now = new Timestamp(System.currentTimeMillis());
        //入库前流程 到图书编目 还有一点收尾逻辑...
        pbcs.cataloging(procInstc);
        //                          其中，下载网络图片到本地的思路是网上抄的...【扣】未吃透、未熟练...
        String bookCoverUrlStr = biblio.getHrefCoverImg();
        FileOutputStream fos = null;
        try {
            URL bookCoverUrl = new URL(bookCoverUrlStr);
            HttpURLConnection conn = (HttpURLConnection)bookCoverUrl.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);   //5s
            InputStream is = conn.getInputStream();
            byte[] imgData = readInputStream(is);

            String localPath = "/book_covers/" + biblio.getISBN13() + ".jpg";
            String staticResourcePath = servletContext.getRealPath("/static");
            //创建存放book cover的server本地文件夹(【扣】其实这句逻辑适合在app startup时call一次即可...当然这里正好看看createDirectories会不会override已有，看来是不会...)
            Files.createDirectories(FileSystems.getDefault().getPath(staticResourcePath + "/book_covers"));
            //
            File imgFile = new File(staticResourcePath + localPath);
            fos = new FileOutputStream(imgFile);
            fos.write(imgData);
            biblio.setHrefCoverImg(localPath);
        } catch (IOException e) {
            e.printStackTrace();    //........................
        } finally {
            IOUtils.closeQuietly(fos);
        }
        bs.saveOne(biblio); //之后 biblio ID 就有了，↓ 下一步中存入...
        //
        InLibBook book = new InLibBook();
        book.setBiblio(biblio);
        Bookshelf bookshelf = new Bookshelf();
        bookshelf.setID(bookshelfID);
        book.setBookshelf(bookshelf);
        book.setState(InLibBook.ILBS_SHELVING); //刚编目好的书，状态 待上架(刚还的书 状态也是 待上架)...
        for (int i = start; i <= start + quantity - 1; i++) {
            StringBuilder sb = new StringBuilder().append(String.valueOf(i));
            int offset = 3 - sb.length();  //bar code 建模为 13位ISBN13号 接 3位序号(不足位填"0")
            for (int j = 0; j < offset; j++) {
                sb.insert(0, "0");  //前插...
            }
//            book.setBarCode(sb.insert(0, procInstc.getISBN13()).toString()); //效果也一样，但看起来略不简洁，性能上优劣不知...
            book.setBarCode(biblio.getISBN13() + sb);
            inLibBookService.saveOne(book);
            book.setID(null);   //★★★★★★
        }
        return 0;
    }

    private static byte[] readInputStream(InputStream is) throws IOException{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = 0;
        while ((length = is.read(buffer)) != -1) {
            baos.write(buffer, 0, length);
        }
        is.close();
        return baos.toByteArray();
    }


//(circulation desk)借还登记■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
    /*libCrdsTempDataMap，暂存(学了缓存以后应当用缓存实现？)借书登记时的暂时数据...
    * map key --- checkout事务客户，即借书证bar code；
    * map value --- checkout事务需暂存的state values(状态值，Map形式)，其keys：
    *               overdueFineUnpaid -- boolean，是否有未缴逾期罚金...【主要用于(先缴纳罚金后借书逻辑的)后端逻辑判断控制？减少DB访问次数，相当于cache作用了orz...】
    *               booksToCheckOut -- 本次借书事务的 待checkout的 书本bar code数组
    *               booksToCheckOutCount -- 本次借书事务的 待checkout的 书本bar code数组size
    */
    private Map<String, Map<String, Object>> libCrdsTempDataMap = new HashMap<>();
    //
    private Object getTempLibCrdData(String libCrdBarCode, String dataKey) {
        return libCrdsTempDataMap.get(libCrdBarCode).get(dataKey);
    }
    //
    private void putTempLibCrdData(String libCrdBarCode, String dataKey, Object dataValue) {
        libCrdsTempDataMap.get(libCrdBarCode).put(dataKey, dataValue);
    }

    //增添 待借出书本 前 的各种事前检查    //check中若相应的子check不通过，则feedback会被populate...
    private boolean bookAddingViolationCheck(String libCrdBarCode, String bookBarCode, Map<String, Object> feedback) {
        List<BorrowReturnRecord> outstandingRecordList = retrieveOutstandingRecordList(libCrdService.retrieveLibCrdWithLibCrdBarCode(libCrdBarCode).getID());
        Map<String, String> booksToCheckOut = (Map<String, String>)getTempLibCrdData(libCrdBarCode, "booksToCheckOut");

        return bookBarCodeFalsityAndBookStateErrorCheck(bookBarCode, feedback) ||
                anyUnpaidFines(libCrdBarCode) ||
                sameBiblioDuplicationCheck(outstandingRecordList, booksToCheckOut, bookBarCode, feedback) ||
                borrowNumberOverLimitCheck(outstandingRecordList, booksToCheckOut, feedback);
    }
    //图书条形码真实有效性检查 + 图书状态合理性(在库在架)检查
    private boolean bookBarCodeFalsityAndBookStateErrorCheck(String bookBarCode, Map<String, Object> feedback) {

        try {
            InLibBook book = inLibBookService.retrieveInLibBookWithInLibBookBarCode(bookBarCode);
            if (!book.getState().equals(InLibBook.ILBS_IN_STK)) {
                feedback.put("success", false);             //〓〓〓〓〓〓〓〓〓〓〓〓〓〓
                feedback.put("failureInfo", "错误：图书当前状态为非可借...可能①待上架而未上架；②正处于借出状态未登记归还；③已登记遗失...");
                return true;
            }
        } catch (RuntimeException e) {
            feedback.put("success", false);                 //〓〓〓〓〓〓〓〓〓〓〓〓〓〓
            feedback.put("failureInfo", e.getMessage());
            e.printStackTrace();
            return true;
        }

        return false;
    }
    //后端检查是否还有未缴纳的 或有罚金
    private boolean anyUnpaidFines(String libCrdBarCode){
        boolean anyFinesUnpaid = (boolean)getTempLibCrdData(libCrdBarCode, "overdueFineUnpaid");
        if(anyFinesUnpaid) {
            return true;
        }
        return false;
    }
    //检查当前扫描进来的书本(bookBarCode)是否已经存在同书目(biblio)的①在借(outstanding)或②待借(temp data)书本【同一书目的书只允许借一本
    // (当然 其中也隐含覆盖了对同一本书多次扫码的情况)】
    //依赖 13位ISBN+3位序号 此种bar code建模方式...     当然标准的做法是比较书本的 书目ID(biblio ID)...
    private boolean sameBiblioDuplicationCheck(List<BorrowReturnRecord> outstandingRecordList, Map<String, String> booksToCheckOut,
                                              String bookBarCode, Map<String, Object> feedback) {
        //①
        for (BorrowReturnRecord record : outstandingRecordList) {
            if(inLibBookService.retrieveOne(record.getBookID()).getBarCode().substring(0,12).equals(bookBarCode.substring(0,12))) {
                feedback.put("success", false);              //〓〓〓〓〓〓〓〓〓〓〓〓〓〓
                feedback.put("failureInfo", "错误：您已借出过本书目，且尚未归还...");
                return true;
            }
        }
        //②
        for (String barCode : booksToCheckOut.keySet()) {
            if (barCode.substring(0,12).equals(bookBarCode.substring(0,12))) {
                feedback.put("success", false);              //〓〓〓〓〓〓〓〓〓〓〓〓〓〓
                feedback.put("failureInfo", "错误：同书目图书只许借阅一本(/套)...");
                return true;
            }
        }
        //
        return false;
    }
    //检查是否超借阅本数上限
    private boolean borrowNumberOverLimitCheck(List<BorrowReturnRecord> outstandingRecordList, Map<String, String> booksToCheckOut,
                                               Map<String, Object> feedback) {
        if(booksToCheckOut.size() + outstandingRecordList.size() >= Integer.parseInt(sysRuleService.retrieveOne("4").getParmValue())) {
            feedback.put("success", false);              //〓〓〓〓〓〓〓〓〓〓〓〓〓〓
            feedback.put("failureInfo", "错误：超出可借本数上限...");
            return true;
        }
        return false;
    }

    //获取借书卡下 所有未缴逾期罚金项...
    public List<OverdueFine> retrieveUnpaidOverdueFineList(String libCrdID) {
        OverdueFine filterUnpaidOverdueFine = new OverdueFine();
        filterUnpaidOverdueFine.setState("unpaid");
        filterUnpaidOverdueFine.setLibCrdID(libCrdID);
        return overdueFineService.retrieveList(filterUnpaidOverdueFine);
    }

    //获取在借(未还)记录列表
    public List<BorrowReturnRecord> retrieveOutstandingRecordList(String libCrdID) {
        BorrowReturnRecord filterOutstandingRecord = new BorrowReturnRecord();
        filterOutstandingRecord.setLibCrdID(libCrdID);
        List<BorrowReturnRecord> outstandingRecordList;     //这种写法，逻辑体现在service层，而非dao层，即service层复杂点，dao层简单点...性能上可能差些，两次访问DB...

        filterOutstandingRecord.setStage(BorrowReturnRecord.BRRS_OUT_NOT_OVERDUE);
        outstandingRecordList = brrService.retrieveList(filterOutstandingRecord);

        filterOutstandingRecord.setStage(BorrowReturnRecord.BRRS_OVERDUE_NOT_BACK);
        outstandingRecordList.addAll(brrService.retrieveList(filterOutstandingRecord));

        return outstandingRecordList;
    }

    //应对 刷了借书证后，未走完借书流程，而放弃当前借书证借阅办理，直接又刷另一借书证的情况...
    public void updateLibCrdsTempData(String barCodePrev, String barCode, List<OverdueFine> unpaidOverdueFineList) {
        Map<String, Object> tempDataEntry = new HashMap<>();
        Map<String, String> booksToCheckOut = new HashMap<>();  //【待】缘何不用 List<String> 而用 Map<String, String>，List的remove方法真的保险吗？
        tempDataEntry.put("booksToCheckOut", booksToCheckOut);
        tempDataEntry.put("booksToCheckOutCount", new Integer(0));
        if(unpaidOverdueFineList.size() == 0) {
            tempDataEntry.put("overdueFineUnpaid", false);
        } else {
            tempDataEntry.put("overdueFineUnpaid", true);
        }
        //垃圾清理，清理 先前 未办结checkout业务的lib crd 的 temp data...
        // 【这里有个坑，应当先清理旧lib card temp data，再put新lib card temp data，
        //   否则若新、旧为同一lib card(同lib card刷两次的操作情景)，则由于同key name，新put的temp data会被remove，表现就是后面想操作此temp data时NullPointerException】
        libCrdsTempDataMap.remove(barCodePrev);
        libCrdsTempDataMap.put(barCode, tempDataEntry);
    }

    //
    @Transactional
    public Map<String, Object> payFines(String libCrdID, float amountPaid) {
        Map<String, Object> feedback = new HashMap<>();

        List<OverdueFine> unpaidOverdueFineList = retrieveUnpaidOverdueFineList(libCrdID);
        float totalFineAmount = 0;
        for (OverdueFine fine : unpaidOverdueFineList) {
            totalFineAmount += fine.getAmount();
            BorrowReturnRecord brRecord = brrService.retrieveOne(fine.getBorrowAndReturnRecordID());
            brRecord.setStage(BorrowReturnRecord.BRRS_OVERDUE_BACK_FINE_PAID);
            brrService.saveOne(brRecord);
            fine.setState("paid");
            overdueFineService.saveOne(fine);
        }

        if (totalFineAmount == amountPaid) {
            feedback.put("success", true);                  //〓〓〓〓〓〓〓〓〓〓〓〓〓〓【扣】【猜】true会被装箱为Boolean？
            putTempLibCrdData(libCrdService.retrieveOne(libCrdID).getBarCode(), "overdueFineUnpaid", false);
        } else {
            throw new RuntimeException("系统逻辑错误，逾期罚金应缴额应当与实缴额一致才对...");  //正常情况不应该到这里，到这里说明有逻辑bug...
        }
        return feedback;
    }

    //
    public Map<String, Object> addOneBook(String libCrdBarCode, String bookBarCode) {
        Map<String, Object> feedback = new HashMap<>();
        if(!bookAddingViolationCheck(libCrdBarCode, bookBarCode, feedback)) {   //check中若相应的子check不通过，则feedback会被populate...
            Map<String, String> booksToCheckOut = (Map<String, String>)getTempLibCrdData(libCrdBarCode, "booksToCheckOut");
            booksToCheckOut.put(bookBarCode, bookBarCode);      //【待】缘何不用 List<String> 而用 Map<String, String>，因为不知道List的remove方法真的保险吗？==不同于string的equals啊...
            Integer booksToCheckOutCount = (Integer) getTempLibCrdData(libCrdBarCode, "booksToCheckOutCount");
            putTempLibCrdData(libCrdBarCode, "booksToCheckOutCount", ++booksToCheckOutCount);
            feedback.put("success", true);
            feedback.put("booksToCheckOutCount", booksToCheckOutCount);
            feedback.put("bookInfo", inLibBookService.retrieveInLibBookWithInLibBookBarCode(bookBarCode));
        }
        return feedback;
    }

    //
    public Map<String, Object> abolishOneBook(String libCrdBarCode, String bookBarCode) {
        Map<String, Object> feedback = new HashMap<>();
        Map<String, String> booksToCheckOut = (Map<String, String>)getTempLibCrdData(libCrdBarCode, "booksToCheckOut");
        booksToCheckOut.remove(bookBarCode);
        Integer booksToCheckOutCount = (Integer) getTempLibCrdData(libCrdBarCode, "booksToCheckOutCount");
        putTempLibCrdData(libCrdBarCode, "booksToCheckOutCount", --booksToCheckOutCount);
        feedback.put("success", true);
        feedback.put("booksToCheckOutCount", booksToCheckOutCount);

        return feedback;
    }

    //借书登记确认  提交DB了...
    @Transactional
    public boolean confirmCheckingOut(String libCrdBarCode) {
        Map<String, String> booksToCheckOut = (Map<String, String>)getTempLibCrdData(libCrdBarCode, "booksToCheckOut");

        Timestamp now = new Timestamp(System.currentTimeMillis());

        BorrowReturnRecord record = new BorrowReturnRecord();
        record.setBorrowTime(now);
        record.setLibCrdID(libCrdService.retrieveLibCrdWithLibCrdBarCode(libCrdBarCode).getID());
        record.setLendingTransactorID(UserUtil.getCurrentUser().getID());
        record.setStage(BorrowReturnRecord.BRRS_OUT_NOT_OVERDUE);

        InLibBook auxiliaryBookObject = new InLibBook();    //用于更新在库图书state的辅助对象
        auxiliaryBookObject.setState(InLibBook.ILBS_OUT);

        for (String bookBarCode : booksToCheckOut.keySet()) {
            String bookID = inLibBookService.retrieveInLibBookWithInLibBookBarCode(bookBarCode).getID();
            //更新图书状态为借出
            auxiliaryBookObject.setID(bookID);
            inLibBookMapper.updateNonBlankByID(auxiliaryBookObject);
            //新启借还记录
            record.setBookID(bookID);
            record.setID(null);     //★★★★★★★此处就是要新建的效果，而非update...
            brrService.saveOne(record);
        }

        return true;
    }

    /*
    还书一本 登记；
    若逾期，需要另外写入 逾期罚金记录表；
    书本state改为 待上架；
    返回本次借还事务的简要信息：显示 借还日期，若逾期，+ 显示 逾期天数...
{{{{{{{{关于时区的问题，试了好久，小结一下：
        System.currentTimeMillis()，看文档，获取的是格林尼治时间毫秒，存DB时，(从取出的timestamp field来看)也是此时间毫秒，并未依照time zone转换，
        (估计DB的time zone是用来presentation时转换用的，
                比如console查询一个timestamp时自动转译到所设time zone，
                   IDE的可视化DB工具、mysql workbench中看到的timestamp也是根据time zone转译过的【坑，当时还以为所见即所得...】
        【与calendar类的产生初衷一个意思？时间数据的 存储(值) 与 表达 是分离的，
                                      试下：同一个当下产生的当前timestamp(一个value值)，设置不同的time zone格式化输出时，是对应不同的时间的！现在知道8小时的时差怎么来的了吧】)
        timestamp在输出的时候，依照所设时区的不同，输出的时间是不同的，但若getTime()以后，会发现，毫秒数都是那个毫秒数不变。存储(值) 与 表达 关注点分离}}}}}}}}
    */
    @Transactional
    public String bookReturnReg(String bookID) {
        //逻辑运转正常的情况下，一本书的未还转态BorrowReturnRecord仅有至多一个。虽然mybatis有<foreach>动态SQL标签，但此处还是试着选择将复杂度留在service层看看...
        StringBuilder feedback = new StringBuilder().append("成功归还图书");

        Timestamp now = new Timestamp(System.currentTimeMillis());

        BorrowReturnRecord filter = new BorrowReturnRecord();

        List<BorrowReturnRecord> aCatchAllList = new ArrayList<>();

        filter.setBookID(bookID);
        filter.setStage(BorrowReturnRecord.BRRS_OUT_NOT_OVERDUE);
        aCatchAllList.addAll(brrService.retrieveList(filter));
        filter.setStage(BorrowReturnRecord.BRRS_OVERDUE_NOT_BACK);
        aCatchAllList.addAll(brrService.retrieveList(filter));
        BorrowReturnRecord record = aCatchAllList.get(0);   //此处若是null list将直接跳异常，但逻辑上不应如此...

        record.setReturnTime(now);
        record.setReturningTransactorID(UserUtil.getCurrentUser().getID());
//        if(record.getIsOverdue()) { //todo （学了定时计划任务后，用之来完成isOverdue的赋值，并且后面的 stage值更新 重新实现...）
//            ...
//        }
        int overdueTimeLimit = Integer.parseInt(sysRuleService.retrieveOne("1").getParmValue());    //借阅时限(未加续借)
        int renewTimeLimit = Integer.parseInt(sysRuleService.retrieveOne("2").getParmValue());      //续借时限
        Date borrowTime = record.getBorrowTime();
//        int actualDuration = (int)( (now.getTime() - borrowTime.getTime()) / (1000*60*60*24) ); //实际借阅天数【弃】...
        int actualDuration = (int)((now.getTime() / (1000*60*60) + 8) / 24) - (int)((borrowTime.getTime() / (1000*60*60) + 8) / 24); //实际借阅天数【扣】体会下与上面这种计算天数的区别，天数认定的模糊化处理，timestamp → date，天数后的位数不认...8小时时差是猜的...
//debug//////////////////////////////////////////////////////////////////
//        double observerN = now.getTime() / (1000*60*60*24.0);
//        int intN = (int)(now.getTime() / (1000*60*60*24));
//
//        double observerBT = borrowTime.getTime() / (1000*60*60*24.0);
//        int intBT = (int)(borrowTime.getTime() / (1000*60*60*24));
//debug//////////////////////////////////////////////////////////////////
        int overdue = record.getIsRenewed() ? actualDuration - overdueTimeLimit - renewTimeLimit : actualDuration - overdueTimeLimit;

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        feedback.append("，借阅时间：").append(df.format(borrowTime))
                .append("，归还时间：").append(df.format(now))
                .append("，借阅天数：").append(actualDuration);
        if(record.getIsRenewed()) {feedback.append("，已办理续借");}

        if(overdue <= 0) {
            record.setStage(BorrowReturnRecord.BRRS_BACK_NOT_OVERDUE);
            feedback.append("，未逾期");
        } else {
            record.setStage(BorrowReturnRecord.BRRS_OVERDUE_BACK_FINE_UNPAID);
            OverdueFine fine = new OverdueFine();
            fine.setBorrowAndReturnRecordID(record.getID());
            float fineRate = Float.parseFloat(sysRuleService.retrieveOne("3").getParmValue());
            float fineAmount = fineRate * overdue;
            fine.setAmount(fineAmount);
            fine.setFormationTime(now);
            fine.setLibCrdID(record.getLibCrdID());
            fine.setState("unpaid");
            overdueFineService.saveOne(fine);
            feedback.append("，逾期天数：").append(overdue).append("，产生罚金：").append(fineAmount).append("元");
        }
        brrService.saveOne(record);
        //
        InLibBook book = new InLibBook();
        book.setID(bookID);
        book.setState(InLibBook.ILBS_SHELVING);
        inLibBookMapper.updateNonBlankByID(book);

        //释放libCrdsTempDataMap中lib card名下的temp data？不释放应该也不会造成逻辑问题，因为每次刷lib card都会new 新的override掉...

        return feedback.toString();
    }

//图书上架(摆架：①新书入库编目完成后摆架、②还回来的图书摆架)■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
    //获取待摆架图书(InLibBook.ILBS_SHELVING)
    @Transactional
    public List<InLibBook> retrieveToBeShelvedBooks() {
        InLibBook filterBookTobeShelved = new InLibBook();
        filterBookTobeShelved.setState(InLibBook.ILBS_SHELVING);
        return inLibBookService.retrieveList(filterBookTobeShelved);
    }

    //登记已摆架图书(InLibBook.ILBS_SHELVING → InLibBook.ILBS_IN_STK)
    @Transactional
    public Integer doShelving(BulkBookShelvingAid bbsa) {
        int count = 0;
        InLibBook book = new InLibBook();
        book.setState(InLibBook.ILBS_IN_STK);
        for (BookShelvingResultPair pair : bbsa.getList()) {
            if(pair.getResult().equals("on")) {
                book.setID(pair.getSubject().getID());
                inLibBookMapper.updateNonBlankByID(book);
                count++;
            }
        }
        return count;
    }


//馆藏查询■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
    public List<Biblio> biblioQuery(Biblio filterBiblio, int page, int rows) {
        PageHelper.startPage(page, rows);
        return biblioMapper.retrieveListFuzzyQuery(filterBiblio);
    }


}