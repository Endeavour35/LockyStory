package cn.itcast.lottery.service.impl;

import java.io.InputStream;
import java.io.StringReader;

import org.apache.commons.codec.digest.DigestUtils;
import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;
import cn.itcast.lottery.ConstantValue;
import cn.itcast.lottery.net.HttpClientAdapter;
import cn.itcast.lottery.net.protocol.Message;
import cn.itcast.lottery.net.protocol.element.CurrentIssueElement;
import cn.itcast.lottery.service.BaseService;
import cn.itcast.lottery.service.CommonService;
import cn.itcast.lottery.util.DES;

public class CommonServiceImpl extends BaseService implements CommonService {

	public Message getCurrentIssueInfo1(String lotteryId) {
		// 1、创建查询用Element，传递查询用参数
		CurrentIssueElement element = new CurrentIssueElement();
		element.getLotteryid().setValue(lotteryId);
		// 2、生成xml文件
		Message message = new Message();
		String xml = message.getXml(element);
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
		//将解密后的信息保存在body的bodyInfo中
		result.getBody().setBodyInfo(authcode);
		//生成MD5信息
		String md5info = result.getHeader().getTimestamp().getValue() + ConstantValue.AGENT_PASSWORD + authcode;
		String md5Hex = DigestUtils.md5Hex(md5info);
		//与服务器的MD5信息进行比对
		if (md5Hex.equals(result.getHeader().getDigest().getValue())) {
			// 7、解析body部分信息
			// 针对具体功能处理具体业务逻辑
			XmlPullParser parserBody = Xml.newPullParser();
			try {
				String body = result.getBody().getBodyInfo();
				parserBody.setInput(new StringReader(body));
				int eventType = parserBody.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					String name = "";
					switch (eventType) {
						case XmlPullParser.START_TAG:
							name = parserBody.getName();
							if ("errorcode".equalsIgnoreCase(name)) {
								result.getBody().getOelement().setErrorcode(parserBody.nextText());
							}
							if ("errormsg".equalsIgnoreCase(name)) {
								result.getBody().getOelement().setErrormsg(parserBody.nextText());
							}
							
							if ("element".equalsIgnoreCase(name)) {
								element = new CurrentIssueElement();
							}
							if ("issue".equalsIgnoreCase(name)) {
								element.setIssue(parserBody.nextText());
							}
							if ("lasttime".equalsIgnoreCase(name)) {
								element.setLasttime(parserBody.nextText());
							}
							if ("lotteryname".equalsIgnoreCase(name)) {
								element.setLotteryname(parserBody.nextText());
							}
							if ("lotteryid".equalsIgnoreCase(name)) {
								element.getLotteryid().setValue(parserBody.nextText());
							}
							
							
							break;
						case XmlPullParser.END_TAG:
							name = parserBody.getName();
							if ("element".equalsIgnoreCase(name)) {
								message.getBody().getElements().add(element);
							}
							break;
					}
					eventType = parserBody.next();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			return result;
		}

		return null;
	}

	public Message getCurrentIssueInfo(String lotteryId) {
		// 1、创建查询用Element，传递查询用参数
		CurrentIssueElement element = new CurrentIssueElement();
		element.getLotteryid().setValue(lotteryId);
		// 2、生成xml文件
		Message message = new Message();
		String xml = message.getXml(element);

		Message result = getResult(xml, new CallBack() {

			public void parserBody(Message message) {
				// 7、解析body部分信息
				// 针对具体功能处理具体业务逻辑
				XmlPullParser parserBody = Xml.newPullParser();
				try {
					String body = message.getBody().getBodyInfo();
					parserBody.setInput(new StringReader(body));
					int eventType = parserBody.getEventType();
					CurrentIssueElement element = null;
					while (eventType != XmlPullParser.END_DOCUMENT) {
						String name = "";
						switch (eventType) {
							case XmlPullParser.START_TAG:
								name = parserBody.getName();
								if ("errorcode".equalsIgnoreCase(name)) {
									message.getBody().getOelement().setErrorcode(parserBody.nextText());
								}
								if ("errormsg".equalsIgnoreCase(name)) {
									message.getBody().getOelement().setErrormsg(parserBody.nextText());
								}
								if ("element".equalsIgnoreCase(name)) {
									element = new CurrentIssueElement();
								}
								if ("issue".equalsIgnoreCase(name)) {
									element.setIssue(parserBody.nextText());
								}
								if ("lasttime".equalsIgnoreCase(name)) {
									element.setLasttime(parserBody.nextText());
								}
								if ("lotteryname".equalsIgnoreCase(name)) {
									element.setLotteryname(parserBody.nextText());
								}
								if ("lotteryid".equalsIgnoreCase(name)) {
									element.getLotteryid().setValue(parserBody.nextText());
								}
								
								break;
							case XmlPullParser.END_TAG:
								name = parserBody.getName();
								if ("element".equalsIgnoreCase(name)) {
									message.getBody().getElements().add(element);
								}
								break;
						}
						eventType = parserBody.next();
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
		return result;
	}

}
