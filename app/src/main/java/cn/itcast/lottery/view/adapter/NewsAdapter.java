package cn.itcast.lottery.view.adapter;

import java.util.List;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.itcast.lottery.R;
import cn.itcast.lottery.bean.News;

/**
 * 将新闻信息填充到ListView
 * 
 * @author Administrator
 * 
 */
public class NewsAdapter extends BaseAdapter {
	private static final String TAG = "NewsAdapter";
	private List<News> data;
	private LayoutInflater inflater;

	

	public NewsAdapter(List<News> data, LayoutInflater inflater) {
		this.data = data;
		this.inflater = inflater;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewContiner continer = null;
		if (convertView == null) {
			Log.i(TAG, "new item:" + position);
			convertView = inflater.inflate(R.layout.il_news_row, null);
			continer = new ViewContiner();
			continer.imageView = (ImageView) convertView.findViewById(R.id.ii_news_img);
			continer.title = (TextView) convertView.findViewById(R.id.ii_news_title);
			continer.summary = (TextView) convertView.findViewById(R.id.ii_news_summary);
			convertView.setTag(continer);
		} else {
			Log.i(TAG, "old item:" + position);
			continer = (ViewContiner) convertView.getTag();
		}

		continer.imageView.setImageResource(R.drawable.icon);
		continer.title.setText(data.get(position).getTitle());
		continer.summary.setText(data.get(position).getSummary());

		return convertView;
	}

	class ViewContiner {
		ImageView imageView;
		TextView title;
		TextView summary;
	}

}
