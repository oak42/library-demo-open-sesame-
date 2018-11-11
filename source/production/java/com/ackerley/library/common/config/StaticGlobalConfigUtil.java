package com.ackerley.library.common.config;

import com.ackerley.library.common.utils.PropertiesUtil;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

/**
 * Created by ackerley on 2018/5/8.
 * 模仿、乞丐版JeeSite的静态全局单例Global类...希望做些改进...
 */
public class StaticGlobalConfigUtil {

//    private static Hashtable<String, String> configs = new Hashtable<>();   //考虑不如直接用 Spring的PropertiesLoaderUtils + jdk的Properties ？

    private static Properties props = new Properties();//Properties extends Hashtable<Object,Object> 哦...不是Hashtable<String, String>哦...但是试过可以凭String来get value，择机看源码...

    static {
        PropertiesUtil.loadInto(props, "application.properties");
    }

    public static String getConfig(String key) {    //...facade...
        String value = props.getProperty(key);
        return value == null ? "" : value;
    }

    public static void main(String[] args) {
        System.out.println(getConfig("brandName"));
    }
}
