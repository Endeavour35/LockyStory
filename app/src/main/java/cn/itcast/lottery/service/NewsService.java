package cn.itcast.lottery.service;

import java.util.List;

import android.graphics.Bitmap;

import cn.itcast.lottery.bean.News;
/**
 * 处理新闻(数据库及网络数据)
 * @author Administrator
 *
 */
public interface NewsService {
	/**
	 * 向数据库中写入数据
	 * @param item
	 * @return
	 */
	long insert(News item);
	/**
	 * 默认条目新闻初始化
	 */
	void init();
	/**
	 * 获取本地缓存新闻（最新的num条）
	 * @param num  获取新闻条目（小于等于零为获取本地全部条目）
	 * @return
	 */
	List<News> getLocalNews(int num);

	/**
	 * 获取网络新闻
	 * 
	 * @return
	 */
	List<News> getRemoteNews();
	/**
	 * 获取网络图片
	 * @param url
	 * @return
	 */
	Bitmap getRemoteImg(String url);
	/**
	 * 本地新闻核实
	 */
	void checkNews();

}
