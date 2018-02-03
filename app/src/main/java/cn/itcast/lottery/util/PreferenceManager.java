package cn.itcast.lottery.util;

import org.apache.commons.lang3.StringUtils;

import cn.itcast.lottery.ConstantValue;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

/**
 * 管理用户信息
 * 
 * @author rui.cao@soarmobile.com.cn
 */
public class PreferenceManager {

	public static final String PREFERENCE_NAME = "zc_preference";
	private static final String TAG = "PreferenceManager";

	/**
	 * 注册时初始化相关信息
	 * 
	 * @param context
	 * @param userName
	 * @param password
	 */
	public static void init(Context context, String userName, String password) {
		SharedPreferences sp = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putBoolean("auto_login", false);
		edit.putString("username", userName);
		edit.putString("password", password);
		edit.commit();
	}

	/**
	 * 修改自动登录信息
	 * 
	 * @param context
	 * @param switchValue
	 */
	public static void changeAutoLogin(Context context, boolean switchValue) {
		SharedPreferences sp = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putBoolean("auto_login", switchValue);
		edit.commit();
	}

	/**
	 * 改变用户登录信息
	 * 
	 * @param context
	 * @param userName
	 * @param password
	 */
	public static void changeUserInfo(Context context, String userName, String password) {
		SharedPreferences sp = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putString("username", userName);

		DES des = new DES();
		password = des.authcode(password, "DECODE", ConstantValue.DES_KEY);

		edit.putString("password", password);
		edit.commit();
	}

	/**
	 * 改变用户账户信息
	 * 
	 * @param context
	 * @param investvalues
	 * @param cashvalues
	 */
	public static void changeUserMoneyInfo(Context context, String investvalues, String cashvalues) {
		SharedPreferences sp = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putString("investvalues", investvalues);
		edit.putString("cashvalues", cashvalues);
		edit.commit();
	}

	/**
	 * 检查是否需要自动登录
	 * 
	 * @param context
	 * @return
	 */
	public static boolean checkAutoLogin(Context context) {
		SharedPreferences sp = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		return sp.getBoolean("auto_login", false);
	}

	public static String getUserName(Context context) {
		SharedPreferences sp = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		return sp.getString("username", "");
	}

	public static String getPassword(Context context) {
		SharedPreferences sp = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		DES des = new DES();
		String password = sp.getString("password", "");
		if (StringUtils.isNotBlank(password))
			password = des.authcode(password, "ENCODE", ConstantValue.DES_KEY);
		return password;
	}

	/**
	 * 新手导航
	 * 
	 * @param context
	 */
	public static boolean getNoviceNavigationState(Context context) {
		SharedPreferences sp = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		return sp.getBoolean("new_user", true);
	}

	public static void changeNoviceNavigationState(Context context, boolean value) {
		SharedPreferences sp = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putBoolean("new_user", value);
		edit.commit();
	}

	/**
	 * 第一次启动
	 * 
	 * @param context
	 * @return
	 */
	public static boolean fristRun(Context context) {
		SharedPreferences sp = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		return sp.getBoolean("frist_run", true);
	}

	public static void checkVersionUpdate(Context context) {
		SharedPreferences sp = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		String version = sp.getString("version", "v1.0.2");

		PackageManager pm = context.getPackageManager();
		String versionName = "";
		try {
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionName = pi.versionName;
		} catch (NameNotFoundException e) {
			versionName = "";
		}
		boolean checkVersion = checkVersion(versionName, version);
		Log.i(TAG, "checkVersion:" + checkVersion);
		if (checkVersion) {
			Editor edit = sp.edit();
			edit.putString("version", versionName);
			edit.putBoolean("frist_run", true);
			edit.commit();
		}
	}

	public static void changeFristRunState(Context context, boolean value) {
		SharedPreferences sp = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putBoolean("frist_run", value);
		edit.commit();
	}

	private static boolean checkVersion(String sVersionName, String versionName) {
		boolean result = false;
		try {

			if (StringUtils.isNotBlank(versionName)) {
				if (versionName.compareTo(sVersionName) < 0)
					result = true;
			} else {
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}