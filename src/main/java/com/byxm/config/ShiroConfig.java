package com.byxm.config;

import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    //ShiroFilterFactoryBean
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("getDefaultWebSecurityManager") DefaultWebSecurityManager defaultWebSecurityManager){
        ShiroFilterFactoryBean bean=new ShiroFilterFactoryBean();
        bean.setSecurityManager(defaultWebSecurityManager); //设置安全管理器
        bean.setSuccessUrl("/index");//登录成功转向的页面
        bean.setUnauthorizedUrl("/noPermission");//没有权限时跳转的页面
        //添加shiro的内置过滤器
        /*
        * anon: 无需认证就能访问
        * authc: 必须认证才能访问
        * user: 必须拥有 记住我 功能才能访问
        * perms: 拥有对某个资源的权限才能访问
        * role: 拥有某个角色权限才能访问
        * */
        Map<String,String> filtermap = new LinkedHashMap<>();
        filtermap.put("/user/**","authc");
        filtermap.put("/manage/**","authc,roles[manage]");
        filtermap.put("/smanage/**","authc,roles[supermanage]");

        bean.setFilterChainDefinitionMap(filtermap);
        //设置登录路径
        bean.setLoginUrl("/login");
        return bean;
    }

    //DefaultWebSecurityManager
    @Bean
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("myRealm") Realm myRealm){
        DefaultWebSecurityManager securityManager=new DefaultWebSecurityManager();
        securityManager.setRealm(myRealm); //关联MyRealm
        return securityManager;
    }

    //创建Realm对象
    @Bean
    public MyRealm myRealm(){
        return new MyRealm();
    }
}
