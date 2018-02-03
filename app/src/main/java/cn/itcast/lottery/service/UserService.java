package cn.itcast.lottery.service;

import cn.itcast.lottery.bean.User;
import cn.itcast.lottery.net.protocol.Message;
/**
 * 用户管理
 * @author Administrator
 *
 */
public interface UserService {
	/**
	 * 用户注册
	 * @param user
	 * @return
	 */
	Message registe(User user);
	/**
	 * 用户登陆
	 * @param user
	 * @return
	 */
	Message login(User user);
	/**
	 * 余额查询
	 * @param user
	 * @return
	 */
	Message balance(User user);
	/**
	 * 投注
	 * @param user
	 * @return
	 */
	Message betting(User user);
}
