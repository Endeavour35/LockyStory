package cn.itcast.lottery.view;

import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.lang3.StringUtils;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;
import cn.itcast.lottery.ConstantValue;
import cn.itcast.lottery.R;
import cn.itcast.lottery.bean.ShoppingCart;
import cn.itcast.lottery.bean.Ticket;
import cn.itcast.lottery.net.protocol.Message;
import cn.itcast.lottery.net.protocol.Oelement;
import cn.itcast.lottery.net.protocol.element.CurrentIssueElement;
import cn.itcast.lottery.service.CommonService;
import cn.itcast.lottery.util.BeanFactory;
import cn.itcast.lottery.util.CalculatorUtils;
import cn.itcast.lottery.util.PromptManager;
import cn.itcast.lottery.view.adapter.PoolAdapter;
import cn.itcast.lottery.view.manager.BottomManager2;
import cn.itcast.lottery.view.manager.TopManager2;
import cn.itcast.lottery.view.manager.UiManager2;
import cn.itcast.lottery.view.sensor.ShakeSensor;

/**
 * 双色球选号界面
 * 
 * @author Administrator
 * 
 */
public class PlaySSQ extends BaseView implements PlayGame {
	/************* 机选按钮 ***************/
	private Button redRandom;
	private Button blueRandom;
	/************* 选号区 **************/
	private GridView redGridView;
	private GridView blueGridView;

	/************* 为选号设置adapter **************/
	private PoolAdapter redAdapter;
	private PoolAdapter blueAdapter;
	/************* 用户已选号码容器 *******************/
	private CopyOnWriteArrayList<Integer> redPool;
	private CopyOnWriteArrayList<Integer> bluePool;

	public PlaySSQ(Context context, Bundle bundle) {
		super(context, bundle);
	}

	@Override
	public Integer getId() {
		return ConstantValue.VIEW_PLAY_SSQ;
	}

	@Override
	protected void init() {
		container = (ViewGroup) inflater.inflate(R.layout.il_play_ssq, null);

		redRandom = (Button) container.findViewById(R.id.ii_ssq_random_red);
		blueRandom = (Button) container.findViewById(R.id.ii_ssq_random_blue);

		redGridView = (GridView) container.findViewById(R.id.ii_ssq_red_number_container);
		blueGridView = (GridView) container.findViewById(R.id.ii_ssq_blue_number_container);

		redPool = new CopyOnWriteArrayList<Integer>();
		redAdapter = new PoolAdapter(context, 1, 33, redPool, R.drawable.id_redball);
		redGridView.setAdapter(redAdapter);

		bluePool = new CopyOnWriteArrayList<Integer>();
		blueAdapter = new PoolAdapter(context, 1, 16, bluePool, R.drawable.id_blueball);
		blueGridView.setAdapter(blueAdapter);

	}

	@Override
	protected void setListener() {
		redRandom.setOnClickListener(this);
		blueRandom.setOnClickListener(this);

		redGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (redPool.contains(redAdapter.getItem(position))) {
					// 改变球的背景图片
					view.setBackgroundResource(R.drawable.id_defalut_ball);
					redPool.remove(redAdapter.getItem(position));
				} else {
					// 改变球的背景图片
					view.setBackgroundResource(R.drawable.id_redball);
					// 给选中的球加动画效果
					view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.shake));
					redPool.add(Integer.parseInt(redAdapter.getItem(position).toString()));
				}
				// 核实当前选号情况
				checkNum();
			}
		});

		blueGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (bluePool.contains(blueAdapter.getItem(position))) {
					// 改变球的背景图片
					view.setBackgroundResource(R.drawable.id_defalut_ball);
					bluePool.remove(blueAdapter.getItem(position));
				} else {
					// 改变球的背景图片
					view.setBackgroundResource(R.drawable.id_blueball);
					// 给选中的球加动画效果
					view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.shake));
					bluePool.add(Integer.parseInt(blueAdapter.getItem(position).toString()));
				}
				// 核实当前选号情况
				checkNum();
			}
		});

	}

	@Override
	public void onResume() {
		// 设置标题内容
		setTitle();
		// 清除当前选择的号码
		clearSelected();
		// register();
		super.onResume();
	}

	/**
	 * 设置标题内容
	 */

	private void setTitle() {
		if (bundle != null) {
			String issue = bundle.getString("lottery_issue");
			String title = StringUtils.replace(context.getResources().getString(R.string.is_ssq_title), "ISSUE", issue);
			TopManager2.getInstrance().setCommonTitle(title);
		} else {
			new HttpTask<Message>() {
				@Override
				protected Message doInBackground(String... params) {
					CommonService commonService = BeanFactory.getImpl(CommonService.class);
					return commonService.getCurrentIssueInfo(params[0]);
				}

				@Override
				protected void onPostExecute(Message result) {
					handler.sendEmptyMessage(ConstantValue.PROGRESS_CLOSE);
					if (result != null) {
						Oelement oelement = result.getBody().getOelement();
						CurrentIssueElement element = (CurrentIssueElement) result.getBody().getElements().get(0);
						if (ConstantValue.SUCCESS.equals(oelement.getErrorcode())) {
							bundle = new Bundle();

							bundle.putInt("lottery_id", ConstantValue.SSQ);
							// 封装数据传递到写一个界面
							bundle.putString("lottery_name", element.getLotteryname());
							bundle.putString("lottery_issue", element.getIssue());
							bundle.putString("lottery_lasttime", element.getLasttime());
							setTitle();
						} else {
							PromptManager.showToast(context, oelement.getErrormsg());
						}
					} else {
						PromptManager.showToast(context, R.string.is_error_network);
					}
					super.onPostExecute(result);
				}

			}.executeProxy(String.valueOf(ConstantValue.SSQ));
		}
	}

	@Override
	public void onPause() {
		// unRegister();
		super.onPause();
	}

	/*************** 摇晃手机随机生成一组投注号码 ******************/
	private ShakeSensor shakeSensor;
	private SensorManager mSensorManager;

	/*********************** 传感器管理 ***************************/
	private void register() {
		mSensorManager=(SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		shakeSensor = new ShakeSensor(context) {
			@Override
			public void createNewLottery() {
				PlaySSQ.this.createNewLottery(6, 1);
			}

		};
		mSensorManager.registerListener(shakeSensor, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST);

	}

	private void unRegister() {
		// 取消传感器监听
		mSensorManager.unregisterListener(shakeSensor);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.ii_ssq_random_red:
				createNewLottery(6, 0);
				break;
			case R.id.ii_ssq_random_blue:
				createNewLottery(0, 1);
				break;
		}
	}

	/**
	 * 机选指定数目的红球和蓝球
	 * 
	 * @param redNum
	 * @param blueNum
	 */
	private void createNewLottery(int redNum, int blueNum) {
		if (redNum != 0)
			redPool.clear();
		if (blueNum != 0)
			bluePool.clear();
		if (redNum > 33 || blueNum > 16) {
			return;
		}
		while (redPool.size() < redNum) {
			int temp;
			if (!redPool.contains(temp = CalculatorUtils.getRandomNumber(1, 33))) {
				redPool.add(temp);
			}
		}
		while (bluePool.size() < blueNum) {
			int temp;
			if (!bluePool.contains(temp = CalculatorUtils.getRandomNumber(1, 16))) {
				bluePool.add(temp);
			}
		}
		if (redNum != 0)
			redAdapter.notifyDataSetChanged();
		if (blueNum != 0)
			blueAdapter.notifyDataSetChanged();
		checkNum();
	}

	/**
	 * 核实当前选号情况
	 */
	protected void checkNum() {
		// 红球选号提示
		// 蓝球选号提示
		// 注数和钱数提示

		String text = context.getString(R.string.is_ssq_default_notice);
		if (redPool.size() < 6) {
			text = StringUtils.replace(context.getString(R.string.is_need_to_chose), "NUM", String.valueOf((6 - redPool.size())))
					+ context.getString(R.string.is_ball_red);
		} else {
			if (bluePool.size() >= 1) {
				// 算注数
				Integer[] reds = new Integer[redPool.size()];
				Integer[] blues = new Integer[bluePool.size()];
				redPool.toArray(reds);
				bluePool.toArray(blues);
				long num = calculatorBoxes(reds, blues);
				if (num != -1) {
					text = StringUtils.replaceEach(context.getResources().getString(R.string.is_play_bottom_title), new String[] { "NUM", "MONEY" },
							new String[] { String.valueOf(num), String.valueOf(2 * num) });
					// text = "   共   " + num + "  注      " + (2 * num) + "  元      ";
				}
			} else {
				// 提示应该选择蓝球了
				text = StringUtils.replace(context.getString(R.string.is_need_to_chose), "NUM", String.valueOf(1))
						+ context.getString(R.string.is_ball_blue);
			}
		}
		BottomManager2.getInstrance().changeGameBottomNotice(text);

	}

	/**
	 * 算当前的注数
	 */
	public static int calculatorBoxes(Integer[] red, Integer[] blue) {
		if (!isValid(red, blue)) {
			return -1;
		}
		int rednum = CalculatorUtils.calculatorCombination(red.length, 6);
		int bluenum = CalculatorUtils.calculatorCombination(blue.length, 1);
		return rednum * bluenum;
	}

	public static boolean isValid(Integer[] red, Integer[] bule) {
		if (red.length == 0 || bule.length == 0)
			return false;
		if (red.length >= 6 && bule.length >= 1) {
			return true;
		}
		return false;
	}

	@Override
	public void addSelected2ShoppingList() {
		if (bundle != null) {
			Integer[] reds = new Integer[redPool.size()];
			Integer[] blues = new Integer[bluePool.size()];
			redPool.toArray(reds);
			bluePool.toArray(blues);
			// 计算注数
			int num = calculatorBoxes(reds, blues);
			if (num == -1) {
				PromptManager.showToast(context, R.string.is_error_ssq_chose);
			} else {
				// 创建一个投注信息
				Ticket ticket = new Ticket(reds, blues);
				ticket.setLotterynumber(num);
				ticket.setLotteryId(bundle.getInt("lottery_id"));

				ShoppingCart.getInstance().setIssue(bundle.getString("lottery_issue"));
				if (StringUtils.isBlank(ShoppingCart.getInstance().getLotteryid()))
					ShoppingCart.getInstance().setLotteryid(String.valueOf(ticket.getLotteryId()));
				ShoppingCart.getInstance().addTicket(ticket);

				// 跳转到购物车界面
				UiManager2.getInstance().changeView3(ShoppingTrolley.class, bundle, true);
			}
		} else {
			AsyncTask<String, Integer, Message> executeProxy = new HttpTask<Message>() {
				@Override
				protected Message doInBackground(String... params) {
					CommonService commonService = BeanFactory.getImpl(CommonService.class);
					return commonService.getCurrentIssueInfo(params[0]);
				}

				@Override
				protected void onPostExecute(Message result) {
					handler.sendEmptyMessage(ConstantValue.PROGRESS_CLOSE);
					if (result != null) {
						Oelement oelement = result.getBody().getOelement();
						CurrentIssueElement element = (CurrentIssueElement) result.getBody().getElements().get(0);
						if (ConstantValue.SUCCESS.equals(oelement.getErrorcode())) {
							bundle = new Bundle();

							bundle.putInt("lottery_id", ConstantValue.SSQ);
							// 封装数据传递到写一个界面
							bundle.putString("lottery_name", element.getLotteryname());
							bundle.putString("lottery_issue", element.getIssue());
							bundle.putString("lottery_lasttime", element.getLasttime());
							setTitle();
						} else {
							PromptManager.showToast(context, oelement.getErrormsg());
						}
					} else {
						PromptManager.showToast(context, R.string.is_error_network);
					}
					super.onPostExecute(result);
				}

			}.executeProxy(String.valueOf(ConstantValue.SSQ));
			if (executeProxy != null) {
				handler.sendEmptyMessage(ConstantValue.PROGRESS_OPEN);
			}
		}

	}

	@Override
	public void clearSelected() {
		redPool.clear();
		bluePool.clear();

		redAdapter.notifyDataSetChanged();
		blueAdapter.notifyDataSetChanged();

		String text = context.getString(R.string.is_ssq_default_notice);
		BottomManager2.getInstrance().changeGameBottomNotice(text);
	}

}
