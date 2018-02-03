package cn.itcast.lottery.service;

import java.io.InputStream;

import org.apache.commons.codec.digest.DigestUtils;
import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;
import cn.itcast.lottery.ConstantValue;
import cn.itcast.lottery.net.HttpClientAdapter;
import cn.itcast.lottery.net.protocol.Message;
import cn.itcast.lottery.util.DES;

/**
 * 业务父类
 * 
 * @author Administrator
 * 
 */
public abstract class BaseService {
	/**
	 * 抽取通用步骤
	 * 
	 * @param xml
	 * @param callBack
	 * @return
	 */
	public Message getResult(String xml, CallBack callBack) {
		// 3、建立于服务器连接，发送查询请求
		HttpClientAdapter adapter = new HttpClientAdapter();
		// 4、获取服务器返回信息
		InputStream inputStream = adapter.sendPostRequest(ConstantValue.URL_LOTTERY, xml);
		// 5、解析服务器返回信息
		XmlPullParser parser = Xml.newPullParser();

		Message result = new Message();
		try {
			parser.setInput(inputStream, ConstantValue.ENCODING);

			int eventType = parser.getEventType();

			while (eventType != XmlPullParser.END_DOCUMENT) {
				String name = "";
				switch (eventType) {
					case XmlPullParser.START_TAG:
						name = parser.getName();
						// header
						if ("timestamp".equalsIgnoreCase(name)) {
							result.getHeader().getTimestamp().setValue(parser.nextText());
						}
						if ("digest".equalsIgnoreCase(name)) {
							result.getHeader().getDigest().setValue(parser.nextText());
						}
						// body
						if ("body".equalsIgnoreCase(name)) {
							result.getBody().setBodyInfo(parser.nextText());
						}
						break;
				}
				eventType = parser.next();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		// 6、验证MD5信息
		// MD5加密用信息：时间戳+代理密钥+body解密

		DES des = new DES();
		String authcode = "<body>" + des.authcode(result.getBody().getBodyInfo(), "ENCODE", ConstantValue.DES_KEY) + "</body>";

		result.getBody().setBodyInfo(authcode);

		String md5info = result.getHeader().getTimestamp().getValue() + ConstantValue.AGENT_PASSWORD + authcode;

		String md5Hex = DigestUtils.md5Hex(md5info);

		if (md5Hex.equals(result.getHeader().getDigest().getValue())) {
			callBack.parserBody(result);
			return result;
		}
		return null;
	}

	/**
	 * 回调接口
	 * 
	 * @author Administrator
	 * 
	 */
	public interface CallBack {
		/**
		 * 解析协议中特殊部分内容
		 * 
		 * @param message
		 */
		void parserBody(Message message);
	}

}
