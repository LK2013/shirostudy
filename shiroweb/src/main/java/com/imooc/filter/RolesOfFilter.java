package com.imooc.filter;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;


/**
 * 任何一个条件满足即刻
 * @ClassName: RolesOfFilter
 * @package com.imooc.filter
 * @author: lk
 * @date: 2018/12/17 11:27
*/
public class RolesOfFilter extends AuthorizationFilter{
    @Override
    protected boolean isAccessAllowed(javax.servlet.ServletRequest servletRequest, javax.servlet.ServletResponse servletResponse, Object o) throws Exception {
        Subject subject=getSubject(servletRequest,servletResponse);
        String[] roles= (String[]) o;
        if(roles==null || roles.length==0){
            return true;
        }else {
            for(String role:roles){
                if (subject.hasRole(role)){
                    return true;
                }
            }
        }
        return false;
    }
}
