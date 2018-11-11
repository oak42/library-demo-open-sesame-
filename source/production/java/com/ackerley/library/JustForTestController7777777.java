package com.ackerley.library;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(path = "test")
public class JustForTestController7777777 {

    @RequestMapping
    public String testView(HttpServletRequest req) {
        return "justForTest7777777";
    }

    @RequestMapping(path = "p")
    public String ajaxTest(HttpServletRequest req, HttpServletResponse resp) throws JsonProcessingException{

        System.out.println("msg： " + req.getParameter("msg"));

        Map<String, String> map = new HashMap<>();
        map.put("msg", "server hello!");

        ObjectMapper mapper = new ObjectMapper();
        String str = mapper.writeValueAsString(map);

        return "justForTest7777777";
    }

}
