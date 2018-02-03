package cn.itcast.lottery.view;

import java.text.DecimalFormat;

import org.apache.commons.lang3.StringUtils;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.itcast.lottery.ConfigParams;
import cn.itcast.lottery.ConstantValue;
import cn.itcast.lottery.R;
import cn.itcast.lottery.bean.ShoppingCart;
import cn.itcast.lottery.bean.User;
import cn.itcast.lottery.net.protocol.Message;
import cn.itcast.lottery.net.protocol.Oelement;
import cn.itcast.lottery.net.protocol.element.BettingElement;
import cn.itcast.lottery.service.UserService;
import cn.itcast.lottery.util.BeanFactory;
import cn.itcast.lottery.util.PromptManager;
import cn.itcast.lottery.view.manager.UiManager2;

/**
 * 设置注数和追期的页面
 * 
 * @author Administrator
 * 
 */
public class PerfectBetting extends BaseView implements OnClickListener {

	protected static final String TAG = "PerfectBetting";
	private ListView ShoppingCartList;

	private ShoppingCartAdapter adapter;

	private TextView lotteryNumView;
	private TextView lotteryMoneyView;

	// 注数
	private Button addAppnumbers;
	private TextView appnumbers;
	private Button subAppnumbers;
	// 期号
	private Button addIssueflagNum;
	private TextView issueflagNum;
	private Button subIssueflagNum;

	private ImageButton betButton;

	public PerfectBetting(Context context, Bundle bundle) {
		super(context, bundle);
	}

	protected void setListener() {
		addIssueflagNum.setOnClickListener(this);
		addAppnumbers.setOnClickListener(this);
		subIssueflagNum.setOnClickListener(this);
		subAppnumbers.setOnClickListener(this);
		betButton.setOnClickListener(this);
	}

	protected void init() {
		container = (ViewGroup) inflater.inflate(R.layout.il_play_prefectbetting, null);

		ShoppingCartList = (ListView) container.findViewById(R.id.ii_lottery_shopping_list);
		lotteryNumView = (TextView) container.findViewById(R.id.ii_shopping_list_betting_num);
		lotteryMoneyView = (TextView) container.findViewById(R.id.ii_shopping_list_betting_money);
		addAppnumbers = (Button) container.findViewById(R.id.ii_add_appnumbers);
		subAppnumbers = (Button) container.findViewById(R.id.ii_sub_appnumbers);
		addIssueflagNum = (Button) container.findViewById(R.id.ii_add_issueflagNum);
		subIssueflagNum = (Button) container.findViewById(R.id.ii_sub_issueflagNum);

		appnumbers = (TextView) container.findViewById(R.id.ii_appnumbers);
		issueflagNum = (TextView) container.findViewById(R.id.ii_issueflagNum);

		betButton = (ImageButton) container.findViewById(R.id.ii_lottery_purchase);

		adapter = new ShoppingCartAdapter();
		ShoppingCartList.setAdapter(adapter);

		loadScheme();
	}

	/**
	 * 注数和钱数及余额的显示入口
	 */
	private void loadScheme() {
		DecimalFormat format = new DecimalFormat("####0.00");
		String numInfo = StringUtils.replaceEach(context.getString(R.string.is_shopping_list_betting_num), new String[] { "n%" },
				new String[] { ShoppingCart.getInstance().getLotterynumber().toString() });

		String moneyInfo = StringUtils.replaceEach(context.getString(R.string.is_shopping_list_betting_money), new String[] { "m1%", "m2%" },
				new String[] { format.format(ShoppingCart.getInstance().getLotteryvalue() / 100), ConfigParams.INVESTVALUES.toString() });

		lotteryMoneyView.setText(Html.fromHtml(moneyInfo));
		lotteryNumView.setText(Html.fromHtml(numInfo));
	}

	@Override
	public void onResume() {
		super.onResume();
		loadScheme();
		appnumbers.setText("1");
		issueflagNum.setText("1");
		ShoppingCart.getInstance().setAppnumbers(1);
		ShoppingCart.getInstance().setIssuesnumbers(1);

		// 判断当前选号与用户余额
		int lotteryvalue = ShoppingCart.getInstance().getLotterynumber() * ShoppingCart.BASEMONEY * ShoppingCart.getInstance().getAppnumbers()
				* ShoppingCart.getInstance().getLotterynumber();
		if (lotteryvalue > ConfigParams.INVESTVALUES) {
			PromptManager.showToast(context, R.string.is_no_enough_money);
		}
	}

	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.ii_add_appnumbers:
				changeText(1, false);
				break;
			case R.id.ii_sub_appnumbers:
				changeText(-1, false);
				break;
			case R.id.ii_add_issueflagNum:
				changeText(1, true);
				break;
			case R.id.ii_sub_issueflagNum:
				changeText(-1, true);
				break;
			case R.id.ii_lottery_purchase:
				// 投注
				new HttpTask<Message>() {

					@Override
					protected Message doInBackground(String... params) {
						User user = new User();
						user.setUsername(ConfigParams.USERNAME);
						UserService service = BeanFactory.getImpl(UserService.class);
						return service.betting(user);
					}

					@Override
					protected void onPostExecute(Message result) {
						String message = "";
						if (result != null) {
							Oelement oelement = result.getBody().getOelement();
							if (ConstantValue.SUCCESS.equals(oelement.getErrorcode())) {
								BettingElement element = (BettingElement) result.getBody().getElements().get(0);
								String tradevalue = element.getTradevalue();
								if (StringUtils.isNumeric(tradevalue)) {
									ConfigParams.INVESTVALUES = ConfigParams.INVESTVALUES - Double.parseDouble(tradevalue);
									UiManager2.getInstance().clearBackCache();
									ShoppingCart.getInstance().getTickets().clear();
									UiManager2.getInstance().changeView3(Hall.class, null, true);
									message = context.getString(R.string.is_betting_result_success) + tradevalue
											+ context.getString(R.string.is_money_unit);
								}
							} else {
								message = oelement.getErrormsg();
							}
						}
						if (StringUtils.isBlank(message)) {
							message = context.getString(R.string.is_error_betting_failure);
						}
						PromptManager.showErrorDialog(context, context.getString(R.string.is_betting_result), message);
					}

				}.executeProxy("");
				break;
		}

	}

	/**
	 * 改变当前textview的数量显示
	 * 
	 * @param num
	 * @param isIssue
	 */
	private void changeText(int num, boolean isIssue) {
		TextView text = null;
		if (isIssue) {
			text = issueflagNum;
		} else {
			text = appnumbers;
		}

		Integer temp = 0;
		if (StringUtils.isNotBlank(text.getText())) {
			if (StringUtils.isNumeric(text.getText())) {
				temp = Integer.parseInt(text.getText().toString());
				temp = temp + num;
			}
		}

		if (temp <= 0) {
			temp = 1;
		}

		if (temp > 99) {
			temp = 99;
		}

		changeMoney(isIssue, text, temp);
	}

	/**
	 * 改变关联的钱数显示
	 * 
	 * @param isIssue
	 * @param text
	 * @param temp
	 */
	private void changeMoney(boolean isIssue, TextView text, Integer temp) {
		// 判断钱数
		Integer lotteryvalue = 0;
		if (isIssue) {
			lotteryvalue = ShoppingCart.getInstance().getLotterynumber() * ShoppingCart.BASEMONEY * ShoppingCart.getInstance().getAppnumbers() * temp;
		} else {
			lotteryvalue = ShoppingCart.getInstance().getLotterynumber() * ShoppingCart.BASEMONEY * temp* ShoppingCart.getInstance().getIssuesnumbers();
		}
		if (lotteryvalue < ConfigParams.INVESTVALUES) {
			if (isIssue) {
				ShoppingCart.getInstance().setIssuesnumbers(temp);
			} else {
				ShoppingCart.getInstance().setAppnumbers(temp);
			}
			loadScheme();
			text.setText(temp.toString());
		} else {
			Toast.makeText(context, R.string.is_no_enough_money, Toast.LENGTH_SHORT).show();
		}
	}

	private class ShoppingCartAdapter extends BaseAdapter {
		public int getCount() {
			return ShoppingCart.getInstance().getTickets().size();
		}

		public Object getItem(int position) {
			return ShoppingCart.getInstance().getTickets().get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			Holder holder = null;
			if (convertView == null) {
				holder = new Holder();
				convertView = inflater.inflate(R.layout.il_play_prefectbetting_row, null);
				holder.redsTextView = (TextView) convertView.findViewById(R.id.ii_shopping_item_reds);
				holder.bluesTextView = (TextView) convertView.findViewById(R.id.ii_shopping_item_blues);
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}

			holder.redsTextView.setText(ShoppingCart.getInstance().getTickets().get(position).showReds());
			holder.bluesTextView.setText(ShoppingCart.getInstance().getTickets().get(position).showBlues());

			return convertView;
		}

		private class Holder {
			TextView redsTextView;
			TextView bluesTextView;
		}

	}

	@Override
	public Integer getId() {
		return ConstantValue.VIEW_PREFECTBETTING;
	}

}