package com.ackerley.library.modules.inLibBookCircu.web;

import com.ackerley.library.common.web.BaseController;
import com.ackerley.library.modules.inLibBookCircu.entity.Biblio;
import com.ackerley.library.modules.inLibBookCircu.entity.InLibBook;
import com.ackerley.library.modules.inLibBookCircu.service.BiblioService;
import com.ackerley.library.modules.inLibBookCircu.service.IBCService;
import com.ackerley.library.modules.inLibBookCircu.service.InLibBookService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 馆藏图书查询
 */
@Controller
@RequestMapping("/inLibCircu/biblioQuery")
public class BiblioQueryController extends BaseController {
    @Autowired
    IBCService ibcService;
    @Autowired
    InLibBookService inLibBookService;

    @RequestMapping({""})
    public String retrieveBiblioList(Biblio filterBiblio, Model model, @RequestParam(value = "doFilter", required = false) String doFilter,
                                     @RequestParam(name = "page", required = false, defaultValue = "1") int page,
                                     @RequestParam(name = "rows", required = false, defaultValue = "5") int rows,
                                     @RequestParam Map<String, String> map) {
        model.addAttribute("filterBiblio", filterBiblio);

        if(!StringUtils.isEmpty(doFilter) && doFilter.equals("doFilter")) { //【鸣】submit型的button可传name-value pair...

/*还是写在mapper file清爽，且为了应用分页插件pagehelper，必须写在mapper file...
            //①title、subtitle在前台是混淆处理的，后台这里也需要混淆处理一下，即逻辑OR；
            //②biblio classification是层次结构式的，(也相当于一种混淆处理吧)，对于任意选定的节点，后台需要注意囊括进其子节点，即使用ancestorIDs...
            // 排列组合，2*2=4种变体【这是把复杂度都放在了controller，本结构若多次使用应该把复杂度放在mybatis mapper file吧...】
            List<Biblio> list = biblioService.retrieveList(filterBiblio); //变体1...    title 即 title，cls.ID 即 cls.ID

            filterBiblio.setSubtitle(filterBiblio.getTitle());
            filterBiblio.setTitle(null);
            list.addAll(biblioService.retrieveList(filterBiblio));        //变体2...    title 作 subtitle，cls.ID 即 cls.ID

            filterBiblio.getCls().setAncestorIDs(filterBiblio.getCls().getID());
            filterBiblio.getCls().setID(null);
            list.addAll(biblioService.retrieveList(filterBiblio));        //变体3...    title 作 subtitle，cls.ID 作 ancestorIDs

            filterBiblio.setTitle(filterBiblio.getSubtitle());
            filterBiblio.setSubtitle(null);
            list.addAll(biblioService.retrieveList(filterBiblio));        //变体4...    title 即 title，cls.ID 作 ancestorIDs

            //多种情况可能导致list中含重复元素，去重：(已改写元素(Biblio)的equals方法，认为Biblio ID相等即Biblio相等)...
            ArrayList<Biblio> biblioList = new ArrayList<>();
            for (Biblio b : list) {
                if(!biblioList.contains(b)) {
                    biblioList.add(b);
                }
            }
*/
            List<Biblio> biblioList = ibcService.biblioQuery(filterBiblio, page, rows);
            //参考Spring databinder通用反射工具类没写成之前，只能先暂时这么麻烦一下...
            StringBuilder sb = new StringBuilder();
            sb.append("&ISBN13=").append(filterBiblio.getISBN13())
                    .append("&cls.ID=").append(filterBiblio.getCls().getID())
                    .append("&title=").append(filterBiblio.getTitle())
                    .append("&authors=").append(filterBiblio.getAuthors())
                    .append("&translators=").append(filterBiblio.getTranslators())  //要是有 通用反射工具...
                    .append("&doFilter=doFilter");

            model.addAttribute("pageInfo", new PageInfo<Biblio>(biblioList))
                    .addAttribute("filterBiblio", filterBiblio)
                    .addAttribute("filterStr", sb.toString());  //要是有 通用反射工具...

        }

        return "modules/inLibBookCircu/biblioQuery";
    }

    @RequestMapping("books")
    @ResponseBody
    public List<InLibBook> retrieveBooksOfTheSameBiblio(@RequestParam("biblioID") String biblioID) {
        InLibBook filterBook = new InLibBook();
        Biblio filterBiblio = new Biblio();
        filterBiblio.setID(biblioID);
        filterBook.setBiblio(filterBiblio);
        return inLibBookService.retrieveList(filterBook);
    }
}
