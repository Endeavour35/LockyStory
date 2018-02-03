package cn.itcast.lottery.util;

import java.util.Random;

public class CalculatorUtils {

	/**
	 * 计算在start(包括)和end(包括)之间的不重复的随机数。
	 * 
	 * @param start
	 *            起始数大于等于零
	 * @param end
	 *            终止数
	 * @return int
	 */
	public static int getRandomNumber(int start, int end) {
		end++;
		Random random = new Random();
		int result = -1;

		if (start == 0) {
			result = random.nextInt(end);
		} else {
			result = random.nextInt(end);
			if (result < start) {
				result = result + start;
			}
		}
		return result;
	}

	/**
	 * 计算n个数的组合值
	 * 
	 * @param total
	 * @param k
	 * @return
	 */
	public static int calculatorCombination(int n, int r) {
		int result = factorial(n, n - r + 1) / factorial(r, 1);
		return result;
	}

	/**
	 * 计算n个数的排列值
	 * 
	 * @param total
	 * @param k
	 * @return
	 */
	public static long calculatorPermutation(int n, int r) {
		long result = factorial(n, 1) / factorial(n - r, 1);
		return result;
	}

	/**
	 * 计算start到end之间数字的乘积（含start和end）
	 * @param start(大值)
	 * @param end（小值）
	 * @return
	 */
	public static int factorial(int start, int end) {
		if (start < 0) {
			throw new IllegalArgumentException("x must be>=0");
		}
		if (start == end) {
			return end;
		} else if (start > end)
			return start * factorial(start - 1, end);
		else
			throw new IllegalArgumentException("start must be >= end");

	}

}