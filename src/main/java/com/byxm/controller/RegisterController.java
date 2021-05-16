package com.byxm.controller;

import com.byxm.model.User;
import com.byxm.services.UserService;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Controller
public class RegisterController {
    @Autowired
    private DefaultKaptcha defaultKaptcha;

    @Autowired
    private UserService userService;

    @RequestMapping("/register")
    public String register(){

        return "register";
    }
    //生成验证码
    @RequestMapping("/codeimage")
    public void getkaptcha(HttpServletRequest request, HttpServletResponse response)throws Exception {
        String text = defaultKaptcha.createText();
        request.getSession().setAttribute("code", text);
        BufferedImage image = defaultKaptcha.createImage(text);
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            ImageIO.write(image, "jpg", outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //验证码验证
    @RequestMapping("/register/codecheck")
    @ResponseBody
    public String registercheck(HttpServletRequest request, String code){
        if(code!=null){
            String checkcode = (String) request.getSession().getAttribute("code");
//            System.out.println("code-->"+code+"  sessioncode-->"+checkcode);
            if(checkcode.equals(code)){
                return "true";
            }else {
                return "false";
            }
        }
        return "false";
    }
    //验证用户名是否重复
    @RequestMapping("/register/usercheck")
    @ResponseBody
    public String usercheck(String username){
        User user = userService.getUser(username);
        if(user==null){
            return "true";
        }
        return "false";
    }
    //执行注册操作
    @RequestMapping(value = "/doregister",method = RequestMethod.POST)
    @ResponseBody
    public Object doRegister(String username,String password){
        String result = userService.register(username,password);
        return result;
    }
}
