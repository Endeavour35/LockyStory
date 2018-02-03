package cn.itcast.lottery.util;

import org.apache.commons.lang3.StringUtils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;
import cn.itcast.lottery.ConfigParams;

/**
 * 网络管理
 * 
 * @author rui.cao@soarmobile.com.cn
 * 
 */
public class NetUtil {
	public static final Uri PREFERRED_APN_URI = Uri.parse("content://telephony/carriers/preferapn");
	// public static final Uri CURRENT_APN_URI = Uri.parse("content://telephony/carriers/current");
	private static final String TAG = "NetUtil";

	/**
	 * 检查网络情况，保留网络参数
	 * 
	 * @param context
	 * @return
	 */
	public static boolean checkNet(Context context) {
		boolean result = true;
		// 判断当前是否是wifi连接
		boolean wifiConnected = isConnectbyType(context, ConnectivityManager.TYPE_WIFI);
		// 判断当前是否是手机APN连接
		boolean mobileAvailable = isConnectbyType(context, ConnectivityManager.TYPE_MOBILE);

		if (wifiConnected == false && mobileAvailable == false) {
			// 当前无网络连接
			result = false;
		}

		if (wifiConnected == false && mobileAvailable == true) {
			// 获取当前联网apn的代理ip和端口
			setApnProxyInfo(context);
		}

		return result;

	}

	/**
	 * 获取连网形式
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isConnectbyType(Context context, int networkType) {
		boolean result = false;
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getNetworkInfo(networkType);
		if (networkInfo != null && networkInfo.isConnected()) {
			result = true;
		}
		return result;
	}

	/**
	 * 设置apn的代理信息，部分wap还是net，当然为net的时候获取的代理信息
	 * 
	 * @param context
	 */
	public static void setApnProxyInfo(Context context) {
		ContentResolver localContentResolver = context.getContentResolver();
		String ip = "";
		Cursor localCursor = null;
		try {
			localCursor = localContentResolver.query(PREFERRED_APN_URI, null, null, null, null);
			if ((localCursor != null) && (localCursor.getCount() >= 1)) {
				if (localCursor.moveToFirst()) {
					ConfigParams.PROXY_IP = localCursor.getString(localCursor.getColumnIndex("proxy"));
					ConfigParams.PROXY_PORT = localCursor.getInt(localCursor.getColumnIndex("port"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (localCursor != null)
				localCursor.close();
		}
	}

	/****************************************************************************************/
	/**
	 * 获取当前apn的代理ip
	 * 
	 * @param paramContext
	 * @return
	 */
	public static String getProxyIp(Context paramContext) {
		ContentResolver localContentResolver = paramContext.getContentResolver();
		String ip = "";
		Cursor localCursor = null;
		try {
			localCursor = localContentResolver.query(PREFERRED_APN_URI, null, null, null, null);
			if ((localCursor != null) && (localCursor.getCount() >= 1)) {
				if (localCursor.moveToFirst()) {
					ip = localCursor.getString(localCursor.getColumnIndex("proxy"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (localCursor != null)
				localCursor.close();
		}
		return ip;
	}

	/**
	 * 获取当前apn的代理端口
	 * 
	 * @param paramContext
	 * @return
	 */
	public static int getProxyPort(Context paramContext) {
		ContentResolver localContentResolver = paramContext.getContentResolver();
		int port = 0;
		Cursor localCursor = null;
		try {
			localCursor = localContentResolver.query(PREFERRED_APN_URI, null, null, null, null);
			if ((localCursor != null) && (localCursor.getCount() >= 1)) {
				if (localCursor.moveToFirst()) {
					port = localCursor.getInt(localCursor.getColumnIndex("port"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (localCursor != null)
				localCursor.close();
		}
		return port;
	}

	/**
	 * 是否为wap连接
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isWAPConnected(Context context) {

		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (activeNetworkInfo != null && activeNetworkInfo.getExtraInfo().toLowerCase().contains("wap")) {
			return true;
		}
		return false;
	}

	public static void setNowAPN(Context paramContext, int paramInt) {
		ContentResolver localContentResolver = paramContext.getContentResolver();
		ContentValues localContentValues = new ContentValues();
		localContentValues.put("apn_id", paramInt);
		try {
			localContentResolver.update(PREFERRED_APN_URI, localContentValues, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static int openCMWAP(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		Log.i(TAG, "opencmwap");
		return connectivityManager.startUsingNetworkFeature(ConnectivityManager.TYPE_MOBILE, "enableWAP");
	}

	public static boolean checkContiune = true;

	public static void chectNetworkInfo(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		int num = 0;
		while (checkContiune) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			NetworkInfo[] arrayOfNetworkInfo = connectivityManager.getAllNetworkInfo();
			if (arrayOfNetworkInfo != null && arrayOfNetworkInfo.length > 0) {
				for (int index = 0; index < arrayOfNetworkInfo.length; index++) {
					if (arrayOfNetworkInfo[index].getState().compareTo(NetworkInfo.State.CONNECTED) == 0) {

						String type = arrayOfNetworkInfo[index].getTypeName();
						if (StringUtils.isNotBlank(type) && type.contains("wap")) {
							checkContiune = false;
							String reason = arrayOfNetworkInfo[index].getReason();
							String subtypeName = arrayOfNetworkInfo[index].getSubtypeName();
							String typeName = arrayOfNetworkInfo[index].getTypeName();

							connectivityManager.setNetworkPreference(ConnectivityManager.TYPE_MOBILE);// 蜂窝优先，后续的网络操作会走蜂窝通道
							break;
						}
					}
				}
			}
			num++;
			if (num > 30) {

			}
		}
	}

	public static void closeCMWAP(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		int stopUsingNetworkFeature = connectivityManager.stopUsingNetworkFeature(ConnectivityManager.TYPE_MOBILE, "enableWAP");
		connectivityManager.setNetworkPreference(ConnectivityManager.TYPE_WIFI);// wifi优先，后续的网络操作会走wifi通道

		// return
		// connectivityManager.stopUsingNetworkFeature(ConnectivityManager.TYPE_MOBILE,
		// "enableWAP");
	}

	/**
	 * 获取当前的apn信息，若为cmwap在建立连接时需要使用代理信息
	 * 
	 * @param paramContext
	 * @return
	 */
	public static String getNowAPN(Context paramContext) {
		ContentResolver localContentResolver = paramContext.getContentResolver();
		String nowApn = "";
		Cursor localCursor = null;
		try {
			localContentResolver.query(PREFERRED_APN_URI, null, null, null, null);
			if ((localCursor != null) && (localCursor.getCount() >= 1)) {
				if (localCursor.moveToFirst()) {
					nowApn = localCursor.getString(localCursor.getColumnIndex("name"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (localCursor != null)
				localCursor.close();
		}
		return nowApn;

	}
}
