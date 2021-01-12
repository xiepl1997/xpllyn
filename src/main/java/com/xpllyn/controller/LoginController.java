/**
 * 登陆控制
 * author:Xie Peiliang
 * date:2020/2/5
 */
package com.xpllyn.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {

    @RequestMapping("/login")
    public ModelAndView Login(){
        ModelAndView mv = new ModelAndView();
        mv.addObject("tab_index", 5);
        mv.setViewName("login");
        return mv;
    }
}
