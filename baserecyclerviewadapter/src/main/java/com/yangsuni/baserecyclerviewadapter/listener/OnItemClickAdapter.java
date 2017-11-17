package com.yangsuni.baserecyclerviewadapter.listener;

import android.view.View;

/**
 * Gesture OnItemClickListener
 * Created by yangsuni on 2016-08-22.
 */
public class OnItemClickAdapter implements OnItemClickListener {

	@Override
	public boolean onItemClick(View view, int position) {
		return false;
	}

	@Override
	public boolean onItemLongClick(View view, int position) {
		return false;
	}

	@Override
	public boolean onItemDoubleClick(View view, int position) {
		return false;
	}
}
