package cn.itcast.lottery.net.protocol;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.xmlpull.v1.XmlSerializer;

import android.util.Xml;
import cn.itcast.lottery.ConstantValue;
import cn.itcast.lottery.util.DES;
/**
 * 消息体部分封装
 * @author Administrator
 *
 */
public class Body {
	private List<Element> elements = new ArrayList<Element>();// 请求
	private Oelement oelement = new Oelement();// 处理回复
	
	private String bodyInfo;// body信息加密及解析存储

	public Oelement getOelement() {
		return oelement;
	}

	public void setOelement(Oelement oelement) {
		this.oelement = oelement;
	}

	public String getBodyInfo() {
		return bodyInfo;
	}

	public void setBodyInfo(String bodyInfo) {
		this.bodyInfo = bodyInfo;
	}

	public List<Element> getElements() {
		return elements;
	}
	
	
	public void serializer(XmlSerializer serializer) {
		try {
			serializer.startTag(null, "body");
			serializer.startTag(null, "elements");
			
			for (Element element : elements) {
				element.serializer(serializer);
			}
			
			serializer.endTag(null, "elements");
			serializer.endTag(null, "body");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取body部分未经过加密的信息
	 * @return
	 */
	public String getBody()
	{
		XmlSerializer temp = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		try {
			temp.setOutput(writer);
			serializer(temp);
			temp.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return writer.toString();
	}
	
	/**
	 * 获取body部分des加密信息
	 * @param body
	 * @return
	 */
	public String getDESBody()
	{
		String body=getBody();
		body = StringUtils.substringBetween(body, "<body>","</body>");
		String authcode = new DES().authcode(body, "DECODE", ConstantValue.DES_KEY);
		return authcode;
	}
}
