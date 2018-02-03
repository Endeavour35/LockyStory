package cn.itcast.lottery.view.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;

public abstract class ShakeSensor implements SensorEventListener {

	// 摇晃手机选号相关变量
	private long curTime;
	private long lastTime;
	// 最后记录的缓存
	private float lastX;
	private float lastY;
	private float lastZ;
	// 单次振幅
	private float shake;
	// 总的振幅
	private float totalShake;

	private Context mContext;
	private Vibrator mVibrator;

	public ShakeSensor(Context context) {
		initShake();
		mContext = context;
	}

	/**
	 * 初始化
	 */
	private void initShake() {
		curTime = 0;
		lastTime = 0;
		lastX = 0;
		lastY = 0;
		lastZ = 0;
		shake = 0.0f;
		totalShake = 0.0f;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (Sensor.TYPE_ACCELEROMETER == event.sensor.getType()) {
			// 获取加速度传感器的三个参数
			float x = event.values[SensorManager.DATA_X];
			float y = event.values[SensorManager.DATA_Y];
			float z = event.values[SensorManager.DATA_Z];
			// 获取当前时刻毫秒数
			curTime = System.currentTimeMillis();
			// 100毫秒检测一次
			float duration = curTime - lastTime;
			if (duration > 100) {
				// 判断是否是刚开始晃动
				if (lastX == 0.0f && lastY == 0.0f && lastZ == 0.0f) {
					// last_x last_y last_z同时为0时，表示刚刚开始
				} else {
					// 单次晃动振幅
					shake = (Math.abs(x - lastX) + Math.abs(y - lastY) + Math.abs(z - lastZ));

				}
				totalShake += shake;

				lastX = x;
				lastY = y;
				lastZ = z;
				lastTime = curTime;

				// 判断是否为晃动
				if (totalShake > 200) {
					// 摇晃手机生成一注随机彩票。。。
					createNewLottery();
					initShake();
					onVibrator();
				}

			}
		}

	}

	/**
	 * 创建一注新号码
	 */
	public abstract void createNewLottery();

	/**
	 * 震动
	 */
	public void onVibrator() {
		if (mVibrator == null) {
			mVibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
		}
		mVibrator.vibrate(100L);
	}

}
