package cn.itcast.lottery.net.protocol;
/**
 * 服务器回复状态信息
 * @author Administrator
 *
 */
public class Oelement {
	private String errorcode;//错误代码,返回0时代表成功
	private String errormsg;//具体错误信息
	
	public String getErrorcode() {
		return errorcode;
	}
	public void setErrorcode(String errorcode) {
		this.errorcode = errorcode;
	}
	public String getErrormsg() {
		return errormsg;
	}
	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}
}
