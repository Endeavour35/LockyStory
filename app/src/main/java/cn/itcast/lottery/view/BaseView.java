package cn.itcast.lottery.view;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.itcast.lottery.R;
import cn.itcast.lottery.net.protocol.Message;
import cn.itcast.lottery.util.NetUtil;
import cn.itcast.lottery.util.PromptManager;

/**
 * 中间容器父类
 * 
 * @author Administrator
 * 
 */
public abstract class BaseView implements View.OnClickListener {
	protected static Handler handler;// 完善用户提示

	protected Context context;// 传递上下文对象，用户界面配置文件加载
	protected LayoutInflater inflater;// 加载xml文件的工具类

	protected ViewGroup container;// 中间容器

	protected Bundle bundle;

	public BaseView(Context context, Bundle bundle) {
		this.context = context;
		if (bundle != null)
			this.bundle = bundle;

		inflater = LayoutInflater.from(context);
		init();
		// 设置监听
		setListener();
	}

	/**
	 * 界面管理工具切换界面时使用
	 */
	public View getView() {
		return container;
	}

	/**
	 * 界面管理工具切换界面时调整标题和底部导航使用
	 */
	public abstract Integer getId();

	/**
	 * 设置Handler用于处理友好提示
	 * 
	 * @param handler
	 */
	public static void setHandler(Handler handler) {
		BaseView.handler = handler;
	}

	/**
	 * 子类初始化使用
	 */
	protected abstract void init();

	/**
	 * 设置监听
	 */
	protected abstract void setListener();

	/**
	 * 离开当前页面执行
	 */
	public void onPause() {
		// TODO Auto-generated method stub

	}

	/**
	 * 进入当前页面执行
	 */
	public void onResume() {

	}

	public Bundle getBundle() {
		return bundle;
	}

	public void setBundle(Bundle bundle) {
		this.bundle = bundle;
	}

	/**
	 * 判断当前网络状况
	 * 
	 * @return
	 */
	protected boolean checkNetWork() {
		boolean result = true;
		boolean checkNet = NetUtil.checkNet(context);
		if (!checkNet) {
			PromptManager.showToast(context, R.string.is_error_no_network);
			result = false;
		}

		return result;
	}

	@Override
	public void onClick(View v) {
	}
	
	public abstract class HttpTask<T> extends AsyncTask<String, Integer, T> 
	{
		/**
		 * 任务执行的代理（添加网络状态判断）
		 * 
		 * @param params
		 * @return
		 */
		public AsyncTask<String, Integer, T> executeProxy(String... params) {
			if (checkNetWork()) {
				return execute(params);
			} else {
				return null;
			}
		}
	}

//	protected class HttpTask<T> extends AsyncTask<String, Integer, T> {
		/**
		 * 任务执行的代理（添加网络状态判断）
		 * 
		 * @param params
		 * @return
		 */
//		public AsyncTask<String, Integer, T> executeProxy(String... params) {
//			if (checkNetWork()) {
//				return execute(params);
//			} else {
//				return null;
//			}
//		}
//
//		/**
//		 * 后台运行
//		 */
//		@Override
//		protected T doInBackground(String... params) {
//			return BaseView.this.doInBackground(params);
//		}
//
//		/**
//		 * 取消任务
//		 */
//		@Override
//		protected void onCancelled() {
//			BaseView.this.onCancelled();
//			super.onCancelled();
//		}
//
//		/**
//		 * 任务执行完毕（主线程）
//		 */
//		@Override
//		protected void onPostExecute(T result) {
//			BaseView.this.onPostExecute(result);
//			super.onPostExecute(result);
//		}
//
//		/**
//		 * 任务执行前（主线程）
//		 */
//		@Override
//		protected void onPreExecute() {
//			BaseView.this.onPreExecute();
//			super.onPreExecute();
//		}
//
//		/**
//		 * 任务执行过程中（主线程）
//		 */
//		@Override
//		protected void onProgressUpdate(Integer... values) {
//			BaseView.this.onProgressUpdate(values);
//			super.onProgressUpdate(values);
//		}
//
//	}

//	protected <T> T doInBackground(String... params) {
//		return null;
//	}
//
//	protected void onProgressUpdate(Integer[] values) {
//	}
//
//	protected void onPreExecute() {
//	}
//
//	protected void onCancelled() {
//	}
//
//	protected <T> void onPostExecute(T result) {
//	}

}
