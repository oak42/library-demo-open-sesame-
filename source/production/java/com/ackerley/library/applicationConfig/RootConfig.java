package com.ackerley.library.applicationConfig;

import com.ackerley.library.modules.sys.repository.UserMapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement    //开启标注式事务控制
@ImportResource(locations = {
        "classpath:Spring-myBatis.xml",
        "classpath:Spring-applicationContext-Shiro.xml"
})
@ComponentScan(basePackages = {"com.ackerley.library"} ,
                excludeFilters = {@ComponentScan.Filter(Configuration.class), @ComponentScan.Filter(Controller.class)})
public class RootConfig {

}
