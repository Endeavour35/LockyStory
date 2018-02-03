package cn.itcast.lottery;

public interface ConstantValue {
	/**
	 * 注册请求
	 */
	String ELEMENT_USER_REGISTE = "10001";
	/**
	 * 登陆请求
	 */
	String ELEMENT_USER_LOGIN = "14001";

	/**
	 * 用户投注
	 */
	String ELEMENT_USER_BETTING = "12006";
	/**
	 * 用户余额
	 */
	String ELEMENT_USER_BALANCE = "11007";

	/**
	 * 当前奖期信息查询
	 */
	String ELEMENT_CURRENT_ISSUE = "12002";

	/**
	 * 代理商id
	 */
	String AGENTER_ID = "1000002";

	/**
	 * 代理商密码
	 */
	String AGENT_PASSWORD = "9ab62a694d8bf6ced1fab6acd48d02f8";

	/**
	 * des加密用密钥
	 */
	String DES_KEY = "9b2648fcdfbad80f";
	/**
	 * 加密形式
	 */
	String COMPRESS = "DES";

	/**
	 * 编码格式
	 */
	String ENCODING = "utf-8";

	/**
	 * 来源
	 */
	String SOURCE = "ivr";

	/**
	 * 服务器处理成功
	 */
	String SUCCESS = "0";
	/**
	 * 双色球标示
	 */
	int SSQ = 118;
	/**
	 * 福彩3d玩法编号
	 */
	int FC3D = 116;
	/**
	 * 七乐彩玩法编号
	 */
	int QLC = 117;

	/**
	 * 彩票服务器连接
	 */
	// String URL_LOTTERY = "http://192.168.1.134:8080/ZCWService/Entrance";
	String URL_LOTTERY = "http://10.0.2.2:8080/ZCWService/Entrance";

	/**
	 * 获取新闻链接
	 */
	String URL_NEWS = "http://10.0.2.2:8080/ZCWService/news.xml";

	/**
	 * 新闻链接
	 */
	String URL_NEWS_ITEM = "http://10.0.2.2:8080/ZCWService/upload/news/1.html";

	/**
	 * 网址替代部分
	 */
	String URL_NEWS_REPLACE = "http://10.0.2.2:8080/ZCWService/";

	/****************************** 界面用常量 ***********************************/

	/**
	 * 购彩大厅
	 */
	int VIEW_HALL = 1;
	/**
	 * 双色球选号
	 */
	int VIEW_PLAY_SSQ = 2;

	/**
	 * 彩票购物车
	 */
	int VIEW_SHOPPING_LIST = 3;

	/**
	 * 登陆
	 */
	int VIEW_USER_LOGIN = 10;
	/**
	 * 注册
	 */
	int VIEW_USER_REGISTE = 11;

	/**
	 * 完善注册
	 */
	int VIEW_COMPLETEPERSONINFO = 12;

	/**
	 * 首页
	 */
	int VIEW_HOST = 0;
	/**
	 * 设置追期和倍投页面
	 */
	int VIEW_PREFECTBETTING = 15;

	/**
	 * 新闻页
	 */
	int VIEW_NEWS = 20;
	/**
	 * 新闻详情
	 */
	int VIEW_NEWS_DETAIL = 21;
	/******************* 用户提示 ********************/
	int PROGRESS_OPEN = 0;
	int PROGRESS_CLOSE = 1;
	int ERROE = 2;
	int EXIT = 3;
	/**********************************************/
	/******************* 新闻相关 *******************/
	String NEWS_PATH = "news";
	/**
	 * 首页上用于展示的新闻条目数
	 */
	int NEWS_SHOW_NUM = 10;
	int NEWS_NUM = 100;
}
