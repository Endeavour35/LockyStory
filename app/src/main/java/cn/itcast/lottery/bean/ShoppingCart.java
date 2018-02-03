package cn.itcast.lottery.bean;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.lang3.StringUtils;

import android.util.Log;
import cn.itcast.lottery.ConstantValue;
import cn.itcast.lottery.util.CalculatorUtils;

/**
 * 购物车
 * 
 * @author Administrator
 * 
 */
public class ShoppingCart {
	private static final String TAG = "ShoppingCart";
	
	/**************************获取shoppingcart的实例 *************/
	private static ShoppingCart instance;

	private ShoppingCart() {
	}

	public static ShoppingCart getInstance() {
		if (instance == null) {
			instance = new ShoppingCart();
		}
		return instance;
	}
	
	public static final int BASEMONEY = 2;// 单注彩票的基础价格
	private String lotteryid;// 玩法编号
	private String issue;// 期号（当前销售期）
	private Integer lotteryvalue;// 方案金额，以分为单位

	private CopyOnWriteArrayList<Ticket> tickets = new CopyOnWriteArrayList<Ticket>();//存储票的集合
	private Integer lotterynumber = 1;// 注数
	
	private Integer issueflag;// 是否多期追号 0否，1多期
	private Integer bonusstop = 1;// 中奖后是否停止：0不停，1停
	private Integer appnumbers = 1;// 倍数
	private Integer issuesnumbers = 1;// 追期

	public String getLotteryid() {
		return lotteryid;
	}

	public void setLotteryid(String lotteryid) {
		this.lotteryid = lotteryid;
	}

	public String getIssue() {
		return issue;
	}

	public void setIssue(String issue) {
		this.issue = issue;
	}

	public Integer getLotteryvalue() {
		return lotteryvalue;
	}

	public void setLotteryvalue(Integer lotteryvalue) {
		this.lotteryvalue = lotteryvalue;
	}

	public Integer getIssueflag() {
		if (issuesnumbers > 1)
			issueflag = 1;
		else
			issueflag = 0;
		return issueflag;
	}

	public Integer getBonusstop() {
		return bonusstop;
	}

	public void setBonusstop(Integer bonusstop) {
		this.bonusstop = bonusstop;
	}

	public List<Ticket> getTickets() {
		return tickets;
	}

	public Integer getAppnumbers() {
		return appnumbers;
	}

	public void setAppnumbers(Integer appnumbers) {
		this.appnumbers = appnumbers;
		statistics();
	}

	public Integer getLotterynumber() {
		return lotterynumber;
	}

	public Integer getIssuesnumbers() {
		return issuesnumbers;
	}

	public void setIssuesnumbers(Integer lotterynumber) {
		this.issuesnumbers = lotterynumber;
		statistics();
	}

	/**
	 * 添加信息到购物车
	 * 
	 * @param ticket
	 * @return
	 */
	public boolean addTicket(Ticket ticket) {
		boolean result = false;
		if (!tickets.contains(ticket)) {
			result = tickets.add(ticket);
		}
		if (result)
			statistics();

		log();
		return result;
	}

	/**
	 * 删除购物车信息
	 * 
	 * @param ticket
	 * @return
	 */
	private boolean deleteTicket(Ticket ticket) {
		boolean result = false;
		result = tickets.remove(ticket);
		if (result)
			statistics();
		log();
		return result;
	}

	/**
	 * 按位置删除
	 * 
	 * @param position
	 * @return
	 */
	public boolean deleteTicket(int position) {
		boolean result = false;
		Ticket ticket = tickets.get(position);
		result = deleteTicket(ticket);

		log();
		return result;
	}

	/**
	 * 生成一注机选号码
	 */
	public void addOneRandomLottery() {
		if (StringUtils.isNotBlank(lotteryid)) {
			switch (Integer.parseInt(lotteryid)) {
				case ConstantValue.SSQ:
					Integer[] reds = new Integer[6];
					Integer[] blues = new Integer[1];
					createNewLottery(reds, blues);
					// 添加一注双色球投注
					Ticket item = new Ticket(reds, blues);
					item.setLotterynumber(1);
					item.setLotteryId(Integer.valueOf(getLotteryid()));

					addTicket(item);
					break;
				case ConstantValue.FC3D:
					// 添加一注3D投注
					break;
				case ConstantValue.QLC:
					// 添加一注七乐彩投注
					break;
			}
		}
	}

	/**
	 * 机选指定数目的红球和蓝球
	 * 
	 * @param redNum
	 * @param blueNum
	 */
	private void createNewLottery(Integer[] reds, Integer[] blues) {
		CopyOnWriteArrayList<Integer> redPool = new CopyOnWriteArrayList<Integer>();
		CopyOnWriteArrayList<Integer> bluePool = new CopyOnWriteArrayList<Integer>();
		while (redPool.size() < reds.length) {
			int temp;
			if (!redPool.contains(temp = CalculatorUtils.getRandomNumber(1, 33))) {
				redPool.add(temp);
			}
		}
		while (bluePool.size() < blues.length) {
			int temp;
			if (!bluePool.contains(temp = CalculatorUtils.getRandomNumber(1, 16))) {
				bluePool.add(temp);
			}
		}
		redPool.toArray(reds);
		bluePool.toArray(blues);
	}

	/**
	 * 获取投注的彩票内容
	 * 
	 * @return
	 */
	public String getLotteryCode() {
		StringBuilder result = new StringBuilder();
		for (Ticket ticket : tickets) {
			result.append("^").append((ticket.getLotterycode()));
		}
		return result.substring(1);
	}

	/**
	 * 统计当前购物车中的方案信息
	 */
	public void statistics() {
		lotteryvalue = 0;
		lotterynumber = 0;
		for (Ticket item : tickets) {
			lotterynumber += item.getLotterynumber();
		}
		// 注数、单注钱数、追期、倍投
		lotteryvalue = lotterynumber * BASEMONEY * issuesnumbers * appnumbers * 100;
		log();
	}

	/**
	 * 清空购物车（含彩种和期次）
	 */
	public void clear() {
		issue = "";
		lotteryid = "";
		clearTicket();
	}

	/**
	 * 清空购物车中所有的投注信息（不含彩种及期次）
	 */
	public void clearTicket() {
		lotterynumber = 0;
		lotteryvalue = 0;
		issuesnumbers = 1;
		appnumbers = 1;
		tickets.clear();
		log();
	}

	public void log() {
		Log.i(TAG, "彩种：" + lotteryid + "期次：" + issue + "方案金额：" + lotteryvalue + "注数：" + lotterynumber + "追期：" + issuesnumbers + "倍投：" + appnumbers);
	}
}
