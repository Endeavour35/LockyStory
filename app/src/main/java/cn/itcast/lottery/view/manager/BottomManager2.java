package cn.itcast.lottery.view.manager;

import java.util.Observable;
import java.util.Observer;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.itcast.lottery.ConstantValue;
import cn.itcast.lottery.R;
import cn.itcast.lottery.R.id;
import cn.itcast.lottery.view.BaseView;
import cn.itcast.lottery.view.Hall;
import cn.itcast.lottery.view.Host;
import cn.itcast.lottery.view.PlayGame;

/**
 * 管理底部菜单
 * 
 * @author Administrator
 * 
 */
public class BottomManager2 implements Observer {
	protected static final String TAG = "BottomManager";

	/******************* 第一步：管理对象的创建(单例模式) ***************************************************/
	// 创建一个静态实例
	private static BottomManager2 instrance;

	// 构造私有
	private BottomManager2() {
	}

	// 提供统一的对外获取实例的入口
	public static BottomManager2 getInstrance() {
		if (instrance == null) {
			instrance = new BottomManager2();
		}
		return instrance;
	}

	/*********************************************************************************************/
	/******************* 第二步：初始化各个导航容器及相关控件设置监听 *********************************/

	/********** 底部菜单容器 **********/
	private RelativeLayout bottomMenuContainer;
	/************ 底部导航 ************/
	private LinearLayout commonBottom;// 购彩通用导航
	private LinearLayout playBottom;// 购彩
	private LinearLayout informationCommonBottom;// 资讯导航

	/***************** 导航按钮 ******************/

	/************ 购彩导航底部按钮及提示信息 ************/
	private ImageButton cleanButton;
	private ImageButton addButton;

	private TextView playBottomNotice;

	/************ 购彩通用导航底部按钮 ************/
	private ImageButton homeButton;
	private ImageButton hallButton;
	private ImageButton rechargeButton;
	private ImageButton myselfButton;
	/************ 资讯通用导航底部按钮 *************/
	private ImageButton newsButton;
	private ImageButton buyButton;

	public void init(Activity activity) {
		bottomMenuContainer = (RelativeLayout) activity.findViewById(R.id.ii_main_bottom);
		commonBottom = (LinearLayout) activity.findViewById(R.id.ii_bottom_common);
		playBottom = (LinearLayout) activity.findViewById(R.id.ii_bottom_game);
		informationCommonBottom = (LinearLayout) activity.findViewById(R.id.ii_bottom_information_common);

		playBottomNotice = (TextView) activity.findViewById(R.id.ii_bottom_game_choose_notice);
		cleanButton = (ImageButton) activity.findViewById(R.id.ii_bottom_game_choose_clean);
		addButton = (ImageButton) activity.findViewById(R.id.ii_bottom_game_choose_ok);

		homeButton = (ImageButton) activity.findViewById(R.id.ii_bottom_home);
		hallButton = (ImageButton) activity.findViewById(R.id.ii_bottom_lottery_hall);

		buyButton = (ImageButton) activity.findViewById(R.id.ii_bottom_buy);

		// 设置监听
		setListener();
	}

	/**
	 * 设置监听
	 */
	private void setListener() {
		// 清空按钮
		cleanButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Log.i(TAG, "点击清空按钮");
				BaseView currentView = UiManager2.getInstance().getCurrentView();
				if (currentView instanceof PlayGame) {
					((PlayGame) currentView).clearSelected();
				}

			}
		});
		// 选好按钮
		addButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Log.i(TAG, "点击选好按钮");
				BaseView currentView = UiManager2.getInstance().getCurrentView();
				if (currentView instanceof PlayGame) {
					((PlayGame) currentView).addSelected2ShoppingList();
				}

			}
		});
		// 首页
		homeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				UiManager2.getInstance().clearBackCache();
				UiManager2.getInstance().changeView3(Host.class, null, true);
			}
		});
		// 购彩大厅
		hallButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				UiManager2.getInstance().changeView3(Hall.class, null, true);
			}
		});
		// 购彩
		buyButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				UiManager2.getInstance().clearBackCache();
				UiManager2.getInstance().changeView3(Hall.class, null, true);

			}
		});
	}

	/*********************************************************************************************/
	/****************** 第三步：控制各个导航容器的显示和隐藏 *****************************************/

	private void initBottom() {
		if (commonBottom.getVisibility() == View.VISIBLE)
			commonBottom.setVisibility(View.GONE);
		if (playBottom.getVisibility() == View.VISIBLE)
			playBottom.setVisibility(View.GONE);
		if (informationCommonBottom.getVisibility() == View.VISIBLE)
			informationCommonBottom.setVisibility(View.GONE);
	}

	/**
	 * 转换到通用导航
	 */
	public void showCommonBottom() {
		if (bottomMenuContainer.getVisibility() != View.VISIBLE) {
			bottomMenuContainer.setVisibility(View.VISIBLE);
		}
		initBottom();
		commonBottom.setVisibility(View.VISIBLE);
	}

	/**
	 * 转换到购彩
	 */
	public void showGameBottom() {
		if (bottomMenuContainer.getVisibility() != View.VISIBLE) {
			bottomMenuContainer.setVisibility(View.VISIBLE);
		}
		initBottom();
		playBottom.setVisibility(View.VISIBLE);
	}

	/**
	 * 转换到首页导航
	 */
	public void showHostBottom() {
		if (bottomMenuContainer.getVisibility() != View.VISIBLE) {
			bottomMenuContainer.setVisibility(View.VISIBLE);
		}
		initBottom();
		informationCommonBottom.setVisibility(View.VISIBLE);
	}

	/**
	 * 改变底部导航容器显示情况
	 */
	public void changeBottomVisiblity(int type) {
		if (bottomMenuContainer.getVisibility() != type)
			bottomMenuContainer.setVisibility(type);
	}

	/*********************************************************************************************/
	/*********************** 第四步：控制玩法导航内容显示 ********************************************/

	/**
	 * 设置玩法底部提示信息
	 * 
	 * @param notice
	 */
	public void changeGameBottomNotice(String notice) {
		playBottomNotice.setText(notice);
	}

	/*********************************************************************************************/

	public void update(Observable observable, Object data) {
		if (data != null) {
			Log.i(TAG, data.toString());
			if (StringUtils.isNumeric(data.toString())) {
				int type = Integer.parseInt(data.toString());
				switch (type) {
					case ConstantValue.VIEW_HALL:
					case ConstantValue.VIEW_USER_LOGIN:
					case ConstantValue.VIEW_USER_REGISTE:
						showCommonBottom();
						break;
					case ConstantValue.VIEW_PLAY_SSQ:
						showGameBottom();
						break;
					case ConstantValue.VIEW_SHOPPING_LIST:
						changeBottomVisiblity(View.GONE);
						break;
					case ConstantValue.VIEW_HOST:
					case ConstantValue.VIEW_NEWS_DETAIL:
						showHostBottom();
						break;
				}
			}
		}
	}
}
