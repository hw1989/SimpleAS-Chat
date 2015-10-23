package org.wind.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class OtherAdapter<T> extends BaseAdapter {

	protected ArrayList<T> list = null;
	protected Context context = null;
	protected LayoutInflater inflater = null;

	public OtherAdapter(Context context, ArrayList<T> list) {
		this.context = context;
		if (list == null) {
			this.list = new ArrayList<T>();
		} else {
			this.list = list;
		}
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public T getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	// 添加单个
	public void addItem(T t, int index) {
		if (t != null) {
			this.list.add(index, t);
			notifyDataSetChanged();
		}
	};

	// 添加多个记录
	public void addItems(ArrayList<T> t, int index) {
		if (t != null) {
			if (index > getCount() - 1 || index < 0) {
				this.list.addAll(t);
			} else {
				this.list.addAll(index, t);
			}

			notifyDataSetChanged();
		}
	};

	public T getLastItem() {
		if (this.list.size() > 0) {
			return getItem(getCount() - 1);
		} else {
			return null;
		}
	};

	public T getFristItem() {
		if (this.list.size() > 0) {
			return getItem(0);
		} else {
			return null;
		}
	};

	public ArrayList<T> getAllItem() {
		return this.list;
	}
}
