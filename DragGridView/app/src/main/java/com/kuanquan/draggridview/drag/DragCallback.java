package com.kuanquan.draggridview.drag;

public interface DragCallback {
	void startDrag(int position);
	void endDrag(int position);

	/**
	 *
	 * @param position
	 * @param isUp  true 表示可以删除
	 */
	void isDelete(int position, boolean isUp);
}
