package cn.itcast.lottery;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.widget.RelativeLayout;
import cn.itcast.lottery.util.PromptManager;
import cn.itcast.lottery.view.BaseView;
import cn.itcast.lottery.view.Hall;
import cn.itcast.lottery.view.manager.BottomManager2;
import cn.itcast.lottery.view.manager.TopManager2;
import cn.itcast.lottery.view.manager.UiManager2;

public class MainActivity extends Activity {

	private ProgressDialog progressDialog;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case ConstantValue.PROGRESS_OPEN:
					// 信息加载进度条
					PromptManager.showSimpleProgressDialog(progressDialog, getString(R.string.is_loading));
					break;
				case ConstantValue.PROGRESS_CLOSE:
					// 关闭进度条
					PromptManager.closeProgressDialog(progressDialog);
					break;
				case ConstantValue.EXIT:
					// 退出系统
					PromptManager.showExitDialog(MainActivity.this);
					break;
			}
			super.handleMessage(msg);
		}

	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.il_main);

		init();
	}

	private void init() {
		RelativeLayout continer = (RelativeLayout) findViewById(R.id.ii_main_middle);
		UiManager2.getInstance().setContainer(continer);

		UiManager2.getInstance().addObserver(TopManager2.getInstrance());
		UiManager2.getInstance().addObserver(BottomManager2.getInstrance());
		
		TopManager2.getInstrance().init(this);
		BottomManager2.getInstrance().init(this);

		UiManager2.getInstance().changeView3(Hall.class, null, true);

		BaseView.setHandler(handler);
		progressDialog = new ProgressDialog(this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			boolean changeCacheView = UiManager2.getInstance().changeCacheView();
			if (changeCacheView == false) {
				PromptManager.showExitDialog(this);
			}
			return changeCacheView;
		}
		return super.onKeyDown(keyCode, event);
	}

}