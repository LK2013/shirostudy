package com.shirostudy;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Test;


public class AuthorizationTest {
    SimpleAccountRealm simpleAccountRealm=new SimpleAccountRealm();
    @Before
    public void realm(){
        simpleAccountRealm.addAccount("Mark","123456","admin","user1");
    }
    @Test
    public  void authorize() {
        //1.构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager=new DefaultSecurityManager();
        defaultSecurityManager.setRealm(simpleAccountRealm);

        //2.构建主体
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject=SecurityUtils.getSubject();

        //3.添加认证
        AuthenticationToken token= new  UsernamePasswordToken("Mark","123456");
        subject.login(token);
        subject.isAuthenticated();
        subject.checkRoles("admin");
    }
}
