package cn.itcast.lottery.view;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CacheManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import cn.itcast.lottery.ConstantValue;
import cn.itcast.lottery.R;

public class NewsDetail extends BaseView {
	private WebView webView;
	private ImageView loadingImageView;
	private ImageView errorImageView;

	private AnimationDrawable backgroundAnim;

	public NewsDetail(Context context, Bundle bundle) {
		super(context, bundle);
	}

	@Override
	public Integer getId() {
		return ConstantValue.VIEW_NEWS_DETAIL;
	}

	@Override
	protected void init() {
		container = (ViewGroup) inflater.inflate(R.layout.il_news_detail, null);
		loadingImageView = (ImageView) container.findViewById(R.id.ii_news_detail_loading);
		errorImageView = (ImageView) container.findViewById(R.id.ii_news_detail_error);
		webView = (WebView) container.findViewById(R.id.ii_news_detail_content);

		backgroundAnim = (AnimationDrawable) loadingImageView.getBackground();
	}

	@Override
	public void onResume() {
		// 控件显示回复到初始状态
		webView.setVisibility(View.VISIBLE);
		errorImageView.setVisibility(View.INVISIBLE);

		webView.setWebViewClient(new MyWebViewClient());
		if (checkNetWork()) {
			 webView.loadUrl(bundle.getString("link"));
//			webView.loadUrl(ConstantValue.URL_NEWS_ITEM);
		}
	}

	@Override
	public void onPause() {
		// 将webView中信息清空（跳转到about:blank界面）
		webView.loadDataWithBaseURL(null, null, null, null, null);
		// 清空webview缓存内容
		File file = CacheManager.getCacheFileBaseDir();

		if (file != null && file.exists() && file.isDirectory()) {
			for (File item : file.listFiles()) {
				item.delete();
			}
			file.delete();
		}

		context.deleteDatabase("webview.db");
		context.deleteDatabase("webviewCache.db");
	}

	@Override
	protected void setListener() {

	}

	/**
	 * 待处理问题： 1、链接超时 2、缓存清理 3、错误状态码处理
	 * 
	 * @author Administrator
	 * 
	 */
	private class MyWebViewClient extends WebViewClient {
		private static final int ON_PAGE_STARTED = 0;
		private static final int ON_PAGE_FINISHED = 1;
		private static final int TIME_OUT = 2;
		private static final int ERROR_CODE = 3;
		private static final int ERROR_CODE_1 = 4;

		private int timeout = 5000;

		private Handler handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
					case ON_PAGE_STARTED:
						loadingImageView.setVisibility(View.VISIBLE);
						backgroundAnim.start();
						break;
					case ON_PAGE_FINISHED:
						loadingImageView.setVisibility(View.INVISIBLE);
						backgroundAnim.stop();
						break;
					case TIME_OUT:
						/*
						 * 超时后,首先判断页面加载进度,超时并且进度小于100,就执行超时后的动作
						 */
						if (webView.getProgress() < 100) {
							showError();
						}
						break;
					case ERROR_CODE:
						// 用于控制当链接服务器后获取的状态码不为200时
						String url = msg.obj.toString();
						try {
							HttpURLConnection httpconn = (HttpURLConnection) new URL(url).openConnection();
							httpconn.setConnectTimeout(2000);
							// 查看链接是否能够访问成功
							if (httpconn.getResponseCode() != HttpURLConnection.HTTP_OK) {
								showError();
							}
						} catch (Exception e) {
							e.printStackTrace();
							showError();
						}
						break;
					case ERROR_CODE_1:
						new HttpTask<Boolean>() {

							@Override
							protected Boolean doInBackground(String... params) {

								try {
									HttpURLConnection httpconn = (HttpURLConnection) new URL(params[0]).openConnection();
									httpconn.setConnectTimeout(2000);
									// 查看链接是否能够访问成功
									if (httpconn.getResponseCode() != HttpURLConnection.HTTP_OK) {
										return false;
									}
								} catch (Exception e) {
									e.printStackTrace();
									return false;
								}
								return false;
							}

							@Override
							protected void onPostExecute(Boolean result) {
								if (result == false) {
									showError();
								}
								super.onPostExecute(result);
							}

						}.execute(msg.obj.toString());
						break;
				}
				super.handleMessage(msg);
			}

		};

		@Override
		public void onPageFinished(WebView view, String url) {
			handler.sendEmptyMessage(ON_PAGE_FINISHED);
			super.onPageFinished(view, url);
		}

		@Override
		public void onPageStarted(WebView view, final String url, Bitmap favicon) {
			handler.sendEmptyMessage(ON_PAGE_STARTED);
			handler.sendEmptyMessageDelayed(TIME_OUT, timeout);

			Message msg = new Message();
//			msg.what = ERROR_CODE;
			 msg.what = ERROR_CODE_1;
			msg.obj = url;
			handler.sendMessage(msg);

			super.onPageStarted(view, url, favicon);
		}

		private void showError() {
			webView.setVisibility(View.INVISIBLE);
			loadingImageView.setVisibility(View.GONE);
			backgroundAnim.stop();
			errorImageView.setVisibility(View.VISIBLE);
			errorImageView.setImageResource(R.drawable.id_news_details_web_error);
		}

		@Override
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			webView.setVisibility(View.INVISIBLE);
			errorImageView.setVisibility(View.VISIBLE);
			errorImageView.setImageResource(R.drawable.id_news_details_web_error);
			super.onReceivedError(view, errorCode, description, failingUrl);
		}

	}

}
