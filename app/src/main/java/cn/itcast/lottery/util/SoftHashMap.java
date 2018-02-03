package cn.itcast.lottery.util;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.HashMap;

import android.accounts.Account;
import android.content.res.ColorStateList;
import android.util.Log;

/**
 * 通过软引用构建的Map集合
 * 
 * @author Administrator
 * 
 * @param <K>
 * @param <V>
 */
public class SoftHashMap<K, V> {
	private static final String TAG = "SoftHashMap";

	/**
	 * 存放被垃圾回收掉V的软引用对象
	 */
	private ReferenceQueue<V> queue = new ReferenceQueue<V>();

	private HashMap<K, SoftValue<K, V>> cache = new HashMap<K, SoftValue<K, V>>();

	/**
	 * 添加缓存
	 * 
	 * @param key
	 * @param value
	 */
	public void put(K key, V value) {
		cleanCache();
		cache.put(key, new SoftValue<K, V>(key, value, queue));
	}
	/**
	 * 获取到缓存的对象
	 * @param key
	 * @return
	 */
	public V get(K key) {
		cleanCache();
		SoftValue<K, V> softValue = cache.get(key);
		if (softValue != null) {
			return softValue.get();
		}
		return null;
	}

	/**
	 * 清除缓存，当软引用的对象被垃圾回收之后
	 */
	@SuppressWarnings("unchecked")
	private void cleanCache() {
		SoftValue<K, V> poll = null;
		while ((poll = (SoftValue<K, V>) queue.poll()) != null) {
			Log.i(TAG, poll.key.toString());
			cache.remove(poll.key);
		}
	}
	/**
	 * 判断当前缓存队列中是否还有指定的BaseView对象
	 * @param simpleName
	 * @return
	 */
	public boolean containsKey(K key)
	{
		cleanCache();
		return cache.containsKey(key);
	}
	
	/**
	 * 将强引用对象封装到SoftReference
	 * @author Administrator
	 *
	 * @param <K>
	 * @param <V>
	 */
	private class SoftValue<K, V> extends SoftReference<V> {
		private Object key;

		public SoftValue(K k, V v, ReferenceQueue<V> queue) {
			super(v, queue);
			this.key = k;
		}

	}
}
