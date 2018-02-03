package cn.itcast.lottery.util;

import java.util.HashMap;
import java.util.Map;

import cn.itcast.lottery.R;

/**
 * 异常管理
 * 
 * @author rui.cao@soarmobile.com.cn
 * 
 */
public class MException extends RuntimeException {

	/**
	 * 磁盘空间不足
	 */
	public static int DISK_SPACE_SHORTTAGE = -1;
	/**
	 * 网络异常
	 */
	public static int NETWORK_ANOMALY = 1;
	/**
	 * 找不到请求的资源
	 */
	public static final int NOT_FIND_RESOURCE = 404;

	/**
	 * 配置文件加载异常
	 */
	public static int FAILURE_LOADSETTING = 2;
	/**
	 * 解析同步文件异常
	 */
	public static final int FAILURE_PARSAER_SYNC = 3;
	/**
	 * 未知异常
	 */
	public static final int UNKNOW_EXCEPTION = 4;
	/**
	 * 错误的同步信息
	 */
	public static final int WRONG_INFO = 5;
	/**
	 * 解析用户请求错误，缺少必要的参数
	 */
	public static final int USERINFO_ERROR = 6;

	/**
	 * 其他鉴权错误
	 */
	public static final int AUTHENTICATION_ERROR = 7;
	/**
	 * 登陆数据组装异常
	 */
	public static int LOGIN_ASSEMBLE_ERROR = 8;

	public static int LOGIN_ERROR = 9;

	// 异常状况记录
	public static Map<Integer, Integer> serviceExceptions = new HashMap<Integer, Integer>();
	static {
		serviceExceptions.put(DISK_SPACE_SHORTTAGE, R.string.is_error_disk_space_shorttage);
		serviceExceptions.put(WRONG_INFO, R.string.is_error_wrong_info);
		serviceExceptions.put(USERINFO_ERROR, R.string.is_error_userinfo_error);
		serviceExceptions.put(AUTHENTICATION_ERROR, R.string.is_error_authentication_error);
		serviceExceptions.put(NOT_FIND_RESOURCE, R.string.is_error_not_find_resource);
		serviceExceptions.put(500, R.string.is_error_500);
		serviceExceptions.put(LOGIN_ASSEMBLE_ERROR, R.string.is_error_login_assemble_error);
	}

	private int code;

	public MException(int code, String msg) {
		super(msg);
		this.code = code;
	}

	public MException(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}
}
