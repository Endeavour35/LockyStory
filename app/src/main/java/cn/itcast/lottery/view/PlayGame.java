package cn.itcast.lottery.view;
/**
 * 玩法需要实现的接口
 * @author Administrator
 *
 */
public interface PlayGame {
	/**
	 * 清除当前选择号码
	 */
	void clearSelected();
	/**
	 * 添加选号到购物车
	 */
	void addSelected2ShoppingList();
}
