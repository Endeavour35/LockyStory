package cn.itcast.lottery.net.protocol.element;

import org.xmlpull.v1.XmlSerializer;

import cn.itcast.lottery.ConstantValue;
import cn.itcast.lottery.net.protocol.Element;
import cn.itcast.lottery.net.protocol.Leaf;

public class BettingElement implements Element {

	private Leaf lotteryid = new Leaf("lotteryid");// 玩法编号
	private Leaf issue = new Leaf("issue");// 期号（当前销售期）
	private Leaf lotteryvalue = new Leaf("lotteryvalue");// 方案金额，以分为单位
	private Leaf lotterynumber = new Leaf("lotterynumber");// 注数
	private Leaf appnumbers = new Leaf("appnumbers");// 倍数
	private Leaf issuesnumbers = new Leaf("issuesnumbers");// 追期
	private Leaf lotterycode = new Leaf("lotterycode");// 投注号码，注与注之间^分割
	private Leaf issueflag = new Leaf("issueflag");// 是否多期追号 0否，1多期
	private Leaf bonusstop = new Leaf("bonusstop", "1");// 中奖后是否停止：0不停，1停

	/****************** 投注回复结果 *******************/
	private String serialid;// 账单流水号
	private String tradevalue;// 实际扣费金额
	private String actvalue;// 用户账户余额(用户注册共用)

	public String getSerialid() {
		return serialid;
	}

	public void setSerialid(String serialid) {
		this.serialid = serialid;
	}

	public String getTradevalue() {
		return tradevalue;
	}

	public void setTradevalue(String tradevalue) {
		this.tradevalue = tradevalue;
	}

	public String getActvalue() {
		return actvalue;
	}

	public void setActvalue(String actvalue) {
		this.actvalue = actvalue;
	}

	public String getTransactiontype() {
		return ConstantValue.ELEMENT_USER_BETTING;
	}

	public void serializer(XmlSerializer serializer) {
		try {
			serializer.startTag(null, "element");

			lotteryid.serializer(serializer);
			issue.serializer(serializer);
			lotteryvalue.serializer(serializer);
			lotterynumber.serializer(serializer);
			appnumbers.serializer(serializer);
			issuesnumbers.serializer(serializer);
			lotterycode.serializer(serializer);
			issueflag.serializer(serializer);
			bonusstop.serializer(serializer);

			serializer.endTag(null, "element");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Leaf getLotteryid() {
		return lotteryid;
	}

	public void setLotteryid(Leaf lotteryid) {
		this.lotteryid = lotteryid;
	}

	public Leaf getIssue() {
		return issue;
	}

	public void setIssue(Leaf issue) {
		this.issue = issue;
	}

	public Leaf getLotteryvalue() {
		return lotteryvalue;
	}

	public void setLotteryvalue(Leaf lotteryvalu) {
		this.lotteryvalue = lotteryvalu;
	}

	public Leaf getLotterynumber() {
		return lotterynumber;
	}

	public void setLotterynumber(Leaf lotterynumber) {
		this.lotterynumber = lotterynumber;
	}

	public Leaf getAppnumbers() {
		return appnumbers;
	}

	public void setAppnumbers(Leaf appnumbers) {
		this.appnumbers = appnumbers;
	}

	public Leaf getIssuesnumbers() {
		return issuesnumbers;
	}

	public void setIssuesnumbers(Leaf issuesnumbers) {
		this.issuesnumbers = issuesnumbers;
	}

	public Leaf getLotterycode() {
		return lotterycode;
	}

	public void setLotterycode(Leaf lotterycode) {
		this.lotterycode = lotterycode;
	}

	public Leaf getIssueflag() {
		return issueflag;
	}

	public void setIssueflag(Leaf issueflag) {
		this.issueflag = issueflag;
	}

}
