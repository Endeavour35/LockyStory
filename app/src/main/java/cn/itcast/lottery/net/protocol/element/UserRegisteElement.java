package cn.itcast.lottery.net.protocol.element;

import org.xmlpull.v1.XmlSerializer;

import cn.itcast.lottery.ConstantValue;
import cn.itcast.lottery.net.protocol.Element;
import cn.itcast.lottery.net.protocol.Leaf;
/**
 * 用户注册
 * @author Administrator
 *
 */
public class UserRegisteElement implements Element {
	private Leaf phone = new Leaf("phone");//联系电话
	private Leaf actpassword = new Leaf("actpassword");//账户安全密码
	
	private String actvalue;// 用户账户余额

	public String getActvalue() {
		return actvalue;
	}

	public void setActvalue(String actvalue) {
		this.actvalue = actvalue;
	}

	public Leaf getPhone() {
		return phone;
	}

	public void setPhone(Leaf phone) {
		this.phone = phone;
	}

	public Leaf getActpassword() {
		return actpassword;
	}

	public void setActpassword(Leaf actpassword) {
		this.actpassword = actpassword;
	}

	public String getTransactiontype() {
		return ConstantValue.ELEMENT_USER_REGISTE;
	}

	public void serializer(XmlSerializer serializer) {
		try {
			serializer.startTag(null, "element");
			phone.serializer(serializer);
			actpassword.serializer(serializer);
			serializer.endTag(null, "element");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
