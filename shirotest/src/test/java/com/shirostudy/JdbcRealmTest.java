package com.shirostudy;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

public class JdbcRealmTest {
    //创建数据源
    DruidDataSource dataSource=new DruidDataSource();
    {
        dataSource.setUrl("jdbc:mysql://192.168.194.128:3306/shirotest");
        dataSource.setUsername("root");
        dataSource.setPassword("123456");
    }
    @Test
    public void test(){
        //JdbcRealm
        JdbcRealm jdbcRealm=new JdbcRealm();
        jdbcRealm.setDataSource(dataSource);
        jdbcRealm.setPermissionsLookupEnabled(true);
        String sql_quthen="select password from user where username=?";
        jdbcRealm.setAuthenticationQuery(sql_quthen);//设置认证sql
        String sql_role="select role from user_role where username=?";
        jdbcRealm.setUserRolesQuery(sql_role);
        String sql_permission="select permission from role_permission where role=?";
        jdbcRealm.setPermissionsQuery(sql_permission);
        //1.构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager=new DefaultSecurityManager();
        defaultSecurityManager.setRealm(jdbcRealm);
        //2.主体提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject= SecurityUtils.getSubject();

        AuthenticationToken token=new UsernamePasswordToken("Mark","123456");
        //登录
        subject.login(token);

        System.out.println("isAuthenticated:"+subject.isAuthenticated());

        subject.checkRoles("admin");

        subject.checkPermission("delete");
    }
}
