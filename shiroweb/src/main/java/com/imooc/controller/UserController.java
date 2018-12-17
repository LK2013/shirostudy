package com.imooc.controller;

import com.imooc.vo.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {

    @RequestMapping(value="/subLogin",method = RequestMethod.POST)
    @ResponseBody
    public String subLogin(User user){

        Subject subject= SecurityUtils.getSubject();

        UsernamePasswordToken token=new UsernamePasswordToken(user.getUsername(),user.getPassword());

        try {
            //记住我
            token.setRememberMe(user.isRememberMe());

            subject.login(token);

            subject.checkPermission("delete");

            if(subject.isAuthenticated()){
                if(subject.hasRole("admin")){
                    return "login_success.role_success";
                }
            }

        } catch (AuthenticationException e) {
            return e.getMessage();
        }


        return "login_success";
    }

    //注解加角色权限
    @RequiresRoles("admin")
    @RequestMapping(value="/testRole",method =RequestMethod.GET)
    @ResponseBody
    public String testRole(){
        return "testRole";

    }

    @RequiresRoles("admin")
    @RequestMapping(value="/testRole1",method =RequestMethod.GET)
    @ResponseBody
    public String testRole1(){
        return "testRole1";

    }
    @RequestMapping(value="/testRoles1",method =RequestMethod.GET)
    @ResponseBody
    public String testRoles1(){
        return "testRole1";

    }

}
