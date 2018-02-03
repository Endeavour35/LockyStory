package cn.itcast.lottery.dao;

import cn.itcast.lottery.bean.News;
import cn.itcast.lottery.dao.base.DAO;

public interface NewsDao extends DAO<News> {
	/**
	 * 判断是否含有指定id的新闻内容
	 * @param guid
	 * @return
	 */
	boolean hasNews(String guid);
}
