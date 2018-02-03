package cn.itcast.lottery.view;

import org.apache.commons.lang3.StringUtils;

import android.content.Context;
import android.database.CursorJoiner.Result;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import cn.itcast.lottery.ConstantValue;
import cn.itcast.lottery.R;
import cn.itcast.lottery.bean.ShoppingCart;
import cn.itcast.lottery.net.protocol.Message;
import cn.itcast.lottery.net.protocol.Oelement;
import cn.itcast.lottery.net.protocol.element.CurrentIssueElement;
import cn.itcast.lottery.service.CommonService;
import cn.itcast.lottery.util.BeanFactory;
import cn.itcast.lottery.util.LogUtil;
import cn.itcast.lottery.util.PromptManager;
import cn.itcast.lottery.view.manager.UiManager2;
/**
 * 购彩大厅
 * @author Administrator
 *
 */
public class Hall extends BaseView {

	/************** 彩种摘要（需要到服务器获取信息） ****************/
	private TextView ssqSummary;
	private TextView sdSummary;
	private TextView qlcSummary;
	/************************************************************/
	/************** 处理购彩入口的按钮 *********************/
	private ImageView ssqBet;
	private ImageView sdBet;
	private ImageView qlcBet;

	/****************************************************/
	/*************** 封装获取到的当前销售期数据 ***********************/
	private Bundle ssqBundle;
	private Bundle sdBundle;
	private Bundle qlcBundle;

	public Hall(Context context, Bundle bundle) {
		super(context, bundle);
	}

	@Override
	public Integer getId() {
		return ConstantValue.VIEW_HALL;
	}

	@Override
	protected void init() {
		container = (ViewGroup) inflater.inflate(R.layout.il_hall, null);

		ssqSummary = (TextView) container.findViewById(R.id.ii_hall_ssq_summary);
		sdSummary = (TextView) container.findViewById(R.id.ii_hall_3d_summary);
		qlcSummary = (TextView) container.findViewById(R.id.ii_hall_qlc_summary);

		ssqBet = (ImageView) container.findViewById(R.id.ii_hall_ssq_bet);
		sdBet = (ImageView) container.findViewById(R.id.ii_hall_3d_bet);
		qlcBet = (ImageView) container.findViewById(R.id.ii_hall_qlc_bet);

	}

	@Override
	protected void setListener() {
		ssqBet.setOnClickListener(this);
		sdBet.setOnClickListener(this);
		qlcBet.setOnClickListener(this);
	}

	@Override
	public void onResume() {
		// 加载彩种当前销售期信息
		new HttpTask<Message>() {

			@Override
			protected Message doInBackground(String... params) {
				CommonService commonService = BeanFactory.getImpl(CommonService.class);
				return commonService.getCurrentIssueInfo(params[0]);
			}

			@Override
			protected void onPostExecute(Message result) {
				if (result != null) {
					// 获取服务器返回的处理状态信息
					Oelement oelement = result.getBody().getOelement();
					// 判断服务器是否处理成功
					if (ConstantValue.SUCCESS.equals(oelement.getErrorcode())) {
						CurrentIssueElement element = (CurrentIssueElement) result.getBody().getElements().get(0);
						changeSummary(element);
					} else {
						PromptManager.showToast(context, oelement.getErrormsg());
					}
				} else {
					// 未获取到服务器回复信息
					PromptManager.showToast(context, R.string.is_error_network);
				}
				super.onPostExecute(result);
			}

		}.executeProxy(String.valueOf(ConstantValue.SSQ));

		ShoppingCart.getInstance().clear();
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.ii_hall_ssq_bet:
				UiManager2.getInstance().changeView3(PlaySSQ.class, ssqBundle, true);
				break;
			case R.id.ii_hall_3d_bet:
				break;
			case R.id.ii_hall_qlc_bet:
				break;
		}
	}

	/**
	 * 改变彩种的摘要显示
	 */
	public void changeSummary(CurrentIssueElement element) {
		if (element != null) {
			String lotteryId = element.getLotteryid().getValue();
			if (StringUtils.isNotBlank(lotteryId) && StringUtils.isNumeric(lotteryId)) {
				// 获取需要替换的文字信息
				String text = context.getString(R.string.is_hall_common_summary);
				text = StringUtils.replaceEach(text, new String[] { "ISSUE", "TIME" }, new String[] { element.getIssue(),
						getLasttime(element.getLasttime()) });

				switch (Integer.parseInt(lotteryId)) {
					case ConstantValue.SSQ:
						ssqSummary.setText(text);

						// 封装数据传递到写一个界面
						ssqBundle = new Bundle();
						ssqBundle.putInt("lottery_id", ConstantValue.SSQ);
						ssqBundle.putString("lottery_name", element.getLotteryname());
						ssqBundle.putString("lottery_issue", element.getIssue());
						ssqBundle.putString("lottery_lasttime", element.getLasttime());
						break;

				}
			}
		} else {
			LogUtil.info(Hall.class, "parser current issue info error");
		}
	}

	/**
	 * 将秒时间转换成日时分格式
	 * 
	 * @param lasttime
	 * @return
	 */
	public String getLasttime(String lasttime) {
		StringBuffer result = new StringBuffer();
		if (StringUtils.isNumericSpace(lasttime)) {
			int time = Integer.parseInt(lasttime);
			int day = time / (24 * 60 * 60);
			result.append(day).append("天");
			if (day > 0) {
				time = time - day * 24 * 60 * 60;
			}
			int hour = time / 3600;
			result.append(hour).append("时");
			if (hour > 0) {
				time = time - hour * 60 * 60;
			}
			int minute = time / 60;
			result.append(minute).append("分");
		}
		return result.toString();
	}
}
