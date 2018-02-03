package cn.itcast.lottery.dao.base;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import cn.itcast.lottery.dao.DBHelper;
import cn.itcast.lottery.dao.annotation.Column;
import cn.itcast.lottery.dao.annotation.ID;
import cn.itcast.lottery.dao.annotation.Table;
import cn.itcast.lottery.util.LogUtil;

public class DaoSupport<M> implements DAO<M> {
	protected Context context;
	protected static DBHelper dbHelper;
	protected static SQLiteDatabase database;

	protected DaoSupport(Context context) {
		this.context = context;
		if (dbHelper == null)
			dbHelper = new DBHelper(context);
		if (database == null || !database.isOpen())
			database = dbHelper.getWritableDatabase();
	}

	@Override
	public int delete(Serializable id) {
		// 问题一：获取到当前操作对象对应的数据库中表
		return database.delete(getTableName(null), DBHelper.TABLE_ID_COL + "=?", new String[] { id.toString() });
	}

	@Override
	public long insert(M item) {
		// 问题一：将实体中封装信息导入到ContentValues
		// 问题二：获取到当前操作对象对应的数据库中表
		ContentValues values = new ContentValues();
		fillContentValues(item, values);
		values.remove(DBHelper.TABLE_ID_COL);
		return database.insert(getTableName(item), null, values);
	}

	@Override
	public List<M> queryAll() {
		// 问题一：获取到当前操作对象对应的数据库中表
		// 问题二：获取当前正在操作的实体的实例
		// 问题三：将数据库中信息封装到实体中
		Cursor query = null;
		List<M> list = new ArrayList<M>();
		try {
			query = database.query(getTableName(null), null, null, null, null, null, null);

			while (query.moveToNext()) {
				M item = getInstance();
				fillField(query, item);
				list.add(item);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (query != null)
				query.close();
		}
		return list;
	}

	@Override
	public int update(M item) {
		// 问题一：将实体中封装信息导入到ContentValues
		// 问题二：获取到当前操作对象对应的数据库中表
		// 问题三：获取主键值
		ContentValues values = new ContentValues();
		fillContentValues(item, values);
		return database.update(DBHelper.TABLE_NEW, values, DBHelper.TABLE_ID_COL + "=?",
				new String[] { values.get(DBHelper.TABLE_ID_COL).toString() });
	}

	@Override
	public List<M> queryByCondition(String selection, String[] selectionArgs) {
		return queryByCondition(null, selection, selectionArgs, null, null, null, null);
	}

	@Override
	public List<M> queryByCondition(String selection, String[] selectionArgs, String orderBy, String limit) {
		return queryByCondition(null, selection, selectionArgs, null, null, orderBy, limit);
	}

	@Override
	public List<M> queryByCondition(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy,
			String limit) {
		List<M> result = new ArrayList<M>();
		Cursor rawQuery = null;
		// cursor返回集合
		try {
			rawQuery = database.query(getTableName(null), columns, selection, selectionArgs, groupBy, having, orderBy, limit);
			if (rawQuery != null) {
				while (rawQuery.moveToNext()) {

					M item = getInstance();

					fillField(rawQuery, item);
					result.add(item);

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rawQuery != null) {
				rawQuery.close();
				rawQuery = null;
			}
		}
		return result;
	}

	/**
	 * 问题一：获取到当前操作对象对应的数据库中表
	 * 
	 * @param item
	 * @return
	 */
	public String getTableName(M item) {
		String result = "";
		if (item == null)
			item = getInstance();
		Table annotation = item.getClass().getAnnotation(Table.class);
		if (annotation == null) {
			throw new RuntimeException("The JavaBean no @Table.");
		}
		result = annotation.value();
		return result;
	}

	/**
	 * 问题二：将实体中封装信息导入到ContentValues
	 */
	public void fillContentValues(M item, ContentValues values) {
		// values.put(DBHelper.TABLE_NEW_TITLE_COL, news.getTitle());
		if (item != null && values != null) {
			Field[] fields = item.getClass().getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				Column annotation = field.getAnnotation(Column.class);
				if (annotation != null) {
					String key = annotation.value();
					String value;
					try {
						Object object = field.get(item);
						if (object == null)
							value = "";
						else
							value = object.toString();
						values.put(key, value);
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			throw new RuntimeException("item or values is null");
		}
	}

	/**
	 * 问题三：将数据库中信息封装到实体中
	 */

	public void fillField(Cursor query, M item) {
		if (item != null && query != null) {
			Field[] fields = item.getClass().getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				Column column = field.getAnnotation(Column.class);
				if (column != null) {
					String key = column.value();
					String value = query.getString(query.getColumnIndex(key));
					try {
						boolean autoIncrement = false;
						ID id = field.getAnnotation(ID.class);
						if (id != null) {
							autoIncrement = id.autoIncrement();
						}
						if (autoIncrement)
							field.set(item, Long.parseLong(value));
						else
							field.set(item, value);
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			throw new RuntimeException("item or query is null");
		}
	}

	/**
	 * 问题四：获取主键值
	 */
	public String getPrimaryKeyValue(M item) {
		if (item != null) {
			Field[] fields = item.getClass().getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				ID id = field.getAnnotation(ID.class);
				if (id != null) {
					try {
						return field.get(item).toString();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
					break;
				}
			}

		} else {
			throw new RuntimeException("item is null");
		}
		return null;
	}

	/**
	 * 问题五:获取当前正在操作的实体的实例
	 * 
	 * @return
	 */
	public M getInstance() {
		ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
		Class clazz = (Class) pt.getActualTypeArguments()[0];
		try {
			M m = (M) clazz.newInstance();
			return m;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Integer getCount() {
		int result = 0;
		Cursor rawQuery = null;
		try {
			rawQuery = database.rawQuery("select count(*) from " + getTableName(getInstance()), null);
			if (rawQuery.moveToNext()) {
				result = rawQuery.getInt(0);
			}
		} finally {
			if (rawQuery != null)
				rawQuery.close();
		}

		return result;
	}

}
