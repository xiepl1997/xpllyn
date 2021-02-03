/**
 * 登陆控制
 * author:Xie Peiliang
 * date:2020/2/5
 */
package com.xpllyn.controller;

import com.xpllyn.pojo.User;
import com.xpllyn.service.impl.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LoginController {
    @Autowired
    private UserService userService;

    @RequestMapping("/loginpage")
    public ModelAndView loginPage(){
        ModelAndView mv = new ModelAndView();
        mv.addObject("tab_index", 5);
        mv.setViewName("login");
        return mv;
    }

    @RequestMapping("/register")
    public String register(HttpServletRequest request,
                           Model model) {
        String email = request.getParameter("email");
        String name = request.getParameter("name");
        String pwd = request.getParameter("password");
        String sex = request.getParameter("sex");
        boolean flag = userService.addNewUser(email, name, pwd, sex);
        if (!flag) {
            model.addAttribute("msg", "该email已经使用！");
            return "login";
        }

        //获取subject
        Subject subject = SecurityUtils.getSubject();
        //封装用户数据
        UsernamePasswordToken token = new UsernamePasswordToken(email, pwd, false);
        subject.login(token);

        return "redirect:/chatroom";
    }

    @RequestMapping("/login")
    public String login(@RequestParam("email") String email,
                        @RequestParam("password") String password,
                        @RequestParam(value = "rememberMe", required = false) boolean rememberMe,
                        Model model) {
        //获取subject
        Subject subject = SecurityUtils.getSubject();
        //封装用户数据
        UsernamePasswordToken token = new UsernamePasswordToken(email, password, rememberMe);
        try {
            subject.login(token);
        } catch (UnknownAccountException e) {
            model.addAttribute("msg", "用户名不存在！");
            return "login";
        } catch (IncorrectCredentialsException e) {
            model.addAttribute("msg", "密码错误！");
            return "login";
        }
        return "redirect:/chatroom";
    }
}
