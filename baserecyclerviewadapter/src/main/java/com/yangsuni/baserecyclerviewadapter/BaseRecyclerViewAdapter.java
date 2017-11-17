package com.yangsuni.baserecyclerviewadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.yangsuni.baserecyclerviewadapter.listener.OnItemClickListener;
import com.yangsuni.baserecyclerviewadapter.listener.RecyclerItemTouchListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by yhyuns1024 on 2017. 11. 17..
 */
public abstract class BaseRecyclerViewAdapter<T, V extends View> extends RecyclerView.Adapter<ViewWrapper> implements OnItemClickListener {

	protected List<T> mDataItems = new ArrayList<>();

	public static final int TYPE_HEADER = Integer.MAX_VALUE;
	public static final int TYPE_FOOTER = Integer.MIN_VALUE;
	public static final int TYPE_NORMAL = 0;

	private List<ViewWrapper> mHeaderViews = new ArrayList<>();
	private List<ViewWrapper> mFooterViews = new ArrayList<>();

	protected Context mContext;

	protected boolean isApiLoading;

	private RecyclerItemTouchListener mRecyclerItemTouchListener;
	private OnItemClickListener mOnItemClickListener;

	protected RecyclerView mRecyclerView;

	protected BaseRecyclerViewAdapter(Context context) {
		this.mContext = context;
	}


	@Override
	public void onAttachedToRecyclerView(RecyclerView recyclerView) {
		super.onAttachedToRecyclerView(recyclerView);
		this.mRecyclerView = recyclerView;
		if (mRecyclerItemTouchListener == null) {
			mRecyclerItemTouchListener = new RecyclerItemTouchListener(mContext, this);
			recyclerView.addOnItemTouchListener(mRecyclerItemTouchListener);
		}
	}

	@Override
	public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
		super.onDetachedFromRecyclerView(recyclerView);
		if (mRecyclerItemTouchListener != null) {
			recyclerView.removeOnItemTouchListener(mRecyclerItemTouchListener);
			mRecyclerItemTouchListener = null;
		}
	}

	@Override
	public int getItemViewType(int position) {
		int headerCount = getHeaderCount();
		if (position < headerCount)
			return TYPE_HEADER - position;
		else if (position < headerCount + getDataCount())
			return TYPE_NORMAL;
		else
			return TYPE_FOOTER + position - headerCount - getDataCount();

	}

	@Override
	public ViewWrapper onCreateViewHolder(ViewGroup viewGroup, int viewType) {
		ViewWrapper viewWrapper;
		if (viewType > TYPE_HEADER - getHeaderCount()) {
			viewWrapper = mHeaderViews.get(TYPE_HEADER - viewType);
		} else if (viewType < TYPE_FOOTER + getFooterCount()) {
			viewWrapper = mFooterViews.get(viewType - TYPE_FOOTER);
		} else {
			viewWrapper = new ViewWrapper(onCreateItemView(viewGroup, viewType));
		}

		return viewWrapper;
	}

	protected abstract V onCreateItemView(ViewGroup parent, int viewType);

	protected abstract void onBindViewWrapper(ViewWrapper viewHolder, int position);

	protected void onBindHeaderViewWrapper(ViewWrapper viewHolder, int position) {
	}

	protected void onBindFooterViewWrapper(ViewWrapper viewHolder, int position) {
	}

	@Override
	public void onBindViewHolder(ViewWrapper viewHolder, int position) {
		int headerViewSize = mHeaderViews.size();
		int footerViewSize = mFooterViews.size();

		if (position < headerViewSize) {
			onBindHeaderViewWrapper(viewHolder, position);
		} else if (position < headerViewSize + mDataItems.size()) {
			onBindViewWrapper(viewHolder, position - headerViewSize);
		} else {
			onBindFooterViewWrapper(viewHolder, position - headerViewSize - footerViewSize);
		}
	}

	public boolean isApiLoading() {
		return isApiLoading;
	}

	/**
	 * Add a static view to appear at the start of the RecyclerView. Headers are displayed in the
	 * order they were added.
	 *
	 * @param view The header view to add
	 */
	public void addHeaderView(View view) {
		mHeaderViews.add(new ViewWrapper(view));
		notifyItemInserted(mHeaderViews.size() - 1);
	}

	/**
	 * @param position : the index at which to insertData.
	 * @param view     : The header view to add
	 */
	public void addHeaderView(int position, View view) {
		mHeaderViews.add(position, new ViewWrapper(view));
		notifyItemInserted(position);
	}

	/**
	 * Add a static view to appear at the end of the RecyclerView. Footers are displayed in the
	 * order they were added.
	 *
	 * @param view The footer view to add
	 */
	public void addFooterView(View view) {
		mFooterViews.add(new ViewWrapper(view));
		notifyItemInserted(getItemCount() - 1);
	}

	@Override
	public int getItemCount() {
		return getHeaderCount() + getFooterCount() + getDataCount();
	}

	/**
	 * @return The item count in the underlying adapter
	 */
	public int getDataCount() {
		return mDataItems.size();
	}

	/**
	 * Get the data item associated with the specified position in the data set.
	 *
	 * @param position Position of the item whose data we want within the adapter's
	 *                 data set.
	 * @return The data at the specified position.
	 */
	public T getItem(int position) {
		if (position < 0 || position >= mDataItems.size()) {
			return null;
		}
		return mDataItems.get(position);
	}

	public ArrayList<T> getItemArray() {
		return (ArrayList<T>) mDataItems;
	}

	/**
	 * @return The number of header views added
	 */
	public int getHeaderCount() {
		return mHeaderViews.size();
	}

	/**
	 * @return The number of footer views added
	 */
	public int getFooterCount() {
		return mFooterViews.size();
	}

	/**
	 * @param view 찾을 headerView
	 * @return
	 */
	public View getHeaderView(View view) {
		for (int i = 0, size = mHeaderViews.size(); i < size; i++) {
			View headerView = mHeaderViews.get(i).itemView;
			if (view.getClass().isInstance(headerView)) {
				return headerView;
			}
		}

		return null;
	}

	/**
	 * @param position 찾을 headerView
	 * @return headerView
	 */
	public View getHeaderView(int position) {
		if (position < mHeaderViews.size()) {
			return mHeaderViews.get(position).itemView;
		}
		return null;
	}

	/**
	 * @param view 찾을 footerView
	 * @return
	 */
	public View getFooterView(View view) {
		for (int i = 0, size = mFooterViews.size(); i < size; i++) {
			View footerView = mFooterViews.get(i).itemView;
			if (footerView == view) {
				return footerView;
			}
		}

		return null;
	}

	/**
	 * @param position 찾을 footerView
	 * @return
	 */
	public View getFooterView(int position) {
		if (position < mFooterViews.size()) {
			return mFooterViews.get(position).itemView;
		}
		return null;
	}

	// region add
	public void itemAdd(T item) {
		mDataItems.add(item);
		notifyItemInserted(mDataItems.size() + mHeaderViews.size());
	}

	public void itemAdd(int position, T item) {
		mDataItems.add(position, item);
		notifyItemInserted(position + mHeaderViews.size());
	}

	public void itemAddArray(Collection<T> collection) {
		if (collection == null || collection.isEmpty()) return;
		int preCount = mDataItems.size();
		mDataItems.addAll(collection);
		notifyItemRangeInserted(preCount + mHeaderViews.size(), collection.size());
	}

	public void itemAddArray(int location, Collection<T> collection) {
		if (collection == null || collection.isEmpty()) return;
		mDataItems.addAll(location, collection);
		notifyItemRangeInserted(location + mHeaderViews.size(), collection.size());
	}
	// endregion add

	// region change
	public T itemChange(T t) {
		int index = mDataItems.indexOf(t);
		if (index != -1) {
			mDataItems.set(index, t);
			notifyItemChanged(index + mHeaderViews.size());
		}
		return null;
	}

	public T itemChange(int location, T t) {
		T item = mDataItems.set(location, t);
		notifyItemChanged(location + mHeaderViews.size());
		return item;
	}

	public void itemChange(int fromPosition, int toPosition) {
		T fromItem = mDataItems.get(fromPosition);
		T toItem = mDataItems.get(toPosition);

		mDataItems.set(fromPosition, toItem);
		mDataItems.set(toPosition, fromItem);
//		Collections.swap(mDataItems, fromPosition, toPosition);
		notifyItemRangeChanged(fromPosition + mHeaderViews.size(), toPosition + mHeaderViews.size());
	}

	// endregion change

	// region remove
	public T itemRemove(T item) {
		int index = mDataItems.indexOf(item);
		return itemRemove(index);
	}

	public T itemRemove(int position) {
		if (position < 0 || position >= mDataItems.size()) {
			return null;
		}
		T item = mDataItems.remove(position);
		notifyItemRemoved(position + mHeaderViews.size());
		return item;
	}
	// endregion remove

	public void itemMove(int fromPosition, boolean up) {
		if (fromPosition < 0 || fromPosition >= mDataItems.size()) {
			return;
		}
//		Collections.swap(mDataItems, fromPosition, up ? fromPosition + 1 : fromPosition - 1 );
		T fromItem = mDataItems.get(fromPosition);

		int toPosition;
		if (up)
			toPosition = fromPosition + 1;
		else if (fromPosition == 0)
			return;
		else
			toPosition = fromPosition - 1;

		T toItem = mDataItems.get(toPosition);

		mDataItems.set(fromPosition, toItem);
		mDataItems.set(toPosition, fromItem);
		notifyItemMoved(fromPosition + mHeaderViews.size(), toPosition + mHeaderViews.size());
	}

	// region itemClear
	public void itemClear() {
		mDataItems.clear();
		notifyDataSetChanged();
	}
	// endregion itemClear

	// region itemReset
	public void itemReset(T t) {
		mDataItems.clear();
		mDataItems.add(t);
		notifyDataSetChanged();
	}

	public void itemReset(Collection<T> collection) {
		mDataItems.clear();
		mDataItems.addAll(collection);
		notifyDataSetChanged();
	}
	// endregion itemReset

	public boolean isEmpty() {
		return mDataItems.size() == 0;
	}


	//region itemClickListener
	public void setItemClickListener(OnItemClickListener mOnItemClickListener) {
		this.mOnItemClickListener = mOnItemClickListener;
	}

	@Override
	public boolean onItemClick(View view, int itemPosition) {
		return mOnItemClickListener != null && mOnItemClickListener.onItemClick(view, itemPosition);
	}

	@Override
	public boolean onItemLongClick(View view, int itemPosition) {
		return mOnItemClickListener != null && mOnItemClickListener.onItemLongClick(view, itemPosition);
	}

	@Override
	public boolean onItemDoubleClick(View view, int itemPosition) {
		return mOnItemClickListener != null && mOnItemClickListener.onItemDoubleClick(view, itemPosition);
	}
	//endregion

}
