package com.ackerley.library.common.utils;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Properties;

/**
 * Created by ackerley on 2018/5/8.
 * 模仿、乞丐版JeeSite的PropertiesLoader，希望做些改进，主要封装Spring的DefaultResourceLoader、、等...
 */
public class PropertiesUtil {

    private static ResourceLoader rsrcLoader = new DefaultResourceLoader(); //【】应用ResourceLoaderAware是否更elegant？

    public static void loadInto(Properties dest, String... rsrcPaths) {    //回忆一下"String..." 相比 "String[]" 的优势

        for (String location : rsrcPaths) {
            InputStream inputStream = null;
            try{
                inputStream = rsrcLoader.getResource(location).getInputStream();    //Spring的Resource没有getReader，只有getInputStream...
                dest.load(inputStream); //spi doc貌似没说明白...但试过后面的load不会override掉前面load的properties哦...择机看源码figure it out...
            } catch (IOException e) {
                //log...
            } finally {
                IOUtils.closeQuietly(inputStream);
            }

        }
    }


}
