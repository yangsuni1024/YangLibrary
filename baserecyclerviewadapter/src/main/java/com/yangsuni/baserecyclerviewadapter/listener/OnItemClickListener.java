package com.yangsuni.baserecyclerviewadapter.listener;

import android.view.View;

/**
 * Gesture OnItemClickListener
 * Created by yangsuni on 2016-08-22.
 */
public interface OnItemClickListener {

	boolean onItemClick(View view, int position);

	boolean onItemLongClick(View view, int position);

	boolean onItemDoubleClick(View view, int position);
}
