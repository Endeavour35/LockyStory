package cn.itcast.lottery.net.protocol;

import org.xmlpull.v1.XmlSerializer;
/**
 * 协议中请求接口
 * @author Administrator
 *
 */
public interface Element {
	// 协议中包含35个请求
	// 每个请求对应一个请求标示
	/**
	 * 序列化
	 * 
	 * @param serializer
	 */
	public void serializer(XmlSerializer serializer);

	/**
	 * 获取标示
	 * 
	 * @return
	 */
	public String getTransactiontype();
}
