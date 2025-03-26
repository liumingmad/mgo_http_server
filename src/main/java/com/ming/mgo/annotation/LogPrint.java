package com.ming.mgo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD) // 注解只能用在方法上
@Retention(RetentionPolicy.RUNTIME) // 运行时生效
public @interface LogPrint {
    String value() default "";
}