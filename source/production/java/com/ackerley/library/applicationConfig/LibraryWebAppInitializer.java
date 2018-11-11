package com.ackerley.library.applicationConfig;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class LibraryWebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

        @Override       //AbstractDispatcherServletInitializer.getServletMappings - Specify the servlet mapping(s) for the DispatcherServlet — for example "/", "/app", etc.
        protected String[] getServletMappings(){
                return new String[]{"/"};
        }

        @Override       //Specify @Configuration and/or @Component classes to be provided to the root application context.
        protected Class<?>[] getRootConfigClasses(){
                return new Class<?>[]{RootConfig.class};
        }

        @Override       //Specify @Configuration and/or @Component classes to be provided to the dispatcher servlet application context.
        protected Class<?>[] getServletConfigClasses(){
                return new Class<?>[]{WebConfig.class};
        }

}