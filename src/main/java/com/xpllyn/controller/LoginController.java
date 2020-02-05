/**
 * 登陆控制
 * author:Xie Peiliang
 * date:2020/2/5
 */
package com.xpllyn.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {

    @RequestMapping("/login")
    public String Login(){
        return "login";
    }
}
