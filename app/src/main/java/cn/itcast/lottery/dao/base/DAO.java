package cn.itcast.lottery.dao.base;

import java.io.Serializable;
import java.util.List;

/**
 * 实体操作通用接口
 * 
 * @author Administrator
 * 
 * @param <M>要操作的实体类
 */
public interface DAO<M> {// 多个方法用到泛型所以在接口这声明
	public long insert(M item);// 采用什么类型的输入参数Object？采用泛型去明确需要操作的实体类是那个类

	public int delete(Serializable id);

	public int update(M item);

	public List<M> queryAll();

	List<M> queryByCondition(String selection, String[] selectionArgs);

	List<M> queryByCondition(String selection, String[] selectionArgs, String orderBy, String limit);

	List<M> queryByCondition(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit);
	/**
	 *  查询当前表中记录条目
	 * @return
	 */
	Integer getCount();
}
