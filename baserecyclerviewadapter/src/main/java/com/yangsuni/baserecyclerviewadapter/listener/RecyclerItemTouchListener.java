package com.yangsuni.baserecyclerviewadapter.listener;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * https://github.com/thesurix/gesture-recycler
 *
 * RecyclerView OnItemTouchListener RecyclerItemTouchListener
 * Created by yangsuni on 2016-08-22.
 */
public class RecyclerItemTouchListener extends RecyclerView.SimpleOnItemTouchListener {

	private final GestureClickListener mGestureClickListener;
	private final GestureDetector mGestureDetector;

	public RecyclerItemTouchListener(Context context, OnItemClickListener onItemClickListener) {
		mGestureClickListener = new GestureClickListener(onItemClickListener);
		mGestureDetector = new GestureDetector(context, mGestureClickListener);
	}

	@Override
	public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
		if (rv.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
			return false;
		}
		final View itemView = rv.findChildViewUnder(e.getX(), e.getY());
		if (itemView != null) {
			final int itemPosition = rv.getChildAdapterPosition(itemView);
			mGestureClickListener.setTouchedView(itemView, itemPosition);

			return mGestureDetector.onTouchEvent(e);
		}

		return false;
	}

}
