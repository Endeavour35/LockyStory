package cn.itcast.lottery.net;

import java.io.InputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;
import cn.itcast.lottery.ConfigParams;
import cn.itcast.lottery.ConstantValue;
import cn.itcast.lottery.util.LogUtil;

public class HttpClientAdapter {
	// 1、设置代理信息
	// 2、明确Post、Get（URL）
	// 3、使用到的是Post，设置发送内容
	// 4、HttpClient 发送请求
	// 5、服务器会送状态码判断（200）
	// 6、把服务器回送的输入流返回给调用者

	private HttpClient httpClient;
	private HttpPost httpPost;
	private HttpGet httpGet;

	private HttpResponse httpResponse;

	public HttpClientAdapter() {
		httpClient = new DefaultHttpClient();
		// 1、设置代理
		setProxy();
	}

	/**
	 * 发送一个POST请求
	 * 
	 * @param url
	 * @param xml
	 */
	public InputStream sendPostRequest(String url, String xml) {
		try {
			// 2、明确（URL）
			httpPost = new HttpPost(url);
			// 3、使用到的是Post，设置发送内容
			StringEntity entity = new StringEntity(xml, ConstantValue.ENCODING);
			httpPost.setEntity(entity);
			// 4、HttpClient 发送请求
			httpResponse = httpClient.execute(httpPost);
			// 5、服务器会送状态码判断（200）
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				// 6、把服务器回送的输入流返回给调用者
				return httpResponse.getEntity().getContent();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 发送一个get请求
	 * 
	 * @param url
	 * @return
	 */
	public InputStream sendGetRequest(String url) {

		try {
			// 1、明确URL
			httpGet = new HttpGet(url);
			// 2、HttpClient 发送请求
			httpClient = new DefaultHttpClient();
			httpResponse = httpClient.execute(httpGet);
			// 3、服务器会送状态码判断（200）
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				// 4、把服务器回送的输入流返回给调用者
				return httpResponse.getEntity().getContent();
			}
		} catch (Exception e) {
			LogUtil.info(HttpClientAdapter.class, Log.getStackTraceString(e));
		}
		return null;
	}

	/**
	 * 设置代理信息
	 */
	private void setProxy() {
		if (StringUtils.isNotBlank(ConfigParams.PROXY_IP)) {
			HttpHost httpHost = new HttpHost(ConfigParams.PROXY_IP, ConfigParams.PROXY_PORT);
			httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, httpHost);
		}
	}

}
