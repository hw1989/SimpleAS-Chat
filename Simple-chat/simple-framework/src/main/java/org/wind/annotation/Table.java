package org.wind.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 类的注解（方便设置类的数据表结构）
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Table {
	String DTname() default "";
	//设置复合主键使用逗号隔开: A,b
	String UnionKey() default "";
}
