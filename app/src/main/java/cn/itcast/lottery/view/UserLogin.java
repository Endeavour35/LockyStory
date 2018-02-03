package cn.itcast.lottery.view;

import org.apache.commons.lang3.StringUtils;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import cn.itcast.lottery.ConfigParams;
import cn.itcast.lottery.ConstantValue;
import cn.itcast.lottery.R;
import cn.itcast.lottery.bean.User;
import cn.itcast.lottery.net.protocol.Message;
import cn.itcast.lottery.net.protocol.Oelement;
import cn.itcast.lottery.net.protocol.element.BalanceElement;
import cn.itcast.lottery.service.UserService;
import cn.itcast.lottery.util.BeanFactory;
import cn.itcast.lottery.util.PreferenceManager;
import cn.itcast.lottery.util.PromptManager;
import cn.itcast.lottery.view.manager.UiManager2;

public class UserLogin extends BaseView {
	private ImageView clearView;
	private EditText username;
	private EditText password;

	public UserLogin(Context context, Bundle bundle) {
		super(context, bundle);
	}

	@Override
	public Integer getId() {
		return ConstantValue.VIEW_USER_LOGIN;
	}

	@Override
	protected void init() {
		container = (ViewGroup) inflater.inflate(R.layout.il_user_login, null);

		clearView = (ImageView) container.findViewById(R.id.clear);
		username = (EditText) container.findViewById(R.id.ii_user_login_username);
		password = (EditText) container.findViewById(R.id.ii_user_login_password);

	}

	@Override
	protected void setListener() {
		clearView.setOnClickListener(this);
		container.findViewById(R.id.ii_user_login_regist).setOnClickListener(this);
		container.findViewById(R.id.ii_user_login).setOnClickListener(this);

		((CheckBox) container.findViewById(R.id.ii_user_login_auto_login)).setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				PreferenceManager.changeAutoLogin(context, isChecked);
			}
		});
		username.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			public void afterTextChanged(Editable s) {

				if (username.getText().toString().length() > 0) {
					clearView.setVisibility(View.VISIBLE);
				} else {
					clearView.setVisibility(View.INVISIBLE);
				}

			}
		});

	}

	public void onClick(View v) {

		switch (v.getId()) {
			case R.id.clear:
				username.setText("");
				break;
			case R.id.ii_user_login_regist:
				PreferenceManager.changeAutoLogin(context, false);
				UiManager2.getInstance().changeView3(UserRegiste.class, null, false);
				break;
			case R.id.ii_user_login:
				String checkInfo = checkInfo();
				if (StringUtils.isBlank(checkInfo)) {
					// 用户登陆，获取用户账户余额
					new HttpTask<Message>() {
						@Override
						protected void onPreExecute() {
							handler.sendEmptyMessage(ConstantValue.PROGRESS_OPEN);
						}

						@Override
						protected Message doInBackground(String... params) {
							Message result = null;
							User user = new User();
							user.setUsername(params[0]);
							user.setActpassword(params[1]);

							UserService service = BeanFactory.getImpl(UserService.class);
							result = service.login(user);

							// 处理用户登陆回复
							if (result != null) {
								String errorcode = result.getBody().getOelement().getErrorcode();
								String errormsg = result.getBody().getOelement().getErrormsg();

								// 判断登陆状态
								if (ConstantValue.SUCCESS.equals(errorcode)) {
									// 保存用户信息
									if (PreferenceManager.checkAutoLogin(context)) {
										PreferenceManager.changeUserInfo(context, user.getUsername(), user.getActpassword());
									}

									// 设置登录状态
									ConfigParams.ISLOGIN = true;
									ConfigParams.USERNAME = username.getText().toString();

									// 获取账户信息
									result = service.balance(user);
									return result;
								} else {
									// 服务器处理失败，提示用户失败原因
									PromptManager.showToast(context, errormsg);
								}
							} else {
								// 处理失败，网络原因
								PromptManager.showToast(context, R.string.is_error_network);
							}

							return null;
						}

						@Override
						protected void onPostExecute(Message result) {
							handler.sendEmptyMessage(ConstantValue.PROGRESS_CLOSE);
							if (result != null) {
								Oelement oelement = result.getBody().getOelement();
								String errorcode = oelement.getErrorcode();
								String errormsg = oelement.getErrormsg();

								// 判断余额是否获取成功
								if (ConstantValue.SUCCESS.equals(errorcode)) {
									BalanceElement element = (BalanceElement) result.getBody().getElements().get(0);
									// 获取到余额信息
									if (StringUtils.isNotBlank(element.getInvestvalues())) {
										ConfigParams.INVESTVALUES = Double.parseDouble(element.getInvestvalues());
										// 页面处理(用户登陆、购彩提示用户登陆)
										// BaseView current = UiManager2.getInstance().getCurrentView();
										// if (current.getClass() == Hall.class)
										// UiManager2.getInstance().changeView3(Hall.class, null, true);
										// else {
										UiManager2.getInstance().changeCacheView();
										// }
									}
								} else {
									// 服务器处理失败，提示用户失败原因
									PromptManager.showToast(context, errormsg);
								}
							} else {
								// 处理失败，网络原因
								PromptManager.showToast(context, R.string.is_error_network);
							}
						}

					}.executeProxy(username.getText().toString(), password.getText().toString());
				} else {
					Toast.makeText(context, checkInfo, Toast.LENGTH_LONG).show();
				}
				break;
		}

	}

	private String checkInfo() {
		String result = "";
		if (StringUtils.isBlank(username.getText())) {
			result = context.getResources().getString(R.string.is_error_userlogin_username_null);
		} else if (StringUtils.isBlank(password.getText())) {
			result = context.getResources().getString(R.string.is_error_userlogin_password_null);
		}
		return result;

	}

}
