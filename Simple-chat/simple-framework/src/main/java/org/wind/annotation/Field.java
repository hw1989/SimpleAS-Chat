package org.wind.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 类的属性注解（方便设置列的创建）
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Field {

	// 对应的类名
	public String name() default "";

	// 数据的类型 主键默认为int
	public String type() default "Text";

	// 是否为主键,对象中不存在主键
	@Deprecated
	public boolean key() default false;

	// 是否自增
	public boolean add() default false;

	// 设置文本字段的长度
	public int size() default 10;

	// 设置唯一字段
	public boolean unique() default false;
}