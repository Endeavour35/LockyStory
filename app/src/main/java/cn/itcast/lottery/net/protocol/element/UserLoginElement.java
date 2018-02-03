package cn.itcast.lottery.net.protocol.element;

import org.xmlpull.v1.XmlSerializer;

import cn.itcast.lottery.ConstantValue;
import cn.itcast.lottery.net.protocol.Element;
import cn.itcast.lottery.net.protocol.Leaf;
/**
 * 用户登陆：
 * @author Administrator
 *
 */
public class UserLoginElement implements Element {
	private Leaf actpassword;// 密码

	public UserLoginElement() {
		actpassword = new Leaf("actpassword");
	}

	public String getTransactiontype() {
		return ConstantValue.ELEMENT_USER_LOGIN;
	}

	public Leaf getActpassword() {
		return actpassword;
	}

	public void setActpassword(Leaf actpassword) {
		this.actpassword = actpassword;
	}

	public void serializer(XmlSerializer serializer) {
		try {
			serializer.startTag(null, "element");
			actpassword.serializer(serializer);
			serializer.endTag(null, "element");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
