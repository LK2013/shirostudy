package com.shirostudy;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.SimpleByteSource;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * 自定义Realm
 * @ClassName: CustomRealm
 * @package com.shirostudy
 * @author: lk
 * @date: 2018/12/13 19:50
*/
public class CustomRealm extends AuthorizingRealm {
    HashMap<String,String> userMap=new HashMap<>();
    {
        userMap.put("Mark","73bea81c6c06bacab41a995495239545");
    }
    //重写授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //1.获取用户
        String username=principalCollection.getPrimaryPrincipal().toString();
        //2.通过用户获取角色数据
        Set<String> roles=getRolesByUsername(username);
        if(roles==null){return null;}
        //3.模拟从数据库或缓存中获取权限
        Set<String> permissions=getPermissionsByUserName(username);

        //4.设置角色、权限
        SimpleAuthorizationInfo authorizationInfo=new SimpleAuthorizationInfo();
        authorizationInfo.setRoles(roles);
        authorizationInfo.setStringPermissions(permissions);
        return authorizationInfo;
    }
    //重写认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //1.从主体传过来用户名
        String username=authenticationToken.getPrincipal().toString();
        //2.根据用户名从数据库中获取凭证
        String password = getPasswordByUserName(username);
        if(password==null){return null;}

        SimpleAuthenticationInfo authenticationInfo=new SimpleAuthenticationInfo(username,password,"customRealm");
        authenticationInfo.setCredentialsSalt(new SimpleByteSource("mark"));//加盐
        return authenticationInfo;
    }
    //模拟从数据库查询密码
    private String getPasswordByUserName(String username) {
        return userMap.get(username);
    }

    //模拟从数据库根据用户名获取角色
    private Set<String> getRolesByUsername(String username) {
        Set<String> roles=new HashSet<>();
        roles.add("admin");
        roles.add("role1");
        return roles;
    }
    //模拟从数据库根据用户名获取角色
    private Set<String> getPermissionsByUserName(String username) {
        Set<String> permissions=new HashSet<>();
        permissions.add("user:delete");
        permissions.add("user:update");
        return permissions;
    }

    public static void main(String[] args) {
        Md5Hash md5Hash=new Md5Hash("123456","mark");//md5加盐

        System.out.println(md5Hash.toString());
    }
}
