package cn.itcast.lottery.view.manager;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;
import cn.itcast.lottery.ConstantValue;
import cn.itcast.lottery.R;
import cn.itcast.lottery.view.BaseView;

/**
 * 控制内容部分切换
 * 
 * @author Administrator
 * 
 */
public class UiManager {
	private static final String TAG = "UiManager";
	/************* 简单的实现单例模式 *****/
	private static UiManager uiManager= new UiManager();

	private UiManager() {
	}

	public static UiManager getInstance() {
		return uiManager;
	}

	/***********************************/

	RelativeLayout continer;// 内容部分容器

	/**
	 * 设置容器
	 * 
	 * @param continer
	 */
	public void setContiner(RelativeLayout continer) {
		this.continer = continer;
	}

	/***************** 界面缓存 ******************/
	private static Map<String, BaseView> VIEWCACHE = new HashMap<String, BaseView>();
	/***************** 返回界面缓存 ******************/
	private static LinkedList<String> BACKVIEW = new LinkedList<String>();

	private BaseView currentView = null;// 当前界面

	/**
	 * 界面切换
	 * 
	 * @param baseView
	 * @return
	 */
	public boolean changeView(BaseView baseView) {

		if (continer == null)
			return false;
		continer.removeAllViews();
		continer.addView(baseView.getView());

		return true;
	}

	/**
	 * 界面切换
	 * 
	 * @param baseView
	 * @return
	 */
	public boolean changeView1(Class<? extends BaseView> newView, Bundle bundle) {
		// 返回结果
		boolean result = true;
		if (continer == null)
			return false;

		continer.removeAllViews();

		// 创建目标界面的实例
		Constructor<? extends BaseView> newViewInstanceConstructor;

		try {
			BaseView newViewInstance = null;
			if (VIEWCACHE.containsKey(newView.getSimpleName())) {
				newViewInstance = VIEWCACHE.get(newView.getSimpleName());
			} else {
				newViewInstanceConstructor = newView.getConstructor(Context.class, Bundle.class);
				newViewInstance = newViewInstanceConstructor.newInstance(continer.getContext(), bundle);
				Log.i(TAG, "new instance");
				VIEWCACHE.put(newView.getSimpleName(), newViewInstance);
			}

			continer.addView(newViewInstance.getView());
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		return result;
	}

	/**
	 * 界面切换
	 * 
	 * @param baseView
	 * @return
	 */
	public boolean changeView2(Class<? extends BaseView> newView, Bundle bundle) {
		// 返回结果
		boolean result = true;
		if (continer == null)
			return false;

		if (currentView != null && currentView.getClass() == newView) {
			Log.i(TAG, "same view");
			return false;
		}

		continer.removeAllViews();

		// 创建目标界面的实例
		Constructor<? extends BaseView> newViewInstanceConstructor;

		try {
			BaseView newViewInstance = null;
			if (VIEWCACHE.containsKey(newView.getSimpleName())) {
				newViewInstance = VIEWCACHE.get(newView.getSimpleName());
			} else {
				newViewInstanceConstructor = newView.getConstructor(Context.class, Bundle.class);
				newViewInstance = newViewInstanceConstructor.newInstance(continer.getContext(), bundle);
				Log.i(TAG, "new instance");
				VIEWCACHE.put(newView.getSimpleName(), newViewInstance);
			}

			continer.addView(newViewInstance.getView());

			currentView = newViewInstance;
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		return result;
	}

	/**
	 * 界面切换
	 * 
	 * @param baseView
	 * @return
	 */
	public boolean changeView3(Class<? extends BaseView> newView, Bundle bundle, boolean needBack) {
		// 返回结果
		boolean result = true;
		if (continer == null)
			return false;

		if (currentView != null) {
			if (currentView.getClass() == newView) {
				Log.i(TAG, "same view");
				return false;
			}
		}

		continer.removeAllViews();

		// 创建目标界面的实例
		Constructor<? extends BaseView> newViewInstanceConstructor;

		try {
			BaseView newViewInstance = null;
			if (VIEWCACHE.containsKey(newView.getSimpleName())) {
				newViewInstance = VIEWCACHE.get(newView.getSimpleName());
			} else {
				newViewInstanceConstructor = newView.getConstructor(Context.class, Bundle.class);
				newViewInstance = newViewInstanceConstructor.newInstance(continer.getContext(), bundle);
				Log.i(TAG, "new instance");
				VIEWCACHE.put(newView.getSimpleName(), newViewInstance);
			}

			continer.addView(newViewInstance.getView());

			currentView = newViewInstance;

			if (needBack) {
				BACKVIEW.add(newView.getSimpleName());
			}

			changeTitleAndBottom(newViewInstance.getId());

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		return result;
	}

	/**
	 * 切换返回缓存信息
	 * 
	 * @return
	 */
	public boolean changeCacheView() {
		boolean result = false;
		String poll = BACKVIEW.getFirst();

		if (StringUtils.isNotBlank(poll)) {

			if (!currentView.getClass().getSimpleName().equals(poll)) {
				continer.removeAllViews();
				continer.addView(VIEWCACHE.get(poll).getView());
				currentView = VIEWCACHE.get(poll);
				changeTitleAndBottom(currentView.getId());
				result = true;
			} else {
				if (BACKVIEW.size() > 1) {
					BACKVIEW.remove(poll);
					result = changeCacheView();
				} else
					return false;
			}

		}
		return result;
	}

	/**
	 * 切换标题和底部导航
	 */
	public void changeTitleAndBottom(int type) {
		switch (type) {
			case ConstantValue.VIEW_USER_LOGIN:
				TopManager.getInstrance().showUnLoginTitle();
				BottomManager.getInstrance().showGameBottom();
				break;
			case ConstantValue.VIEW_USER_REGISTE:
				TopManager.getInstrance().showCommonTitle(R.string.is_regist, true, true);
				BottomManager.getInstrance().showCommonBottom();
				break;
		}
	}

}
