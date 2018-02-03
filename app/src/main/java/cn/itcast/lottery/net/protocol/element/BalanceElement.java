package cn.itcast.lottery.net.protocol.element;

import org.xmlpull.v1.XmlSerializer;

import cn.itcast.lottery.ConstantValue;
import cn.itcast.lottery.net.protocol.Element;
/**
 * 用户余额查询
 * @author Administrator
 *
 */
public class BalanceElement implements Element {
	/******************余额查询回复*******************/
	private String investvalues;//可投注金额
	private String cashvalues;//可提现金额
	

	public String getInvestvalues() {
		return investvalues;
	}

	public void setInvestvalues(String investvalues) {
		this.investvalues = investvalues;
	}

	public String getCashvalues() {
		return cashvalues;
	}

	public void setCashvalues(String cashvalues) {
		this.cashvalues = cashvalues;
	}

	public String getTransactiontype() {
		return ConstantValue.ELEMENT_USER_BALANCE;
	}

	public void serializer(XmlSerializer serializer) {

	}

}
