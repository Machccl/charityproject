package com.byxm.controller;

import com.byxm.model.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@Slf4j
public class LoginController {

    @RequestMapping("/login")
    public String login(){
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getSession().getAttribute("user");
        if(user!=null){
            return "redirect:/index";
        }
        return "login";
    }

    @RequestMapping(value = "/checkLogin",method = RequestMethod.POST)
    @ResponseBody
    public Object checkLogin(HttpServletRequest request,String username, String password){
        Subject subject= SecurityUtils.getSubject();
        User user = (User) subject.getSession().getAttribute("user");
        if(user!=null){
            return "redirect:/index";
        }
        UsernamePasswordToken token = new UsernamePasswordToken(username,password);
        try {
            subject.login(token);//执行登录操作，没有异常说明成功
            request.getSession().setAttribute("username",username);
            log.info("ip:"+request.getRemoteAddr()+"---username:"+username+"-Login");
            return "true";
        } catch (UnknownAccountException e) {
            return "false";
        } catch (IncorrectCredentialsException e) {
            subject.getSession().removeAttribute("user");
            return "false";
        }
    }
}
