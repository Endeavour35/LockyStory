package cn.itcast.lottery.view.manager;

import java.util.Observable;
import java.util.Observer;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.itcast.lottery.ConfigParams;
import cn.itcast.lottery.ConstantValue;
import cn.itcast.lottery.R;
import cn.itcast.lottery.bean.User;
import cn.itcast.lottery.net.protocol.Message;
import cn.itcast.lottery.net.protocol.Oelement;
import cn.itcast.lottery.net.protocol.element.BalanceElement;
import cn.itcast.lottery.service.UserService;
import cn.itcast.lottery.util.BeanFactory;
import cn.itcast.lottery.util.PreferenceManager;
import cn.itcast.lottery.util.PromptManager;
import cn.itcast.lottery.view.UserLogin;
import cn.itcast.lottery.view.UserRegiste;

/**
 * 管理标题容器
 * 
 * @author Administrator
 * 
 */
public class TopManager2 implements Observer {
	protected static final String TAG = "TopManager";

	/******************* 步骤一：管理对象的创建 ************************************************************************/
	// 创建一个静态实例
	private static TopManager2 instrance;

	// 构造私有
	private TopManager2() {
	}

	// 提供统一的对外获取实例的入口
	public static TopManager2 getInstrance() {
		if (instrance == null) {
			instrance = new TopManager2();
		}
		return instrance;
	}
	/*******************************************************************************************************************/
	
	/******************* 步骤二：初始化各个标题容器及相关控件设置监听 ****************************************************/
	/************ 标题的容器 **************/
	private RelativeLayout commonContainer;// 通用标题容器
	private RelativeLayout loginContainer;// 登陆标题容器
	private RelativeLayout unLoginContainer;// 未登陆标题容器
	private RelativeLayout hostContainer;// 首页标题容器

	/**************** 通用标题 *******************/
	private ImageButton returnButton;
	private ImageButton helpButton;
	private TextView titleView;
	/**************** 未登录标题 *******************/
	private Button registeButton;
	private Button loginButton;
	/**************** 登录标题 ******************/
	private TextView userInfoTextView;

	private Context context;

	/**
	 * 初始化工作
	 * 
	 * @param activity
	 */
	public void init(Activity activity) {
		context = activity.getApplicationContext();

		// 初始化通用标题
		commonContainer = (RelativeLayout) activity.findViewById(R.id.ii_top_common_continer);
		returnButton = (ImageButton) activity.findViewById(R.id.ii_top_return);
		helpButton = (ImageButton) activity.findViewById(R.id.ii_top_help);
		titleView = (TextView) activity.findViewById(R.id.ii_top_title);

		// 初始化登录标题
		loginContainer = (RelativeLayout) activity.findViewById(R.id.ii_top_login_continer);
		userInfoTextView = (TextView) activity.findViewById(R.id.ii_top_user_info);

		// 初始化未登录标题
		unLoginContainer = (RelativeLayout) activity.findViewById(R.id.ii_top_unlogin_continer);
		registeButton = (Button) activity.findViewById(R.id.ii_top_regist);
		loginButton = (Button) activity.findViewById(R.id.ii_top_login);

		// 首页标题
		hostContainer = (RelativeLayout) activity.findViewById(R.id.ii_top_host);

		// 设置监听
		setListener();

	}

	private void setListener() {
		// 注册按钮
		registeButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Log.i(TAG, "点击注册按钮");
				// UiManager.getInstance().changeView(UserRegiste.class, null);
				UiManager2.getInstance().changeView3(UserRegiste.class, null, true);
			}
		});
		// 登陆按钮
		loginButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Log.i(TAG, "点击登录按钮");
				// UiManager.getInstance().changeView(UserLogin.class, null);
				// UiManager.getInstance().changeView(new UserLoginView(loginButton.getContext(),null));
				// UiManager.getInstance().changeView1(UserLoginView.class, null);
				// UiManager.getInstance().changeView2(UserLoginView.class, null);
				UiManager2.getInstance().changeView3(UserLogin.class, null, true);
			}
		});
		// 返回按钮
		returnButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Log.i(TAG, "点击返回按钮");
				UiManager2.getInstance().changeCacheView();
			}
		});
		// 帮助按钮
		helpButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Log.i(TAG, "点击帮助按钮");

			}
		});
	}

	/******************* 第三步：控制各个标题容器的显示和隐藏 ****************************************************/
	/**
	 * 将标题全部隐藏
	 */
	private void initTitle() {
		if (loginContainer.getVisibility() != View.GONE)
			loginContainer.setVisibility(View.GONE);
		if (unLoginContainer.getVisibility() != View.GONE)
			unLoginContainer.setVisibility(View.GONE);
		if (commonContainer.getVisibility() != View.GONE)
			commonContainer.setVisibility(View.GONE);
		if (hostContainer.getVisibility() != View.GONE)
			hostContainer.setVisibility(View.GONE);
	}

	/**
	 * 1、普通标题 标题内容管理 帮助和返回按钮显示控制
	 */
	public void showCommonTitle(int title, boolean showHelp, boolean showBack) {
		initTitle();
		// 显示需要标题
		commonContainer.setVisibility(View.VISIBLE);

		// 设置内容显示
		// 设置返回按钮显示状态
		setTitleButtonVisible(showBack, returnButton);
		// 设置帮助按钮显示状态
		setTitleButtonVisible(showHelp, helpButton);
		// 设置标题内容
		titleView.setText(title);
	}

	/**
	 * 2、未登陆标题
	 */
	public void showUnLoginTitle() {
		initTitle();
		unLoginContainer.setVisibility(View.VISIBLE);
	}

	/**
	 * 3、登陆中
	 */
	public void showLoginningTitle() {
		initTitle();
		loginContainer.setVisibility(View.VISIBLE);
		// 设置标题内容
		userInfoTextView.setText(R.string.is_user_loginning);
	}

	/**
	 * 4、登陆完成后标题
	 */
	public void showLoginedTitle(String userInfo) {
		initTitle();
		loginContainer.setVisibility(View.VISIBLE);
		// 设置标题内容
		userInfoTextView.setText(userInfo);
	}

	/**
	 * 设置首页标题显示
	 */
	public void showHost() {
		initTitle();
		hostContainer.setVisibility(View.VISIBLE);
	}

	/*************************************************************************************************************/
	/*************************** 第四步：控制标题内容显示 *****************************************************/
	/**
	 * 控制按钮显示情况
	 * 
	 * @param isShow
	 * @param button
	 */
	private void setTitleButtonVisible(boolean isShow, ImageButton button) {
		if (isShow) {
			int visibility = button.getVisibility();
			if (visibility != View.VISIBLE) {
				button.setVisibility(View.VISIBLE);
			}
		} else {
			int visibility = button.getVisibility();
			if (visibility == View.VISIBLE)
				button.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * 设置通用标题中标题内容
	 * 
	 * @param title
	 */
	public void setCommonTitle(String title) {
		if (titleView != null) {
			titleView.setText(title);
		}
	}

	/*************************************************************************************************************/

	public void update(Observable observable, Object data) {
		if (data != null) {
			Log.i(TAG, data.toString());
			if (StringUtils.isNumeric(data.toString())) {
				int type = Integer.parseInt(data.toString());
				switch (type) {
					case ConstantValue.VIEW_HALL:
						// 是否已经登陆
						if (ConfigParams.ISLOGIN) {
							// 显示已经登陆标题
							String msg = context.getString(R.string.is_user_loginned_notice);
							showLoginedTitle(StringUtils.replaceEach(msg, new String[] { "T%", "M%" }, new String[] { ConfigParams.USERNAME,
									ConfigParams.INVESTVALUES.toString() }));
						} else {
							// 是否自动登陆
							boolean checkAutoLogin = PreferenceManager.checkAutoLogin(context);
							if (checkAutoLogin) {
								// 进行登陆操作
								showLoginningTitle();
								// 处理用户登陆
								userLogin();
							} else {
								// 显示未登录窗口
								showUnLoginTitle();
							}
						}

						break;
					case ConstantValue.VIEW_PLAY_SSQ:
						showCommonTitle(R.string.is_ssq_title_default, true, true);
						break;

					case ConstantValue.VIEW_USER_LOGIN:
						showCommonTitle(R.string.is_login, true, true);
						break;
					case ConstantValue.VIEW_USER_REGISTE:
						showCommonTitle(R.string.is_regist_title, true, true);
						break;
					case ConstantValue.VIEW_HOST:
						showHost();
						break;
					case ConstantValue.VIEW_NEWS_DETAIL:
						showCommonTitle(R.string.is_news_detail_default_title, false, true);
						break;
				}
			}
		}

	}

	/**
	 * 处理用户登陆
	 */
	private void userLogin() {
		new AsyncTask<String, Integer, Message>() {

			@Override
			protected Message doInBackground(String... params) {
				Message result = null;
				User user = new User();
				user.setUsername(PreferenceManager.getUserName(context));
				user.setActpassword(PreferenceManager.getPassword(context));

				UserService service = BeanFactory.getImpl(UserService.class);
				result = service.login(user);

				// 处理用户登陆回复
				if (result != null) {
					String errorcode = result.getBody().getOelement().getErrorcode();
					String errormsg = result.getBody().getOelement().getErrormsg();

					// 判断登陆状态
					if (ConstantValue.SUCCESS.equals(errorcode)) {
						// 保存用户信息
						if (PreferenceManager.checkAutoLogin(context)) {
							PreferenceManager.changeUserInfo(context, user.getUsername(), user.getActpassword());
						}

						// 设置登录状态
						ConfigParams.ISLOGIN = true;
						ConfigParams.USERNAME = PreferenceManager.getUserName(context);

						// 获取账户信息
						result = service.balance(user);
						return result;
					} else {
						// 服务器处理失败，提示用户失败原因
						PromptManager.showToast(context, errormsg);
					}
				} else {
					// 处理失败，网络原因
					PromptManager.showToast(context, R.string.is_error_network);
				}

				return null;
			}

			@Override
			protected void onPostExecute(Message result) {
				if (result != null) {
					Oelement oelement = result.getBody().getOelement();
					String errorcode = oelement.getErrorcode();
					String errormsg = oelement.getErrormsg();

					// 判断余额是否获取成功
					if (ConstantValue.SUCCESS.equals(errorcode)) {
						BalanceElement element = (BalanceElement) result.getBody().getElements().get(0);
						// 获取到余额信息
						if (StringUtils.isNotBlank(element.getInvestvalues())) {
							ConfigParams.INVESTVALUES = Double.parseDouble(element.getInvestvalues());
							// 显示已登录界面
							String msg = context.getString(R.string.is_user_loginned_notice);
							showLoginedTitle(StringUtils.replaceEach(msg, new String[] { "T%", "M%" }, new String[] { ConfigParams.USERNAME,
									ConfigParams.INVESTVALUES.toString() }));
						}
					} else {
						// 服务器处理失败，提示用户失败原因
						PromptManager.showToast(context, errormsg);
					}
				} else {
					// 处理失败，网络原因
					PromptManager.showToast(context, R.string.is_error_network);
				}
			}

		}.execute("");

	}

}
