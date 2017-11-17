package com.yangsuni.baserecyclerviewadapter.listener;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Gesture ClickListener
 * Created by yangsuni on 2016-08-22.
 */
public class GestureClickListener extends GestureDetector.SimpleOnGestureListener {

	private final OnItemClickListener mOnItemClickListener;
	private View view;
	private int viewPosition;

	public GestureClickListener(OnItemClickListener onItemClickListener) {
		this.mOnItemClickListener = onItemClickListener;

	}

	public void setTouchedView(final View view, final int position) {
		this.view = view;
		this.viewPosition = position;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		return mOnItemClickListener.onItemClick(view, viewPosition);
	}

	@Override
	public void onLongPress(MotionEvent e) {
		mOnItemClickListener.onItemLongClick(view, viewPosition);
	}

	@Override
	public boolean onDoubleTap(MotionEvent e) {
		return mOnItemClickListener.onItemDoubleClick(view, viewPosition);
	}

}
