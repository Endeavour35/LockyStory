package cn.itcast.lottery.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.lang3.StringUtils;
import org.xmlpull.v1.XmlPullParser;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.Xml;
import cn.itcast.lottery.ConstantValue;
import cn.itcast.lottery.bean.News;
import cn.itcast.lottery.dao.DBHelper;
import cn.itcast.lottery.dao.NewsDao;
import cn.itcast.lottery.net.HttpClientAdapter;
import cn.itcast.lottery.service.NewsService;
import cn.itcast.lottery.util.BeanFactory;
import cn.itcast.lottery.util.LogUtil;

public class NewsServiceImpl implements NewsService {
	private Context context;
	private NewsDao dao;

	public NewsServiceImpl(Context context) {
		super();
		this.context = context;
		dao = BeanFactory.getImpl(NewsDao.class, context);
	}

	@Override
	public void checkNews() {
		// 判断是否大于新闻保存的极限值
		if (dao.getCount() >= ConstantValue.NEWS_NUM) {
			// 获取新闻条目，保存最新的指定条目信息
			List<News> list = dao.queryByCondition(null, null, DBHelper.TABLE_NEW_DATE_COL + " asc", "0,"
					+ (ConstantValue.NEWS_NUM - ConstantValue.NEWS_SHOW_NUM));
			// 删除这些新闻
			for (News news : list) {
				dao.delete(news.getId());
			}
			// 删除新闻对应的图片信息
			deleteImg(list);
		}
	}

	/**
	 * 删除新闻图片
	 * 
	 * @param list
	 */
	private void deleteImg(List<News> list) {
		Set<String> fileName = new HashSet<String>();
		for (News news : list) {
			fileName.add(news.getGuid());
		}

		File dir = context.getDir(ConstantValue.NEWS_PATH, Context.MODE_PRIVATE);
		if (dir.list() != null) {
			File[] listFiles = dir.listFiles();
			for (File item : listFiles) {
				String name = item.getName();
				name = name.substring(0, name.indexOf("."));
				if (fileName.contains(name))
					item.delete();
			}

		}
	}

	/**
	 * 删除新闻图片
	 * 
	 * @param list
	 */
	private void deleteImg2(List<News> list) {
		list = getLocalNews(-1);
		Set<String> fileName = new HashSet<String>();
		for (News news : list) {
			fileName.add(news.getGuid());
		}

		File dir = context.getDir(ConstantValue.NEWS_PATH, Context.MODE_PRIVATE);
		if (dir.list() != null) {
			File[] listFiles = dir.listFiles();
			for (File item : listFiles) {
				String name = item.getName();
				name = name.substring(0, name.indexOf("."));
				if (fileName.contains(name))
					continue;
				item.delete();
			}

		}
	}

	@Override
	public List<News> getLocalNews(int num) {
		List<News> result;
		if (num > 0) {
			result = dao.queryByCondition(null, null, DBHelper.TABLE_NEW_DATE_COL + " desc", "0," + num);
		} else {
			result = dao.queryAll();
		}

		return result;
	}

	@Override
	public Bitmap getRemoteImg(String url) {
		return null;
	}

	@Override
	public List<News> getRemoteNews() {
		CopyOnWriteArrayList<News> list = null;

		HttpClientAdapter adapter = new HttpClientAdapter();
		InputStream inputStream = adapter.sendGetRequest(ConstantValue.URL_NEWS);
		if (inputStream != null) {
			// 解析xml
			list = parserNews(inputStream);
			// 写入数据库
			for (News item : list) {
				long insert = insert(item);
				if (insert <= 0) {
					//当手机端已经有
					list.remove(item);
				}
			}

			return list;
		}

		return null;
	}

	/**
	 * 解析服务器恢复的新闻信息
	 * 
	 * @param inputStream
	 * @return
	 */
	private CopyOnWriteArrayList<News> parserNews(InputStream inputStream) {
		CopyOnWriteArrayList<News> list;
		XmlPullParser parser = Xml.newPullParser();

		list = new CopyOnWriteArrayList<News>();
		News item = null;
		String pubDate = "";
		try {
			parser.setInput(inputStream, "UTF-8");
			int eventType = parser.getEventType();
			String name = "";
			while (eventType != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
					case XmlPullParser.START_TAG:
						name = parser.getName();
						if (name.equalsIgnoreCase("pubDate")) {
							if (StringUtils.isBlank(pubDate)) {
								pubDate = parser.nextText();
							}
						}
						if (name.equalsIgnoreCase("item")) {
							item = new News();
							if (StringUtils.isNotBlank(pubDate))
								item.setDate(pubDate);
						}
						if (name.equalsIgnoreCase("guid")) {
							item.setGuid(parser.nextText());
						}
						if (name.equalsIgnoreCase("title")) {
							item.setTitle(parser.nextText());
						}
						if (name.equalsIgnoreCase("link")) {
							String link = parser.nextText();
							if (StringUtils.isNotBlank(link)) {
								if (link.contains("../")) {
									link = link.replaceFirst("../", ConstantValue.URL_NEWS_REPLACE);
								}
								while (link.contains("../")) {
									link = link.replace("../", "");
								}
								item.setLink(link);
							}
						}
						if (name.equalsIgnoreCase("summary")) {
							item.setSummary(parser.nextText());
						}
						if (name.equalsIgnoreCase("logo")) {
							String imgUrl = parser.nextText();
							if (StringUtils.isNotBlank(imgUrl)) {
								if (imgUrl.contains("../")) {
									imgUrl = imgUrl.replaceFirst("../", ConstantValue.URL_NEWS_REPLACE);
								}
								while (imgUrl.contains("../")) {
									imgUrl = imgUrl.replace("../", "");
								}
								if (imgUrl.equals(ConstantValue.URL_NEWS_REPLACE)) {
								} else {
									item.setImgUrl(imgUrl);
								}
							}
						}
						if (name.equalsIgnoreCase("author")) {
							item.setAuthor(parser.nextText());
						}
						if (name.equalsIgnoreCase("source")) {
							item.setSource(parser.nextText());
						}
						break;
					case XmlPullParser.END_TAG:
						name = parser.getName();
						if (name.equalsIgnoreCase("item")) {
							if (item != null) {
								list.add(item);
								item = null;
							}
						}
						break;
				}
				eventType = parser.next();
			}
		} catch (Exception e) {
			LogUtil.info(NewsServiceImpl.class, Log.getStackTraceString(e));
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}

	@Override
	public void init() {
		News item = new News();
		item.setTitle("错失双色球4.5亿 揽701万...");
		item
				.setSummary("2月29日一大早，江西省福利彩票中心兑奖室的工作人员正在感叹，2月28日晚江西又中了一注双色球一等奖时，一位戴着阿迪毛线帽的中年男子淡定地走了进来，称自己就是昨晚福彩双色球第2012023期的大奖得主。经过工作人员核实，该男子姓高(化名)，其持有的一张168元的“9+1”复式彩票确实中得双色球第2012023期一等奖一注");
		item.setLink(ConstantValue.URL_NEWS_ITEM);
		item.setImgUrl("http://wap.zhcw.com/upload/Image/mrtp/1_22546498898.jpg");
		item.setGuid("2254652");
		item.setDate("2012-03-02 11:33:05");

		dao.insert(item);

		item = new News();
		item.setTitle("生意人自选5注号坚守15年终中...");
		item.setSummary("深圳彩民连揽双色球足金大奖，继第12018期一小伙以手机投注定制2元机选撞中1000万元后，仅隔两期，一位买彩长达15年的生意人又以10元自选票捧走1000万元奖金，2月27日上午，大奖得主孙先生（化名）现身福彩中心兑奖。");
		item.setLink(ConstantValue.URL_NEWS_ITEM);
		item.setImgUrl("http://wap.zhcw.com/upload/Image/mrtp/1_22533004169.jpg");
		item.setGuid("2253302");
		item.setDate("2012-03-02 11:33:05");

		dao.insert(item);

		item = new News();
		item.setTitle("3D上周对子号“808”登台唱...");
		item.setSummary("43% 据中彩网数据统计，3D游戏上周（第2012044-2012050期）周销量4.2多亿元，同比小幅攀升2.43%。上周平均期销量6118万多元");
		item.setLink(ConstantValue.URL_NEWS_ITEM);
		item.setImgUrl("http://wap.zhcw.com/upload/Image/mrtp/1_22526115308.jpg");
		item.setGuid("2252613");
		item.setDate("2012-03-02 11:33:05");

		dao.insert(item);
	}

	@Override
	public long insert(News item) {
		// 判断item信息
		if (item != null && StringUtils.isNotBlank(item.getGuid())) {
			// 判断手机本地是否含有该信息
			if (!dao.hasNews(item.getGuid())) {
				// 不含该条信息时添加到本地库
				return dao.insert(item);
			}
		}
		return 0;
	}

}
