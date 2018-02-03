package cn.itcast.lottery.bean;

import java.text.DecimalFormat;

import org.apache.commons.lang3.StringUtils;

import cn.itcast.lottery.ConstantValue;

/**
 * 彩票内容封装
 * 
 * @author Administrator
 * 
 */
public class Ticket {
	private String lotterycode;// 投注号码
	private Integer lotterynumber;// 注数
	private Integer lotteryId;// 彩种标示
	
	private String reds;// 红球内容
	private String blues;// 蓝球内容

	/************************** 处理购物车显示 **************************/
	public static int redShowLen = 7;// 红球显示长度
	public static int blueShowLen = 2;// 蓝球显示长度




	

//	public Integer getAppnumbers() {
//		return appnumbers;
//	}
//
//	public void setAppnumbers(Integer appnumbers) {
//		this.appnumbers = appnumbers;
//	}

	public Integer getLotterynumber() {
		return lotterynumber;
	}

	public void setLotterynumber(Integer lotterynumber) {
		this.lotterynumber = lotterynumber;
	}


	public Integer getLotteryId() {
		return lotteryId;
	}

	public void setLotteryId(Integer lotteryId) {
		this.lotteryId = lotteryId;
	}

	public String showReds() {
		String result = "";
		if (StringUtils.isNotBlank(reds)) {
			if (reds.length() > (redShowLen * 3 - 1)) {
				result = reds.substring(0, (redShowLen * 3 - 4)) + "...";
			} else {
				result = reds;
			}
		}
		return result;
	}

	public String showBlues() {
		String result = "";
		if (StringUtils.isNotBlank(blues)) {
			if (blues.length() > (blueShowLen * 3 - 1)) {
				result = blues.substring(0, (blueShowLen * 3 - 4)) + "...";
			} else {
				result = blues;
			}
		}
		return result;
	}

	/**
	 * 组拼投注号码
	 * 
	 * @return
	 */
	public String getLotterycode() {
		
		if (lotteryId == ConstantValue.SSQ) {
			reds = reds.replaceAll(" ", "");
			blues = blues.replaceAll(" ", "");
			if (StringUtils.isNotBlank(reds) && StringUtils.isNotBlank(blues)) {
				lotterycode = reds + "|" + blues;
			}
		}
		return lotterycode;
	}

	public Ticket(Integer[] redPool, Integer[] bluePool) {
		DecimalFormat format = new DecimalFormat("00");
		if (redPool != null && redPool.length > 0) {
			StringBuffer redBuffer = new StringBuffer();
			for (int i = 0; i < redPool.length; i++) {
				redBuffer.append(" ").append(format.format(redPool[i]));
			}
			reds = redBuffer.substring(1);
		}
		if (bluePool != null && bluePool.length > 0) {
			StringBuffer blueBuffer = new StringBuffer();
			for (int i = 0; i < bluePool.length; i++) {
				blueBuffer.append(" ").append(format.format(bluePool[i]));
			}
			blues = blueBuffer.substring(1);
		}
	}
	
	public Ticket(){}
	
	public String getMoneyDetails() {
		String result = lotterynumber + "注" + lotterynumber*2 + "元";
		if (result.length() > 6) {
			result = result.substring(0, 5) + "...";
		}
		return result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((blues == null) ? 0 : blues.hashCode());
		result = prime * result + ((reds == null) ? 0 : reds.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ticket other = (Ticket) obj;
		if (blues == null) {
			if (other.blues != null)
				return false;
		} else if (!blues.equals(other.blues))
			return false;
		if (reds == null) {
			if (other.reds != null)
				return false;
		} else if (!reds.equals(other.reds))
			return false;
		return true;
	}
	
	
	
	

}
