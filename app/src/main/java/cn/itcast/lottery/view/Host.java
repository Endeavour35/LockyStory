package cn.itcast.lottery.view;

import java.util.List;

import cn.itcast.lottery.ConstantValue;
import cn.itcast.lottery.R;
import cn.itcast.lottery.bean.News;
import cn.itcast.lottery.net.protocol.Message;
import cn.itcast.lottery.service.NewsService;
import cn.itcast.lottery.util.BeanFactory;
import cn.itcast.lottery.util.PromptManager;
import cn.itcast.lottery.view.adapter.NewsAdapter;
import cn.itcast.lottery.view.manager.UiManager2;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 首页
 * 
 * @author Administrator
 * 
 */
public class Host extends BaseView {
	private ListView listView;
	private NewsAdapter newsAdapter;
	private List<News> data;
	private NewsService newsService;

	public Host(Context context, Bundle bundle) {
		super(context, bundle);
	}

	@Override
	public Integer getId() {
		return ConstantValue.VIEW_HOST;
	}

	@Override
	protected void init() {
		container = (ViewGroup) inflater.inflate(R.layout.il_host, null);
		listView = (ListView) container.findViewById(R.id.ii_host_list);

		newsService = BeanFactory.getImpl(NewsService.class, context);
		data = newsService.getLocalNews(ConstantValue.NEWS_SHOW_NUM);

		if (data != null || data.size() > 0) {
			newsService.init();
			data = newsService.getLocalNews(ConstantValue.NEWS_SHOW_NUM);
		}

		newsAdapter = new NewsAdapter(data, inflater);

		listView.setAdapter(newsAdapter);

	}

	@Override
	public void onResume() {
		new HttpTask<List<News>>() {

			@Override
			protected List<News> doInBackground(String... params) {
				NewsService service = BeanFactory.getImpl(NewsService.class, context);
				return service.getRemoteNews();
			}

			@Override
			protected void onPostExecute(List<News> result) {
				if (result != null && !result.isEmpty()) {
					// 如果返回的结果不为空说明有新的新闻信息添加到本地数据库中，需要在返回结果的集合后面添加上当前显示的新闻内容
					result.addAll(data);

					// 截取需要显示的内容部分
					data.clear();
					data.addAll(result.subList(0, ConstantValue.NEWS_SHOW_NUM));
					newsAdapter.notifyDataSetChanged();
				}
				super.onPostExecute(result);
			}

		}.executeProxy("");
	}

	@Override
	protected void setListener() {
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				News item = (News) parent.getItemAtPosition(position);
				String link = item.getLink();
				Bundle bundle = new Bundle();
				bundle.putString("link", link);
				UiManager2.getInstance().changeView3(NewsDetail.class, bundle, false);
			}
		});

	}

}
