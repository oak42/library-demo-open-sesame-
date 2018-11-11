package com.ackerley.library.modules.sys.web;

import com.ackerley.library.common.web.BaseController;
import com.ackerley.library.modules.sys.entity.BiblioCls;
import com.ackerley.library.modules.sys.service.BiblioClsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(path = "/sys/biblioCls")
public class BiblioClsController extends BaseController {
    @Autowired
    private BiblioClsService bcs;



    @RequestMapping(path = "jsonTreeData")
    @ResponseBody
    public List<Map<String, Object>> treeData() {

        BiblioCls dummyFilter = new BiblioCls();
        List<BiblioCls> biblioClsList = bcs.retrieveList(dummyFilter);

        List<Map<String, Object>> mapList = new ArrayList<>();

        for (BiblioCls bc : biblioClsList) {

            Map<String, Object> map = new HashMap<>();
            map.put("id", bc.getID());
            map.put("pId", bc.getParentID());
            map.put("name", bc.getNotation() + " : " + bc.getName());
            mapList.add(map);
        }

        return mapList;
    }


}
