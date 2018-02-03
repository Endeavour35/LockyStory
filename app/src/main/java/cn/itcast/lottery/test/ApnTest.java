package cn.itcast.lottery.test;

import cn.itcast.lottery.util.NetUtil;
import android.test.AndroidTestCase;

public class ApnTest extends AndroidTestCase {
	public void getApn()
	{
		NetUtil.setApnProxyInfo(getContext());
	}
}
