package com.ackerley.library.applicationConfig;

import com.ackerley.library.modules.sys.web.UserController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@EnableWebMvc   /* 等效于 <mvc:annotation-driven/> */
@ComponentScan(basePackages = {"com.ackerley.library"},
                useDefaultFilters = false,
//                excludeFilters = {@ComponentScan.Filter(Configuration.class)},    //useDefaultFilters = false 已经足够了吧...
                includeFilters = {@ComponentScan.Filter(Controller.class)})
public class WebConfig extends WebMvcConfigurerAdapter{

    @Bean
    public ViewResolver ackerleyViewResolver(){
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/jsp/views/");
        resolver.setSuffix(".jsp");
        resolver.setExposeContextBeansAsAttributes(true);   //Set whether to make all Spring beans in the application context
                                                            // accessible as request attributes, through lazy checking once
                                                            // an attribute gets accessed.
                                                            // This will make all such beans accessible in plain ${...} expressions
                                                            // in a JSP 2.0 page, as well as in JSTL's c:out value expressions.
        return resolver;
    }

    @Override
/*  Configure a handler to delegate unhandled requests by forwarding to the Servlet container's "default" servlet.
    A common use case for this is when the DispatcherServlet is mapped to "/" thus overriding the Servlet container's default handling of static resources. */
    public  void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer){
        configurer.enable();    //Enable forwarding to the "default" Servlet. When this method is used the DefaultServletHttpRequestHandler
                                // will try to autodetect the "default" Servlet name. Alternatively, you can specify the name of the
                                // default Servlet via enable(String).
                                // By calling enable() on the given DefaultServletHandlerConfigurer, you’re asking DispatcherServlet to
                                // forward requests for static resources to the servlet container’s default servlet and not to try to
                                // handle them itself.
    }


/*
    Spring Framework automatically creates certain message converters on your behalf if you don’t confgure any manually.
    In many cases, this automatic confguration is suffcient.

    The MappingJackson2HttpMessageConverter is normally created automatically as long as the Jackson Data Processor 2 is on the classpath.

    ......override the configureMessageConverters method......
*/


    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
//        super.configureContentNegotiation(configurer);
        configurer.mediaType("json", MediaType.APPLICATION_JSON);
    }

}
