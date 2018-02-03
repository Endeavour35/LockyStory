package cn.itcast.lottery.bean;
/**
 * 用户信息封装
 * @author Administrator
 *
 */
public class User {
	private String username;//用户账户名
	private String nickname;//用户妮称
	private String mail;//电子邮件
	private String phone;//联系电话
	private String actpassword;//账户安全密码
	private String recommentuser;//推荐用户名
	
	
	
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getActpassword() {
		return actpassword;
	}
	public void setActpassword(String actpassword) {
		this.actpassword = actpassword;
	}
	public String getRecommentuser() {
		return recommentuser;
	}
	public void setRecommentuser(String recommentuser) {
		this.recommentuser = recommentuser;
	}
	
	
	
}
