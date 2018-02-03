package cn.itcast.lottery.dao.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
/**
 * 确定实体中字段与数据库中字段对应关系
 */
public @interface Column {
	/**
	 * 指定数据库字段名
	 * @return
	 */
	String value();
}
