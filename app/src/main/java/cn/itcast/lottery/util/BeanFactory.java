package cn.itcast.lottery.util;

import java.lang.reflect.Constructor;
import java.util.Properties;

import android.content.Context;
import android.os.Handler;
/**
 * bean工厂
 * @author Administrator
 *
 */
public class BeanFactory {
	private static Properties props = new Properties();
	static {
		try {
			props.load(BeanFactory.class.getClassLoader().getResourceAsStream(
					"BeanFactory.properties"));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 获取实现类的实例
	 * 
	 * @param simpleName
	 *            接口的简单名称
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getImpl(Class<T> clazz) {
		// 2，获取实现类的全限定名
		String simpleName = clazz.getSimpleName();
		String className = props.getProperty(simpleName);
		// 3，生成实例并返回
		try {
			Class cls = Class.forName(className);
			T base = (T) cls.newInstance(); 
			return base;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	@SuppressWarnings("unchecked")
	public static <T> T getImpl(Class<T> clazz,Context context) {
		// 2，获取实现类的全限定名
		String simpleName = clazz.getSimpleName();
		String className = props.getProperty(simpleName);
		// 3，生成实例并返回
		try {
			Class cls = Class.forName(className);
			processTransactional(cls);
			Class[] paramTypes = { Context.class };
			Object[] params = { context }; // 方法传入的参数
			Constructor con = cls.getConstructor(paramTypes); 
			T base = (T) con.newInstance(params); 
			return base;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 获取实现类的实例
	 * 
	 * @param simpleName
	 *            接口的简单名称
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getImpl(Class<T> clazz,Context context,Handler handler) {
		// 2，获取实现类的全限定名
		String simpleName = clazz.getSimpleName();
		String className = props.getProperty(simpleName);
		// 3，生成实例并返回
		try {
			Class cls = Class.forName(className);
			Class[] paramTypes = { Context.class ,Handler.class};
			Object[] params = { context ,handler}; // 方法传入的参数
			Constructor con = cls.getConstructor(paramTypes); 
			T base = (T) con.newInstance(params); 
			return base;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 处理事务
	 * @param cls
	 */
	private static void processTransactional(Class cls)
	{
		
	}
}
