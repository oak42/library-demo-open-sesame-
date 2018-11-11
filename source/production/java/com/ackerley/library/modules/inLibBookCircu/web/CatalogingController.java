package com.ackerley.library.modules.inLibBookCircu.web;

import com.ackerley.library.common.web.BaseController;
import com.ackerley.library.modules.inLibBookCircu.entity.Biblio;
import com.ackerley.library.modules.inLibBookCircu.service.IBCService;
import com.ackerley.library.modules.priorBookCircu.entity.PBCProcInstc;
import com.ackerley.library.modules.priorBookCircu.service.PBCService;
import com.ackerley.library.modules.priorBookCircu.service.ProcInstcService;
import com.ackerley.library.modules.sys.entity.Bookshelf;
import com.ackerley.library.modules.sys.service.BookshelfService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

/*
编目，应当是不分pur order的，从proc instc按stage为cataloging挨个一一获取(可以考虑order by heat)，一个instc编好后则next...
编目数量inboundQty从pur order item表获取，join下pur order表，where pur order表的state为'inLib'...
①依照inboundQty顺次安排bar code(采用ISBN13+3位序号 共16位) → InLibBook
②关联分类classification → Biblio
③关联上架位置 → InLibBook
*/

@Controller
@RequestMapping(path = "/inLibCircu/ctlg")
public class CatalogingController extends BaseController{
    @Autowired
    private PBCService pbcs;
    @Autowired
    private IBCService ibcs;
    @Autowired
    private BookshelfService bs;


    @RequestMapping(path = {"", "index"})
    public String catalogingIndex(@RequestParam(name = "page", required = false, defaultValue = "1") int page,
                                  @RequestParam(name = "rows", required = false, defaultValue = "5") int rows,
                                  Model model) {
        List<PBCProcInstc> list= pbcs.retrieveToBeCatalogedList(page, rows);
        model.addAttribute("pageInfo", new PageInfo<PBCProcInstc>(list));
        return "modules/inLibBookCircu/catalogingIndex";
    }

    @RequestMapping(path = "doCataloging")
    public String catalogingForm(PBCProcInstc procInstc, Biblio biblio,                 //procInstc中须有ID、ISBN13...
                                 @RequestParam(name = "bookshelfID", required = false) String bookshelfID,
                                 @RequestParam(name = "quantity", required = false) Integer quantity,
                                 @RequestParam(name = "startingBarCode", required = false) Integer start,    //条形码采用 ISBN13 + 3位序号，start即为3位序号的starting number...
                                 RedirectAttributes ras, Model model, @RequestParam Map<String, String> map) {
        String stage = procInstc.getStage();
        if(!StringUtils.isEmpty(stage) && stage.equals(PBCProcInstc.PCS_IN_LIB)) { //catalogingForm中hidden的"stage"值PBCProcInstc.PCS_IN_LIB
            //【待】★★★GET、POST入参绑定时行为差别？★★★【扣】不知道为何，编目结果提交时POST进入时，从map看，入参是好的，但绑定到涉及同name filed时(ID、ISBN13在PBCProcInstc和Biblio中都有)，就会出现双倍现象，中间逗号分隔...先如下三行临时处理，以后再研究...
            // (然而 选择编目条目时GET进入本catalogingForm方法时，绑定却没问题，无双倍现象...)(原来使用GET编目结果提交时 没这种情况，但出现了数据量超URL长度的情况，遂改成POST)
            procInstc.setID(procInstc.getID().split(",")[0]);
            procInstc.setISBN13(null);
            biblio.setISBN13(biblio.getISBN13().split(",")[0]);
            //end【待】★★★GET、POST入参绑定时行为差别？★★★

            biblio.setID(null); //...PBCProcInstc与Biblio都有ID field...
            ibcs.doCataloging(procInstc, biblio, bookshelfID, quantity, start);    //IBCService.doCataloging中封有PBCService.cataloging，以保证transactional...(IBCService中也因而autowire进IBCService)
            addMessage(ras, "");
            return "redirect:/inLibCircu/ctlg";
        }

        model.addAttribute("procInstc", procInstc);

        int inboundQty = pbcs.getInboundNbr(procInstc.getISBN13());
        model.addAttribute("inboundQty", inboundQty);

        List<Bookshelf> bookshelfList = bs.retrieveList(new Bookshelf());
        model.addAttribute("bookshelfList", bookshelfList);
        return "modules/inLibBookCircu/catalogingForm";
    }



}
