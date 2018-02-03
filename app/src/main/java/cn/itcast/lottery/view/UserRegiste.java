package cn.itcast.lottery.view;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import cn.itcast.lottery.ConfigParams;
import cn.itcast.lottery.ConstantValue;
import cn.itcast.lottery.R;
import cn.itcast.lottery.bean.User;
import cn.itcast.lottery.net.protocol.Message;
import cn.itcast.lottery.net.protocol.element.UserRegisteElement;
import cn.itcast.lottery.service.UserService;
import cn.itcast.lottery.util.BeanFactory;
import cn.itcast.lottery.util.PreferenceManager;
import cn.itcast.lottery.util.PromptManager;
import cn.itcast.lottery.view.manager.UiManager2;

/**
 * 用户注册界面管理
 * 
 * @author Administrator
 * 
 */
public class UserRegiste extends BaseView {
	private EditText phoneEditText;
	private EditText passwordEditText;
	private EditText repasswordEditText;
	private CheckBox protocolCheckBox;

	private String userName;
	private String password;

	public UserRegiste(Context context, Bundle bundle) {
		super(context, bundle);
	}

	@Override
	public Integer getId() {
		return ConstantValue.VIEW_USER_REGISTE;
	}

	@Override
	protected void init() {
		container = (ViewGroup) inflater.inflate(R.layout.il_user_regist, null);

		phoneEditText = (EditText) container.findViewById(R.id.ii_regist_phone);
		passwordEditText = (EditText) container.findViewById(R.id.ii_regist_password);
		repasswordEditText = (EditText) container.findViewById(R.id.ii_regist_repassword);
		protocolCheckBox = (CheckBox) container.findViewById(R.id.ii_regist_protocol_check);

	}

	@Override
	protected void setListener() {
		container.findViewById(R.id.ii_login).setOnClickListener(this);
		container.findViewById(R.id.ii_regist).setOnClickListener(this);

	}

	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.ii_login:
				UiManager2.getInstance().changeView3(UserLogin.class, null, false);
				break;
			case R.id.ii_regist:
				// 检查用户填写情况
				if (!protocolCheckBox.isChecked()) {
					PromptManager.showToast(context, R.string.is_regist_protocol_error);
				} else {
					String checkInfo = checkInfo();
					if (StringUtils.isNotBlank(checkInfo)) {
						PromptManager.showToast(context, checkInfo);
					} else {
						// 调用service完成注册操作
						// 执行耗时操作在子线程内完成
						new HttpTask<Message>() {
							@Override
							protected void onPreExecute() {
								handler.sendEmptyMessage(ConstantValue.PROGRESS_OPEN);
								super.onPreExecute();
							}

							@Override
							protected Message doInBackground(String... params) {
								// 完成注册操作
								User user = new User();
								user.setActpassword(params[0]);
								user.setUsername(params[1]);

								UserService service = BeanFactory.getImpl(UserService.class);
								Message registe = service.registe(user);
								return registe;
							}

							@Override
							protected void onPostExecute(Message result) {
								handler.sendEmptyMessage(ConstantValue.PROGRESS_CLOSE);
								boolean failure = true;
								if (result != null) {
									if (ConstantValue.SUCCESS.equals(result.getBody().getOelement().getErrorcode())) {
										// 保存用户信息
										ConfigParams.USERNAME = userName;
										ConfigParams.ISLOGIN = true;
										UserRegisteElement element = (UserRegisteElement) result.getBody().getElements().get(0);

										ConfigParams.INVESTVALUES = Double.valueOf(element.getActvalue());
										// 页面跳转
										UiManager2.getInstance().changeView3(Hall.class, null, false);
										PromptManager.showToast(context, R.string.is_regist_success);
										failure = false;
									}
								}

								if (failure) {
									// 提示用户注册失败
									String title = "注册失败";
									String msg = "";

									if (result != null) {
										msg = result.getBody().getOelement().getErrormsg();
									}
									PromptManager.showErrorDialog(context, title, msg);

								}
								super.onPostExecute(result);
							}
						}.executeProxy(password, userName);
					}
				}
				break;
		}

	}

	/**
	 * 检查用户输入信息
	 * 
	 * @return
	 */
	private String checkInfo() {
		userName = phoneEditText.getText().toString();
		password = passwordEditText.getText().toString();
		String repassword = repasswordEditText.getText().toString();

		boolean result = false;

		if (StringUtils.isNotBlank(userName)) {
			if (userName.length() == 11) {
				result = true;
			}
		}
		if (!result) {
			phoneEditText.setText("");
			return context.getString(R.string.is_regist_phone_error);
		}
		result = false;
		if (StringUtils.isNotBlank(password)) {
			if (password.length() >= 6 && password.length() <= 15) {
				Pattern p = Pattern.compile("^[a-zA-Z0-9]*$");
				Matcher m = p.matcher(userName);
				if (m.matches()) {
					result = true;
				}
			}

		}
		if (!result) {
			passwordEditText.setText("");
			return context.getString(R.string.is_regist_password_error);
		}
		result = false;
		if (password.equals(repassword)) {
			result = true;
		}
		if (!result) {
			repasswordEditText.setText("");
			return context.getString(R.string.is_regist_password_error);
		}

		return "";

	}
}
