package cn.itcast.lottery.view.manager;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.itcast.lottery.R;
import cn.itcast.lottery.view.UserLogin;
import cn.itcast.lottery.view.UserRegiste;

/**
 * 管理标题容器
 * 
 * @author Administrator
 * 
 */
public class TopManager {
	protected static final String TAG = "TopManager";

	/******************* 步骤一：管理对象的创建 ************************************************************************/
	// 创建一个静态实例
	private static TopManager instrance;

	// 构造私有
	private TopManager() {
	}

	// 提供统一的对外获取实例的入口
	public static TopManager getInstrance() {
		if (instrance == null) {
			instrance = new TopManager();
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

}
