package cn.itcast.lottery.net.protocol;

import java.io.StringWriter;

import org.xmlpull.v1.XmlSerializer;

import android.util.Xml;
import cn.itcast.lottery.ConstantValue;

/**
 * 消息封装
 * 
 * @author Administrator
 * 
 */
public class Message {
	private Header header;// 消息头
	private Body body;// 消息体

	public Message() {
		header = new Header();
		body = new Body();
	}

	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}

	private void serializer(XmlSerializer serializer) {
		try {
			serializer.startTag(null, "message");
			serializer.attribute(null, "version", "1.0");
			header.serializer(serializer, body.getBody());
			serializer.startTag(null, "body");
			serializer.text(body.getDESBody());
			serializer.endTag(null, "body");
			serializer.endTag(null, "message");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getXml(Element element) {
		body.getElements().add(element);
		header.getTransactiontype().setValue(element.getTransactiontype());
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		try {
			serializer.setOutput(writer);
			serializer.startDocument(ConstantValue.ENCODING, null);
			serializer(serializer);
			serializer.endDocument();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return writer.toString();
	}

}
