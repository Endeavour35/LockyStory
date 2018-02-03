package cn.itcast.lottery.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库操作
 * 
 * @author Administrator
 * 
 */
public class DBHelper extends SQLiteOpenHelper {

	public static final String DBNAME = "itcast.db";
	public static final int STARTVERSION = 1;
	public static final int DBVERSION_HISTORY_1 = 2;
	public static final int DBVERSION_CURRENT = 3;

	/**
	 * 通用主键
	 */
	public static final String TABLE_ID_COL = "_ID";// 主键
	/**
	 * 新闻表
	 */
	public static final String TABLE_NEW = "news";
	public static final String TABLE_NEW_TITLE_COL = "TITLE";// 新闻标题
	public static final String TABLE_NEW_SUMMARY_COL = "SUMMARY";// 新闻摘要
	public static final String TABLE_NEW_LINK_COL = "LINK";// 新闻链接
	public static final String TABLE_NEW_AUTHOR_COL = "AUTHOR";// 作者
	public static final String TABLE_NEW_SOURCE_COL = "SOURCE";// 来源
	public static final String TABLE_NEW_IMGURL_COL = "IMGURL";// 新闻图片地址
	public static final String TABLE_NEW_DATE_COL = "DATE";// 发布时间
	public static final String TABLE_NEW_GUID_COL = "GUID";// 服务器端资源唯一标示
	public static final String TABLE_NEW_RESERVED1_COL = "RESERVED1";// 扩展字段1
	public static final String TABLE_NEW_RESERVED2_COL = "RESERVED2";// 扩展字段2

	/**
	 * 收藏表
	 */
	public static final String TABLE_COLLECT = "collect";
	public static final String TABLE_COLLECT_TITLE_COL = "TITLE";
	public static final String TABLE_COLLECT_HREF_COL = "URL";

	/**
	 *专家
	 */
	public static final String TABLE_EXPORT = "export";
	public static final String TABLE_EXPORT_GUID_COL = "guid";

	private Context context;

	public DBHelper(Context context) {
		super(context, DBNAME, null, DBVERSION_CURRENT);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + TABLE_NEW + " (" + //
				TABLE_ID_COL + " integer primary key autoincrement, " + //
				TABLE_NEW_TITLE_COL + " varchar(50), " + //
				TABLE_NEW_SUMMARY_COL + " varchar(200), " + //
				TABLE_NEW_LINK_COL + " varchar(200), " + //
				TABLE_NEW_AUTHOR_COL + " varchar(200), " + //
				TABLE_NEW_SOURCE_COL + " varchar(200), " + //
				TABLE_NEW_IMGURL_COL + " varchar(200), " + //
				TABLE_NEW_DATE_COL + " varchar(20), " + //
				TABLE_NEW_GUID_COL + " varchar(20), " + //
				TABLE_NEW_RESERVED1_COL + " varchar(200), " + //
				TABLE_NEW_RESERVED2_COL + " varchar(200))"//
		);
		if (STARTVERSION < DBVERSION_CURRENT)
			onUpgrade(db, STARTVERSION, DBVERSION_CURRENT);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		switch (oldVersion) {
			case STARTVERSION:
				db.execSQL("CREATE TABLE " + TABLE_COLLECT + " (" + //
						TABLE_ID_COL + " integer primary key autoincrement, " + //
						TABLE_COLLECT_TITLE_COL + " varchar(200), " + //
						TABLE_COLLECT_HREF_COL + " VARCHAR(200))"//
				);
			case DBVERSION_HISTORY_1:
				db.execSQL("CREATE TABLE " + TABLE_EXPORT + " (" + //
						TABLE_EXPORT_GUID_COL + " VARCHAR(20))"//
				);
			case DBVERSION_CURRENT:
				break;
		}
		
	}

}
