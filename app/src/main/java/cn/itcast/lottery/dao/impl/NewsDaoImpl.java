package cn.itcast.lottery.dao.impl;

import java.util.List;

import android.content.Context;
import android.database.Cursor;
import cn.itcast.lottery.ConstantValue;
import cn.itcast.lottery.bean.News;
import cn.itcast.lottery.dao.DBHelper;
import cn.itcast.lottery.dao.NewsDao;
import cn.itcast.lottery.dao.base.DaoSupport;

/**
 * 新闻操作
 * 
 * @author Administrator
 * 
 */
public class NewsDaoImpl extends DaoSupport<News> implements NewsDao {

	public NewsDaoImpl(Context context) {
		super(context);
	}

	public List<News> getLastNews() {
		return queryByCondition(null, null, DBHelper.TABLE_NEW_DATE_COL + " desc", "0," + ConstantValue.NEWS_SHOW_NUM);
	}

	@Override
	public boolean hasNews(String guid) {
		// 方式一：获取一个News的结果集
		// List<News> queryByCondition = queryByCondition(DBHelper.TABLE_NEW_GUID_COL + "=?", new String[] { guid });
		// return queryByCondition.size() > 0;
		// 方式二：获取数据库中该标示的信息个数
		int count = 0;
		Cursor cursor = null;
		try {
			cursor = database.rawQuery("select count(*) from " + DBHelper.TABLE_NEW + " where " + DBHelper.TABLE_NEW_GUID_COL + "=?", new String[] { guid });
			if (cursor != null && cursor.moveToNext()) {
				count = cursor.getInt(0);
			}

		} finally {
			if (cursor != null)
				cursor.close();
		}

		return count > 0;
	}

}
