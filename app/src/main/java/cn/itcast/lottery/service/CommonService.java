package cn.itcast.lottery.service;

import cn.itcast.lottery.net.protocol.Message;
/**
 * 通用功能管理
 * @author Administrator
 *
 */
public interface CommonService {
	/**
	 * 获取当前销售期信息
	 * @param lotteryId
	 * @return
	 */
	Message getCurrentIssueInfo(String lotteryId);
}
