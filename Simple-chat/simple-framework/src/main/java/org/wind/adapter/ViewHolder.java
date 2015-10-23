package org.wind.adapter;

import android.util.SparseArray;
import android.view.View;

/**
 * 简便的 ViewHolder
 * ViewHolder()适应BaseExpandableListAdapter
 * getInstance()适应BaseAdapter
 */
public class ViewHolder {
	private static ViewHolder instance = null;
	private SparseArray<View> array = null;
	private View childView = null;
    
	public ViewHolder() {
	}

	 public static ViewHolder getInstance() {
	 if (instance == null) {
	 instance = new ViewHolder();
	 }
	 return instance;
	 }

	/**
	 * @param view
	 *            item对应的view
	 * @param id
	 *            控件id
	 * @return 为控件的对象
	 */
	public <T extends View> T getView(View view, int id) {
		array = (SparseArray<View>) view.getTag();
		if (array == null) {
			array = new SparseArray<View>();
			view.setTag(array);
		}
		childView = array.get(id);
		if (childView == null) {
			childView = view.findViewById(id);
			array.put(id, childView);
		}
		return (T) childView;
	}
}
