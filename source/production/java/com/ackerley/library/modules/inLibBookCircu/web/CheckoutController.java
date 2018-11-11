package com.ackerley.library.modules.inLibBookCircu.web;

import com.ackerley.library.common.web.BaseController;
import com.ackerley.library.modules.inLibBookCircu.entity.BorrowReturnRecord;
import com.ackerley.library.modules.inLibBookCircu.entity.InLibBook;
import com.ackerley.library.modules.inLibBookCircu.entity.OverdueFine;
import com.ackerley.library.modules.inLibBookCircu.service.*;
import com.ackerley.library.modules.sys.entity.LibCrd;
import com.ackerley.library.modules.sys.entity.User;
import com.ackerley.library.modules.sys.service.LibCrdService;
import com.ackerley.library.modules.sys.service.SysRuleService;
import com.ackerley.library.modules.sys.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(path = "/inLibCircu/checkoutReg")
public class CheckoutController extends BaseController {
    @Autowired
    UserService userService;
    @Autowired
    LibCrdService libCrdService;
    @Autowired
    BiblioService biblioService;
    @Autowired
    InLibBookService inLibBookService;
    @Autowired
    OverdueFineService overdueFineService;
    @Autowired
    BorrowReturnRecordService brrService;
    @Autowired
    IBCService ibcService;

    @Autowired
    SysRuleService sysRuleService;

    @RequestMapping(path = {"switchLibCrd", ""})
    public String switchLibCrd(@RequestParam(name = "barCodePrev", required = false) String barCodePrev,
                               @RequestParam(name = "libCrdBarCode", required = false) String barCode,
                               Model model, @RequestParam Map<String, String> map) {
        if(!StringUtils.isEmpty(barCode)) { //barCodePrev若为empty没关系...
            try {
                LibCrd libCrd;                                  //借书卡
                User borrower;                                  //借书卡owner
                List<OverdueFine> unpaidOverdueFineList;        //未缴罚金项
                List<BorrowReturnRecord> outstandingRecordList; //未还图书借阅记录
                libCrd = libCrdService.retrieveLibCrdWithLibCrdBarCode(barCode);    //若lib card bar code不对，会抛runtime exception...
                borrower = userService.retrieveOne(libCrd.getOwnerID());
                unpaidOverdueFineList = ibcService.retrieveUnpaidOverdueFineList(libCrd.getID());
                outstandingRecordList = ibcService.retrieveOutstandingRecordList(libCrd.getID());
                String overdueTimeLimit = sysRuleService.retrieveOne("1").getParmValue();
                String renewTimeLimit = sysRuleService.retrieveOne("2").getParmValue();
                //这样合适吗？
                ibcService.updateLibCrdsTempData(barCodePrev, barCode, unpaidOverdueFineList);

                model.addAttribute("libCrd", libCrd);
                model.addAttribute("borrower", borrower);
                model.addAttribute("unpaidOverdueFineList", unpaidOverdueFineList);
                model.addAttribute("outstandingRecordList", outstandingRecordList);
                model.addAttribute("overdueTimeLimit", overdueTimeLimit);
                model.addAttribute("renewTimeLimit", renewTimeLimit);

                float finesTotalAmount = 0;
                for (OverdueFine fine : unpaidOverdueFineList) {
                    finesTotalAmount += fine.getAmount();
                }
                model.addAttribute("finesTotalAmount", finesTotalAmount);
            } catch (RuntimeException e) {
                addMessage(model, e.getMessage());
                e.printStackTrace();
            }
        }

        return "modules/inLibBookCircu/checkout";
    }

    @RequestMapping(path = "payFines/{libCrdBarCode}/{amountPaid}/")    //【猜】【扣】入参绑定问题，试出来的，对于float的path variable，其实就是带小数点的value string，若不加最后的“/”，总是会丢失小数部分？
    @ResponseBody
    public Map<String, Object> payFines(@PathVariable(name = "libCrdBarCode") String libCrdBarCode,
                                        @PathVariable(name = "amountPaid") float amountPaid) {      //试过Float，也一样，只不过object型的wrapper class object可以接受null，而此处明显是required...
        String libCrdID = libCrdService.retrieveLibCrdWithLibCrdBarCode(libCrdBarCode).getID();
        return ibcService.payFines(libCrdID, amountPaid);
    }

    @RequestMapping(path = "addOneBook/{libCrdBarCode}/{bookBarCode}")
    @ResponseBody
    public Map<String, Object> addOneBook(@PathVariable(name = "libCrdBarCode")  String libCrdBarCode,
                                          @PathVariable(name = "bookBarCode")  String bookBarCode,
                                          @RequestParam Map<String, String> map) {
        return ibcService.addOneBook(libCrdBarCode, bookBarCode);
    }

    @RequestMapping(path = "abolishOneBook/{libCrdBarCode}/{bookBarCode}")
    @ResponseBody
    public Map<String, Object> abolishOneBook(@PathVariable(name = "libCrdBarCode")  String libCrdBarCode,
                                              @PathVariable(name = "bookBarCode")  String bookBarCode,
                                              @RequestParam Map<String, String> map) {
        return ibcService.abolishOneBook(libCrdBarCode, bookBarCode);
    }

    @RequestMapping(path = "confirmCheckingOut/{libCrdBarCode}")
    public String confirmCheckingOut(@PathVariable(name = "libCrdBarCode")  String libCrdBarCode, RedirectAttributes ras,
                                     @RequestParam Map<String, String> map) {
        if(ibcService.confirmCheckingOut(libCrdBarCode)) {
            addMessage(ras, "借阅登记办理成功");
        } else {
            addMessage(ras, "借阅登记办理失败");  //目前的coding并不会进到这里...
        }
        return "redirect:/inLibCircu/checkoutReg";
    }





}