package com.ackerley.library.modules.sys.web;

import com.ackerley.library.common.web.BaseController;
import com.ackerley.library.modules.sys.entity.Bookshelf;
import com.ackerley.library.modules.sys.service.BookshelfService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Created by ackerley on 2018/5/14.
 */
@Controller
@RequestMapping(path = {"/sys/fclt/bookshelf"})
public class BookshelfController extends BaseController{
    @Autowired
    private BookshelfService bss;

    @ModelAttribute(name = "bookshelf")     //【鸣】name/value/缺省(仅给值)，若省略，默认为decapitalized type name...
    public Bookshelf get(@RequestParam(name = "ID", required = false) String ID) {
        if (!StringUtils.isEmpty(ID)) {
            Bookshelf bs = bss.retrieveOne(ID);
            return bs;  //为方便debug才多写了redundant local variable...
        } else {
            return new Bookshelf();
        }
    }

    @RequestMapping(path = {"create", "update"})    //之所以不用统一用form作为path name，个人觉得path name能保留/体现一定的语义比较清晰？
    public String form(Bookshelf bs, Model model) {
        return "modules/sys/facility/bookshelfForm";
    }

    @RequestMapping(path = {"", "list"})
    public String list(@RequestParam(name = "page", required = false, defaultValue = "1") int page,
                       @RequestParam(name = "rows", required = false, defaultValue = "10") int rows,
                       Bookshelf filterBookshelf, Model model) {
        model.addAttribute("filterBookshelf", filterBookshelf);
        List<Bookshelf> list = bss.retrieveList(filterBookshelf, page, rows);
        model.addAttribute("pageInfo", new PageInfo<>(list));
        return "modules/sys/facility/bookshelfIndex";
    }

    @RequestMapping(path = "save")
    public String save(Bookshelf bs, Model model, RedirectAttributes ras) {
        //validate
        int modNum = bss.saveOne(bs);
        if(modNum > 0){addMessage(ras, "保存书架"+bs.getLabel()+"成功");}else{addMessage(ras, "保存书架"+bs.getLabel()+"失败");}
        return "redirect:/sys/fclt/bookshelf";
    }

    @RequestMapping(path = "delete")
    public String delete(Bookshelf bs, RedirectAttributes ras) {
        int modNum = bss.deleteOne(bs);
        if(modNum > 0){addMessage(ras, "删除书架"+bs.getLabel()+"成功");}else{addMessage(ras, "删除书架"+bs.getLabel()+"失败");}
        return "redirect:/sys/fclt/bookshelf";
    }

}
