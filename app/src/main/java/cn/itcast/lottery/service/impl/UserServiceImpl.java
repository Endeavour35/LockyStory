package cn.itcast.lottery.service.impl;

import java.io.StringReader;

import org.xmlpull.v1.XmlPullParser;

import android.util.Log;
import android.util.Xml;
import cn.itcast.lottery.bean.ShoppingCart;
import cn.itcast.lottery.bean.User;
import cn.itcast.lottery.net.protocol.Message;
import cn.itcast.lottery.net.protocol.element.BalanceElement;
import cn.itcast.lottery.net.protocol.element.BettingElement;
import cn.itcast.lottery.net.protocol.element.UserLoginElement;
import cn.itcast.lottery.net.protocol.element.UserRegisteElement;
import cn.itcast.lottery.service.BaseService;
import cn.itcast.lottery.service.UserService;
import cn.itcast.lottery.service.BaseService.CallBack;

public class UserServiceImpl extends BaseService implements UserService {

	public Message balance(User user) {
		BalanceElement element=new BalanceElement();
		Message message = new Message();
		message.getHeader().getUsername().setValue(user.getUsername());
		String xml = message.getXml(element);
		Message result = getResult(xml,new CallBack() {
			
			public void parserBody(Message message) {
				XmlPullParser parserBody = Xml.newPullParser();
				try {
					String body = message.getBody().getBodyInfo();
					parserBody.setInput(new StringReader(body));
					int eventType = parserBody.getEventType();
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
								if("investvalues".equalsIgnoreCase(name))
								{
									BalanceElement element=new BalanceElement();
									element.setInvestvalues(parserBody.nextText());
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

	public Message betting(User user) {
		BettingElement element=new BettingElement();
		
		element.getLotteryid().setValue(ShoppingCart.getInstance().getLotteryid());
		element.getIssue().setValue(ShoppingCart.getInstance().getIssue());
		element.getLotteryvalue().setValue(ShoppingCart.getInstance().getLotteryvalue().toString());
		element.getLotterynumber().setValue(ShoppingCart.getInstance().getLotterynumber().toString());
		element.getAppnumbers().setValue(ShoppingCart.getInstance().getAppnumbers().toString());
		element.getIssuesnumbers().setValue(ShoppingCart.getInstance().getIssuesnumbers().toString());
		element.getLotterycode().setValue(ShoppingCart.getInstance().getLotteryCode());
		element.getIssueflag().setValue("0");
		
		Message message = new Message();
		message.getHeader().getUsername().setValue(user.getUsername());
		String xml = message.getXml(element);
		
		Log.i("UserServiceImpl", xml);
		
		Message result = getResult(xml, new CallBack() {
			
			public void parserBody(Message message) {
				XmlPullParser parserBody = Xml.newPullParser();
				try {
					String body = message.getBody().getBodyInfo();
					parserBody.setInput(new StringReader(body));
					int eventType = parserBody.getEventType();
					BettingElement element=null;
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
									element = new BettingElement();
								}
								if("tradevalue".equalsIgnoreCase(name))
								{
									element.setTradevalue(parserBody.nextText());
								}
								if("actvalue".equalsIgnoreCase(name))
								{
									element.setActvalue(parserBody.nextText());
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

	public Message login(User user) {
		UserLoginElement element = new UserLoginElement();
		element.getActpassword().setValue(user.getActpassword());

		Message message = new Message();
		message.getHeader().getUsername().setValue(user.getUsername());
		String xml = message.getXml(element);

		Message result = getResult(xml, new CallBack() {
			
			public void parserBody(Message message) {
				XmlPullParser parserBody = Xml.newPullParser();
				try {
					String body = message.getBody().getBodyInfo();
					parserBody.setInput(new StringReader(body));
					int eventType = parserBody.getEventType();
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

	public Message registe(User user) {
		UserRegisteElement element = new UserRegisteElement();
		element.getActpassword().setValue(user.getActpassword());

		Message message = new Message();
		message.getHeader().getUsername().setValue(user.getUsername());
		String xml = message.getXml(element);

		Message result = getResult(xml, new CallBack() {

			public void parserBody(Message message) {
				XmlPullParser parserBody = Xml.newPullParser();
				try {
					String body = message.getBody().getBodyInfo();
					parserBody.setInput(new StringReader(body));
					int eventType = parserBody.getEventType();
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
								if ("actvalue".equalsIgnoreCase(name)) {
									UserRegisteElement element = new UserRegisteElement();
									element.setActvalue(parserBody.nextText());
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
