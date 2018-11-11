package com.ackerley.library.common.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

/**
 * Created by ackerley on 2018/5/9.
 * 模仿jeesite的SpringContextHolder...静态存放application context的引用，方便在任意位置access到application context
 * 另：implements ContextLoaderListener 然后使用 WebApplicationContextUtils.getWebApplicationContext(servletContext) 也是另一法子...
 */

@Component  //试过，没他不行...【扣】不是spring管理的bean不能享受ApplicationContextAware的？应该是的，setApplicationContext是个spring bean生命周期回调...
@Lazy(false)
public class SpringAppCtxtUtil implements ApplicationContextAware{

    private static ApplicationContext appCtxt;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        appCtxt = applicationContext;
    }

    public static <T> T getBean(Class<T> requiredType){
        // assert一下 (application context已注入的assert) 一定要吗？   //org.apache.commons.lang3的Validate.validState来assert...
        return appCtxt.getBean(requiredType);
    }

    public static Object getBean(String beanName){
        return appCtxt.getBean(beanName);
    }

}
