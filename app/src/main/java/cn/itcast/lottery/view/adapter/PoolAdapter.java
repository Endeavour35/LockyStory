package cn.itcast.lottery.view.adapter;

import java.text.DecimalFormat;
import java.util.concurrent.CopyOnWriteArrayList;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.itcast.lottery.R;
/**
 * 选号池
 * @author Administrator
 *
 */
public class PoolAdapter extends BaseAdapter {
	private int startNum;
	private int endNum;
	private Context context;

	private CopyOnWriteArrayList<Integer> pool;
	private int selectRes;

	public PoolAdapter(Context context, int startNum, int endNum, CopyOnWriteArrayList<Integer> pool, int selectRes) {
		super();
		this.context = context;
		this.startNum = startNum;
		this.endNum = endNum;
		this.pool = pool;
		this.selectRes = selectRes;
	}

	@Override
	public int getCount() {
		if (endNum <= startNum)
			throw new RuntimeException("The end num must more then the start num.");
		return endNum - startNum + 1;
	}

	@Override
	public Object getItem(int position) {
		return position + startNum;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView ball = null;
		if (convertView == null) {
			// 定义一个TextView做为球展示的载体
			ball = new TextView(context);
			ball.setBackgroundResource(R.drawable.id_defalut_ball);// 设置背景图片
			ball.setGravity(Gravity.CENTER);// 内容居中显示
			ball.setTextColor(R.color.black_slight);// 设置字体颜色
		} else {
			ball = (TextView) convertView;
		}

		DecimalFormat format = new DecimalFormat("00");

		ball.setText(String.valueOf(format.format((position + startNum))));// 设置显示的内容
//		ball.setBackgroundResource(R.drawable.id_defalut_ball);
		//处理机选号码
		if (pool.contains((position + startNum))) {
			ball.setBackgroundResource(selectRes);
		} else
			ball.setBackgroundResource(R.drawable.id_defalut_ball);
		return ball;
	}

}
