package cn.itcast.lottery.net.protocol.element;

import org.xmlpull.v1.XmlSerializer;

import cn.itcast.lottery.ConstantValue;
import cn.itcast.lottery.net.protocol.Element;
import cn.itcast.lottery.net.protocol.Leaf;

public class CurrentIssueElement implements Element {
	private Leaf lotteryid;// 玩法编号
	private Leaf issues;// 需要获取的最大期数max=100期,当前期为1
	
	/********************封装服务器回复结果************************/
	private String issue;// 当前期（正在销售）
	private String lasttime;// 还剩多少时间当前期完结，针对服务器期的截止时间
	private String lotteryname;//彩种名称

	
	public CurrentIssueElement() {
		lotteryid = new Leaf("lotteryid");
		issues = new Leaf("issues","1");
	}

	public String getTransactiontype() {
		
		return ConstantValue.ELEMENT_CURRENT_ISSUE;
	}

	public void serializer(XmlSerializer serializer) {
		try {
			serializer.startTag(null, "element");
			lotteryid.serializer(serializer);
			issues.serializer(serializer);
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

	public Leaf getIssues() {
		return issues;
	}

	public void setIssues(Leaf issues) {
		this.issues = issues;
	}

	public String getIssue() {
		return issue;
	}

	public void setIssue(String issue) {
		this.issue = issue;
	}

	public String getLasttime() {
		return lasttime;
	}

	public void setLasttime(String lasttime) {
		this.lasttime = lasttime;
	}

	public String getLotteryname() {
		return lotteryname;
	}

	public void setLotteryname(String lotteryname) {
		this.lotteryname = lotteryname;
	}
	
	
	
	
	

}
