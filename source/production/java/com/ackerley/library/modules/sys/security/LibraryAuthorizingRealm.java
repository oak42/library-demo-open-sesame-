package com.ackerley.library.modules.sys.security;

import com.ackerley.library.modules.sys.entity.Menu;
import com.ackerley.library.modules.sys.entity.Role;
import com.ackerley.library.modules.sys.entity.User;
import com.ackerley.library.modules.sys.repository.RoleMapper;
import com.ackerley.library.modules.sys.service.UserService;
import com.ackerley.library.modules.sys.utils.SecurityUtil;
import com.ackerley.library.modules.sys.utils.UserUtil;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.List;

@Service    //realm 是 service
public class LibraryAuthorizingRealm extends AuthorizingRealm {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleMapper roleMapper;

    //
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) {

        UsernamePasswordToken token = (UsernamePasswordToken)authcToken;

        //验证码(后端)校验，阉掉...

        //校验用户名密码：根据login name获取user，参与构造AuthenticationInfo返回，比对交给shiro credential matcher...
        User user = userService.getUserByLoginName(token.getUsername());
        if(user != null) {
            //判断是否被禁止登陆...阉掉了...

            byte[] salt = SecurityUtil.decodeHex(user.getPwd().substring(0, 16));
            return new SimpleAuthenticationInfo(
                    new Principal(user),            //principal - the 'primary' principal associated with the specified realm.
                    user.getPwd().substring(16),    //hashedCredentials - the hashed credentials that verify the given principal.
                    ByteSource.Util.bytes(salt),    //credentialsSalt - the salt used when hashing the given hashedCredentials.
                    getName());                     //realmName - the realm from where the principal and credentials were acquired.
        } else {
            return null;
        }
    }

    //
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        Principal principal = (Principal)getAvailablePrincipal(principals);     //CachingRealm.getAvailablePrincipal

        //先不考虑 是否多点登录 的问题...

        User currentUser = userService.getUserByLoginName(principal.getUserLoginName());
        if(currentUser != null) {   //此处能证明至少是user，排除了anonymous的可能性？
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            info.addStringPermission("user");   //也学着jeesite 加一个user级权限...

            //
            List<Menu> list = UserUtil.getMenuList();
            for(Menu m : list) {
                String p = m.getRequiredPermissions();
                if(!StringUtils.isEmpty(p)) {  //权限菜单项，而非展现菜单项
                    //string permissions乃comma-separated...
                    String[] stringPermissions = p.split(",");
                    for(String permission : stringPermissions) {
                        info.addStringPermission(permission);
                    }
                }
            }

            //
            List<Role> roleList = roleMapper.retrieveListByUserID(currentUser.getID());
            for(Role role : roleList) {
                info.addRole(role.getName());
            }

            return info;
        } else {
            return null;    //（应该就是anonymous了吧？）jeesite是在这里返回null，但api doc未说返回null对上层调用方意味什么...
        }
    }

    /*
	The JSR-250 @PostConstruct and @PreDestroy annotations are generally considered best practice for receiving lifecycle callbacks in a modern Spring application.
	Using these annotations means that your beans are not coupled to Spring specific interfaces.

	@Documented
	 @Retention(value=RUNTIME)
	 @Target(value=METHOD)
	public @interface PostConstruct
	The PostConstruct annotation is used on a method that needs to be executed after dependency injection is done to perform any initialization.
	This method MUST be invoked before the class is put into service.
	This annotation MUST be supported on all classes that support dependency injection.
	The method annotated with PostConstruct MUST be invoked even if the class does not request any resources to be injected.
	Only one method can be annotated with this annotation.
	The method on which the PostConstruct annotation is applied MUST fulfill all of the following criteria:
		● The method MUST NOT have any parameters except in the case of interceptors in which case it takes an InvocationContext object as defined by the Interceptors specification.
		● The method defined on an interceptor class MUST HAVE one of the following signatures:
				□ void <METHOD>(InvocationContext)
				□ Object <METHOD>(InvocationContext) throws Exception
		  Note: A PostConstruct interceptor method must not throw application exceptions,
			    but it may be declared to throw checked exceptions including the java.lang.Exception
			    	if the same interceptor method interposes on business or timeout methods in addition to lifecycle events.
			    If a PostConstruct interceptor method returns a value, it is ignored by the container.
		● The method defined on a non-interceptor class MUST HAVE the following signature:
				□ void <METHOD>()
		● The method on which PostConstruct is applied MAY be public, protected, package private or private.
		● The method MUST NOT be static except for the application client.
		● The method MAY be final.
		● If the method throws an unchecked exception the class MUST NOT be put into service except in the case of EJBs where the EJB can handle exceptions and even recover from them.
	*/
    @PostConstruct  //【鸣】应该是在bean构造完成伊始执行
    public void initCredentialsMatcher() {
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(SecurityUtil.HASH_ALGORITHM);
        matcher.setHashIterations(SecurityUtil.HASH_ITERATIONS);
        setCredentialsMatcher(matcher);    //AuthenticatingRealm.setCredentialsMatcher
    }

}
