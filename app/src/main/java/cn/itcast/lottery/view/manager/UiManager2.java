package cn.itcast.lottery.view.manager;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Observable;

import org.apache.commons.lang3.StringUtils;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import cn.itcast.lottery.R;
import cn.itcast.lottery.util.PromptManager;
import cn.itcast.lottery.view.BaseView;
import cn.itcast.lottery.view.Hall;
import cn.itcast.lottery.view.Host;

/**
 * 控制内容部分切换
 * 
 * @author Administrator
 * 
 */
public class UiManager2 extends Observable {
	private static final String TAG = "UiManager";
	/************* 简单的实现单例模式 *****/
	private static UiManager2 uiManager;

	private UiManager2() {
	}

	public static UiManager2 getInstance() {
		if (uiManager == null)
			uiManager = new UiManager2();
		return uiManager;
	}

	/***********************************/

	RelativeLayout container;// 内容部分容器

	/**
	 * 设置容器
	 * 
	 * @param continer
	 */
	public void setContainer(RelativeLayout continer) {
		this.container = continer;
	}

	/***************** 界面缓存 ******************/
	// private static Map<String, BaseView> VIEWCACHE = new HashMap<String, BaseView>();
	public static HashMap<String, BaseView> VIEWCACHE = new HashMap<String, BaseView>();
	// public static SoftHashMap<String, BaseView> viewCache = new SoftHashMap<String, BaseView>();

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
		
		if (container == null)
			return false;
		container.removeAllViews();
		container.addView(baseView.getView());
		
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
		if (container == null)
			return false;

		container.removeAllViews();

		// 创建目标界面的实例
		Constructor<? extends BaseView> newViewInstanceConstructor;

		try {
			BaseView newViewInstance = null;
			if (VIEWCACHE.containsKey(newView.getSimpleName())) {
				newViewInstance = VIEWCACHE.get(newView.getSimpleName());
			} else {
				newViewInstanceConstructor = newView.getConstructor(Context.class, Bundle.class);
				newViewInstance = newViewInstanceConstructor.newInstance(container.getContext(), bundle);
				Log.i(TAG, "new instance");
				VIEWCACHE.put(newView.getSimpleName(), newViewInstance);
			}

			container.addView(newViewInstance.getView());
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
		if (container == null)
			return false;

		if (currentView != null) {
			if (currentView.getClass() == newView) {
				Log.i(TAG, "same view");
				return false;
			}
		}

		container.removeAllViews();

		// 创建目标界面的实例
		Constructor<? extends BaseView> newViewInstanceConstructor;

		try {
			BaseView newViewInstance = null;
			if (VIEWCACHE.containsKey(newView.getSimpleName())) {
				newViewInstance = VIEWCACHE.get(newView.getSimpleName());
			} else {
				newViewInstanceConstructor = newView.getConstructor(Context.class, Bundle.class);
				newViewInstance = newViewInstanceConstructor.newInstance(container.getContext(), bundle);
				Log.i(TAG, "new instance");
				VIEWCACHE.put(newView.getSimpleName(), newViewInstance);
			}

			container.addView(newViewInstance.getView());

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
		if (container == null)
			return false;

		if (currentView != null) {
			hiddenInput();
			currentView.onPause();
			if (currentView.getClass() == newView) {
				Log.i(TAG, "same view");
				return false;
			}
		}

		container.removeAllViews();

		// 创建目标界面的实例
		Constructor<? extends BaseView> newViewInstanceConstructor;

		try {
			BaseView newViewInstance = null;
			if (VIEWCACHE.containsKey(newView.getSimpleName())) {
				newViewInstance = VIEWCACHE.get(newView.getSimpleName());
				if (bundle != null)
					newViewInstance.setBundle(bundle);
			} else {
				newViewInstanceConstructor = newView.getConstructor(Context.class, Bundle.class);
				newViewInstance = newViewInstanceConstructor.newInstance(container.getContext(), bundle);
				Log.i(TAG, "new instance");
				VIEWCACHE.put(newView.getSimpleName(), newViewInstance);
			}

			container.addView(newViewInstance.getView());
			currentView = newViewInstance;
			currentView.getView().setAnimation(AnimationUtils.loadAnimation(container.getContext(), R.anim.view));

			changeTitleAndBottom(newViewInstance.getId());
			newViewInstance.onResume();
			if (needBack) {
				BACKVIEW.addFirst(newView.getSimpleName());
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		return result;
	}

	/**
	 * 隐藏软键盘
	 */
	private void hiddenInput() {
		if (container != null) {
			InputMethodManager manager = (InputMethodManager) container.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
			if (manager != null && manager.isActive()) {
				manager.hideSoftInputFromWindow(container.getWindowToken(), 0);
			}
		}
	}

	/**
	 * 切换返回缓存信息
	 * 
	 * @return
	 */
	public boolean changeCacheView1() {
		String key = "";
		if (BACKVIEW.size() >= 1) {
			key = BACKVIEW.getFirst();
		}
		if (StringUtils.isNotBlank(key)) {
			if (!currentView.getClass().getSimpleName().equals(key)) {

				BaseView baseView = VIEWCACHE.get(key);

				container.removeAllViews();
				container.addView(baseView.getView());
				currentView = baseView;
				currentView.getView().setAnimation(AnimationUtils.loadAnimation(container.getContext(), R.anim.view));
				changeTitleAndBottom(baseView.getId());
				return true;
			} else {
				BACKVIEW.remove(key);
				return changeCacheView1();
			}
		}
		return false;
	}

	/**
	 * 切换返回缓存信息
	 * 
	 * @return
	 */
	public boolean changeCacheView() {
		boolean result = false;
		String key = "";
		if (BACKVIEW.size() >= 1) {
			key = BACKVIEW.getFirst();
		}

		if (StringUtils.isNotBlank(key)) {

			if (!currentView.getClass().getSimpleName().equals(key)) {
				BaseView view = VIEWCACHE.get(key);
				if (view == null) {
					// 缓存中信息已经被回收
					BACKVIEW.clear();
					PromptManager.showToast(container.getContext(), R.string.is_error_story_low);
					changeView3(Hall.class, null, true);
					return true;
				} else {
					hiddenInput();
					currentView.onPause();
					container.removeAllViews();
					container.addView(view.getView());
					currentView = view;
					changeTitleAndBottom(currentView.getId());
					currentView.onResume();
					currentView.getView().setAnimation(AnimationUtils.loadAnimation(container.getContext(), R.anim.view));
					result = true;
				}
			} else {
				BACKVIEW.remove(key);
				result = changeCacheView();
			}

		}
		return result;
	}

	/**
	 * 切换标题和底部导航
	 */
	public void changeTitleAndBottom(int type) {
		setChanged();
		notifyObservers(type);

		// switch(type)
		// {
		// case ConstantValue.VIEW_USER_LOGIN:
		// TopManager.getInstrance().showUnLoginTitle();
		// BottomManager.getInstrance().showGameBottom();
		// break;
		// case ConstantValue.VIEW_USER_REGISTE:
		// TopManager.getInstrance().showCommonTitle(R.string.is_regist, true, true);
		// BottomManager.getInstrance().showCommonBottom();
		// break;
		// }
	}

	public BaseView getCurrentView() {
		return currentView;
	}

	public void clearBackCache() {
		BACKVIEW.clear();
//		BACKVIEW.add(Host.class.getSimpleName());
	}

}
