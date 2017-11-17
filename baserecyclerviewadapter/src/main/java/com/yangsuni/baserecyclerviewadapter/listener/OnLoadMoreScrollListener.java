package com.yangsuni.baserecyclerviewadapter.listener;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

/**
 * https://gist.github.com/nesquena/d09dc68ff07e845cc622
 * Created by yangsuni on 2016-08-13.
 */
public abstract class OnLoadMoreScrollListener extends RecyclerView.OnScrollListener {

	private static final int DEFAULT_HOLD_SIZE = 10;
	private int mHoldSize;
	private int firstVisibleItem, visibleItemCount, totalItemCount;

	private RecyclerView.LayoutManager mLayoutManager;
	private LinearLayoutManager mLinearLayoutManager;
	private StaggeredGridLayoutManager mStaggeredGridLayoutManager;
	private int[] mInto;

	public OnLoadMoreScrollListener(RecyclerView.LayoutManager layoutManager) {
		this(layoutManager, DEFAULT_HOLD_SIZE);
	}

	public OnLoadMoreScrollListener(RecyclerView.LayoutManager layoutManager, int holdSize) {
		this.mLayoutManager = layoutManager;
		this.mHoldSize = holdSize;

		if (layoutManager instanceof LinearLayoutManager) {
			this.mLinearLayoutManager = (LinearLayoutManager) layoutManager;
		} else if (layoutManager instanceof StaggeredGridLayoutManager) {
			this.mStaggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
		}
	}

	@Override
	public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
		// Scroll Up Event block
		if (dy < 0) return;

		visibleItemCount = recyclerView.getChildCount();
		totalItemCount = mLayoutManager.getItemCount();

		if (mLinearLayoutManager != null) {
			firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();
		} else if (mStaggeredGridLayoutManager != null) {
			mInto = mStaggeredGridLayoutManager.findFirstVisibleItemPositions(null);

			int tempInto = 0;
			for (int into : mInto) {
				if (tempInto == 0) {
					tempInto = into;
				} else if (into < tempInto) {
					tempInto = into;
				}
			}

			firstVisibleItem = tempInto;
		}

		if (totalItemCount == 0 || visibleItemCount == 0) {
			return;
		}

		if ((firstVisibleItem + mHoldSize) >= (totalItemCount - visibleItemCount)
				|| totalItemCount <= visibleItemCount) {
			onLoadMore();
		}

	}

	@Override
	public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
		super.onScrollStateChanged(recyclerView, newState);
	}

	/**
	 * This Method very frequently called....
	 */
	public abstract void onLoadMore();

}
