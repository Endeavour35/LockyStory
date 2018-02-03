package cn.itcast.lottery.util;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;
import cn.itcast.lottery.R;

/**
 * 统一管理显示内容
 * 
 * @author Administrator
 * 
 */
public class PromptManager {

	/**
	 * 控制Toast显示
	 */
	private static boolean isShow = true;
	
	
	/**
	 * 设置简单的进度框
	 * 
	 * @param progressDialog
	 *            需要展示的进度框
	 * @param msg
	 *            需要展示内容
	 */
	public static void showSimpleProgressDialog(ProgressDialog progressDialog, String msg) {
		if (progressDialog != null && !progressDialog.isShowing()) {
			progressDialog.setIcon(R.drawable.icon);
			progressDialog.setTitle(R.string.app_name);

			progressDialog.setMessage(msg);
			// progressDialog.setCancelable(false);
			progressDialog.show();
		}
	}

	/**
	 * 关闭当前进度条
	 * 
	 * @param progressDialog
	 */
	public static void closeProgressDialog(ProgressDialog progressDialog) {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}
	
	/**
	 * 显示退出对话框
	 * 
	 * @param context
	 */
	public static void showExitDialog(Context context) {
		new AlertDialog.Builder(context).setIcon(R.drawable.icon).setTitle(R.string.app_name).setMessage(R.string.is_exit).setPositiveButton(
				context.getString(R.string.is_positive), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// 对缓存数据进行管理
						android.os.Process.killProcess(android.os.Process.myPid());
					}
				}).setNegativeButton(context.getString(R.string.is_negative), null).show();

	}

	/**
	 * 显示错误提示框
	 * 
	 * @param context
	 * @param msg
	 */
	public static void showErrorDialog(Context context, String msg) {
		new AlertDialog.Builder(context).setIcon(R.drawable.icon).setTitle(R.string.app_name).setMessage(msg).setNegativeButton(
				context.getString(R.string.is_positive), null).show();
	}

	/**
	 * 显示错误提示框
	 * 
	 * @param context
	 * @param msg
	 */
	public static void showErrorDialog(Context context, String title, String msg) {
		new AlertDialog.Builder(context).setIcon(R.drawable.icon).setTitle(title).setMessage(msg).setNegativeButton(
				context.getString(R.string.is_positive), null).show();
	}
	

	public static void showToast(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}

	public static void showToast(Context context, int msgResId) {
		Toast.makeText(context, msgResId, Toast.LENGTH_LONG).show();
	}

	/**
	 * 测试用
	 * 
	 * @param context
	 * @param msg
	 */
	public static void showToastTest(Context context, String msg) {
		if (isShow) {
			Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
		}
	}

	



}
