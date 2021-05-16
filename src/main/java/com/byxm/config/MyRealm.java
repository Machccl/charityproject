package com.byxm.config;

import com.byxm.model.User;
import com.byxm.services.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

public class MyRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        Subject subject= SecurityUtils.getSubject();
        Session session=subject.getSession();
        User user = (User) session.getAttribute("user");
        Set<String> roles = new HashSet<>();
        if(!user.getPower().equals("0")){
            roles.add("manage");
        }
        if(user.getPower().equals("2")){
            roles.add("supermanage");
        }
        SimpleAuthorizationInfo simpleAuthorizationInfo=new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setRoles(roles);
        return simpleAuthorizationInfo;
    }

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        UsernamePasswordToken token=(UsernamePasswordToken) authenticationToken;
        String username=token.getUsername();

        //数据库中取出用户名，密码
        User user = userService.getUser(username);

        //验证用户名
        if(user==null){
            return null; //抛出异常 UnknownAccountException 用户名不存在
        }
        Subject subject = SecurityUtils.getSubject();
        subject.getSession().setAttribute("user",user);
        //验证密码  由shiro完成
        return new SimpleAuthenticationInfo(username,user.getPassword(),"");
    }
}
