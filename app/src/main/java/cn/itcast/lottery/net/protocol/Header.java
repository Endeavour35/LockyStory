package cn.itcast.lottery.net.protocol;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.apache.commons.codec.digest.DigestUtils;
import org.xmlpull.v1.XmlSerializer;

import android.util.Log;
import cn.itcast.lottery.ConstantValue;


/**
 * 消息头部分封装
 * @author Administrator
 *
 */
public class Header {
	private Leaf messengerid;// 标示
	private Leaf timestamp;// 时间戳
	private Leaf digest;// MD5加密
	private Leaf username;// 用户名
	
	private Leaf transactiontype;// 类型
	private Leaf agenterid;// 代理商标示
	private Leaf source;// 信息来源
	private Leaf compress;// 是否des加密
	
	public Header() {
		this.transactiontype = new Leaf("transactiontype");
		this.agenterid = new Leaf("agenterid", ConstantValue.AGENTER_ID);
		this.source = new Leaf("source", ConstantValue.SOURCE);
		this.compress = new Leaf("compress", ConstantValue.COMPRESS);
		
		this.messengerid = new Leaf("messengerid");
		this.timestamp = new Leaf("timestamp");
		this.username = new Leaf("username");

		this.digest = new Leaf("digest");
	}

	public Leaf getMessengerid() {
		return messengerid;
	}

	public void setMessengerid(Leaf messengerid) {
		this.messengerid = messengerid;
	}

	public Leaf getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Leaf timestamp) {
		this.timestamp = timestamp;
	}

	public Leaf getDigest() {
		return digest;
	}

	public void setDigest(Leaf digest) {
		this.digest = digest;
	}

	public Leaf getUsername() {
		return username;
	}

	public void setUsername(Leaf username) {
		this.username = username;
	}

	public Leaf getTransactiontype() {
		return transactiontype;
	}

	public void setTransactiontype(Leaf transactiontype) {
		this.transactiontype = transactiontype;
	}

	public Leaf getAgenterid() {
		return agenterid;
	}

	public void setAgenterid(Leaf agenterid) {
		this.agenterid = agenterid;
	}

	

	public Leaf getSource() {
		return source;
	}

	public void setSource(Leaf source) {
		this.source = source;
	}

	public Leaf getCompress() {
		return compress;
	}

	public void setCompress(Leaf compress) {
		this.compress = compress;
	}
	
	
	public void serializer(XmlSerializer serializer, String body) {

		String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		String num = new DecimalFormat("000000").format(new Random().nextInt(999999) + 1);

		this.messengerid.setValue(time + num);
		this.timestamp.setValue(time);

		// 加密用信息
		Log.i("XmlTest", body);
		String md5 = time + ConstantValue.AGENT_PASSWORD + body;
		String md5Hex = DigestUtils.md5Hex(md5);
		this.digest.setValue(md5Hex);

		try {
			serializer.startTag(null, "header");

			this.messengerid.serializer(serializer);
			this.timestamp.serializer(serializer);
			this.digest.serializer(serializer);
			this.agenterid.serializer(serializer);
			this.compress.serializer(serializer);
			this.source.serializer(serializer);
			
			this.username.serializer(serializer);
			this.transactiontype.serializer(serializer);
			
			serializer.endTag(null, "header");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
