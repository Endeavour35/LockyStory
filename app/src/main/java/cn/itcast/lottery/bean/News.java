package cn.itcast.lottery.bean;

import cn.itcast.lottery.dao.DBHelper;
import cn.itcast.lottery.dao.annotation.Column;
import cn.itcast.lottery.dao.annotation.ID;
import cn.itcast.lottery.dao.annotation.Table;


/**
 * 新闻实体
 * 
 * @author caorui201314@163.com
 * 
 */
@Table(DBHelper.TABLE_NEW)
public class News {
	@ID(autoIncrement=true)
	@Column(DBHelper.TABLE_ID_COL)
	private long id;// 唯一标示
	@Column(DBHelper.TABLE_NEW_TITLE_COL)
	private String title;//新闻标题
	@Column(DBHelper.TABLE_NEW_SUMMARY_COL)
	private String summary;//新闻摘要
	@Column(DBHelper.TABLE_NEW_LINK_COL)
	private String link;//新闻链接
	@Column(DBHelper.TABLE_NEW_AUTHOR_COL)
	private String author;//作者
	@Column(DBHelper.TABLE_NEW_SOURCE_COL)
	private String source;//来源
	@Column(DBHelper.TABLE_NEW_IMGURL_COL)
	private String imgUrl;//新闻图片地址
	@Column(DBHelper.TABLE_NEW_DATE_COL)
	private String date;//发布时间
	@Column(DBHelper.TABLE_NEW_GUID_COL)
	private String guid;//服务器端资源唯一标示
	@Column(DBHelper.TABLE_NEW_RESERVED1_COL)
	private String reserved1;//扩展字段1
	@Column(DBHelper.TABLE_NEW_RESERVED2_COL)
	private String reserved2;//扩展字段2

	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getReserved1() {
		return reserved1;
	}

	public void setReserved1(String reserved1) {
		this.reserved1 = reserved1;
	}

	public String getReserved2() {
		return reserved2;
	}

	public void setReserved2(String reserved2) {
		this.reserved2 = reserved2;
	}

	public News() {
	}

}
