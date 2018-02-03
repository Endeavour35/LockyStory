package cn.itcast.lottery.view;

import java.text.DecimalFormat;

import android.content.Context;
import android.os.Bundle;
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
import cn.itcast.lottery.util.PromptManager;
import cn.itcast.lottery.view.manager.TopManager2;
import cn.itcast.lottery.view.manager.UiManager2;

/**
 * 购物车界面
 * 
 * @author Administrator
 * 
 */
public class ShoppingTrolley extends BaseView {

	private TextView lotteryNumView;
	private TextView lotteryMoneyView;
	private ListView shoppingList;
	private ShoppingAdapter adapter;

	public ShoppingTrolley(Context context, Bundle bundle) {
		super(context, bundle);
	}

	@Override
	public Integer getId() {
		return ConstantValue.VIEW_SHOPPING_LIST;
	}

	@Override
	protected void init() {
		container = (ViewGroup) inflater.inflate(R.layout.il_shopping_trolley2, null);

		adapter = new ShoppingAdapter();
		shoppingList = (ListView) container.findViewById(R.id.ii_shopping_list);
		shoppingList.setAdapter(adapter);

		lotteryNumView = (TextView) container.findViewById(R.id.ii_shopping_lottery_num);
		lotteryMoneyView = (TextView) container.findViewById(R.id.ii_shopping_lottery_money);
		loadScheme();
	}

	@Override
	protected void setListener() {
		((Button) container.findViewById(R.id.ii_add_optional)).setOnClickListener(this);
		((Button) container.findViewById(R.id.ii_add_random)).setOnClickListener(this);
		((ImageButton) container.findViewById(R.id.ii_shopping_list_clear)).setOnClickListener(this);
		((Button) container.findViewById(R.id.ii_lottery_shopping_initiate_buy)).setOnClickListener(this);
		((Button) container.findViewById(R.id.ii_lottery_shopping_buy)).setOnClickListener(this);

	}

	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.ii_add_optional:
				UiManager2.getInstance().changeView3(PlaySSQ.class, null, false);
				break;
			case R.id.ii_add_random:
				// 机选号码一注
				ShoppingCart.getInstance().addOneRandomLottery();
				adapter.notifyDataSetChanged();
				loadScheme();
				break;
			case R.id.ii_shopping_list_clear:
				// 清空列表
				ShoppingCart.getInstance().clearTicket();
				adapter.notifyDataSetChanged();
				loadScheme();
				break;
			case R.id.ii_lottery_shopping_initiate_buy:
				// 发起合买
				Toast.makeText(context, R.string.is_error_undevelop, 1).show();
				break;
			case R.id.ii_lottery_shopping_buy:
				if (ShoppingCart.getInstance().getLotterynumber() <= 0) {
					Toast.makeText(context, R.string.is_error_shopping_null, 1).show();
				} else {
					// 检查登录情况
					if (ConfigParams.ISLOGIN == false) {
						PromptManager.showToast(context, context.getString(R.string.is_error_unlogin));
						UiManager2.getInstance().changeView3(UserLogin.class, null, false);
					} else {
						UiManager2.getInstance().changeView3(PerfectBetting.class, null, false);
					}
				}
				break;

		}
	}

	/**
	 * 注数与钱数的提示
	 */
	private void loadScheme() {
		DecimalFormat format = new DecimalFormat("####0.00");
		lotteryMoneyView.setText(format.format(ShoppingCart.getInstance().getLotteryvalue() / 100));
		lotteryNumView.setText(ShoppingCart.getInstance().getLotterynumber().toString());
	}

	@Override
	public void onResume() {
		String lotteryName = bundle.getString("lottery_name");
		String issue = bundle.getString("lottery_issue");
		TopManager2.getInstrance().setCommonTitle(lotteryName + "第" + issue + "期");
		
		loadScheme();
	}

	private class ShoppingAdapter extends BaseAdapter {
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
				convertView = inflater.inflate(R.layout.il_shopping_row, null);
				holder.deleteButton = (ImageButton) convertView.findViewById(R.id.si_shopping_item_delete);
				holder.redsTextView = (TextView) convertView.findViewById(R.id.si_shopping_item_reds);
				holder.bluesTextView = (TextView) convertView.findViewById(R.id.si_shopping_item_blues);
				holder.moneyDetailsTextView = (TextView) convertView.findViewById(R.id.si_shopping_item_money);
				convertView.setTag(holder);
				
			} else {
				holder = (Holder) convertView.getTag();
				
			}

			holder.redsTextView.setText(ShoppingCart.getInstance().getTickets().get(position).showReds());
			holder.bluesTextView.setText(ShoppingCart.getInstance().getTickets().get(position).showBlues());
			holder.moneyDetailsTextView.setText(ShoppingCart.getInstance().getTickets().get(position).getMoneyDetails());
			final int mPosition = position;
			holder.deleteButton.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					ShoppingCart.getInstance().deleteTicket(mPosition);
					notifyDataSetChanged();
					loadScheme();
				}
			});

			return convertView;
		}

		private class Holder {
			ImageButton deleteButton;
			TextView redsTextView;
			TextView bluesTextView;
			TextView moneyDetailsTextView;
		}

	}

}
