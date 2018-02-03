package cn.itcast.lottery.dao.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
/**
 * 标示实体中与数据库表主键相对应的字段信息
 */
public @interface ID {
	/**
	 * 设置主键是否自增
	 * @return
	 */
	boolean autoIncrement();
}
