package cn.itcast.lottery.net.protocol;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.commons.lang3.StringUtils;
import org.xmlpull.v1.XmlSerializer;

import android.util.Xml;

/**
 * 叶子节点封装
 * @author Administrator
 *
 */
public class Leaf {
	private String name;//标签名
	private String value;//值
	public Leaf(String name) {
		super();
		this.name = name;
	}

	public Leaf(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public void serializer(XmlSerializer serializer) {
		try {
			serializer.startTag(null, name);
			if (StringUtils.isBlank(value))
				value = "";
			serializer.text(value);
			serializer.endTag(null, name);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
