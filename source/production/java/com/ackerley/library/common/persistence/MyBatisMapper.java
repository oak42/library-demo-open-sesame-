package com.ackerley.library.common.persistence;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Created by ackerley on 2018/5/16.
 * 方便mapper scanner扫描；@Component消去intellij的“could not autowire，no beans of...found...”警告...
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Component  //★★★★★★★
public @interface MyBatisMapper {

}
