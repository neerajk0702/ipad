package com.apitechnosoft.ipad.filepicker.adaptor;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import com.apitechnosoft.ipad.filepicker.listener.OnItemClickListener;


/**
 * Created 21-06-2017
 *
 * @author AST Inc.
 */
public abstract class CursorRecyclerViewAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

	protected final LayoutInflater inflater;
	protected Cursor mCursor;
	protected boolean mDataValid;
	protected OnItemClickListener itemClickListener;
	private Context mContext;
	private int mRowIdColumn;
	private DataSetObserver mDataSetObserver;

	public CursorRecyclerViewAdapter(Context context, Cursor cursor, OnItemClickListener itemClickListener) {
		mContext = context;
		mCursor = cursor;
		mDataValid = cursor != null;
		mRowIdColumn = mDataValid ? mCursor.getColumnIndex("_id") : -1;
		mDataSetObserver = new NotifyingDataSetObserver();
		if (mCursor != null) {
			mCursor.registerDataSetObserver(mDataSetObserver);
		}
		inflater = LayoutInflater.from(context);
		this.itemClickListener = itemClickListener;
	}

	@Override
	public int getItemCount() {
		if (mDataValid && mCursor != null) {
			return mCursor.getCount();
		}
		return 0;
	}

	@Override
	public long getItemId(int position) {
		if (mDataValid && mCursor != null && mCursor.moveToPosition(position)) {
			return mCursor.getLong(mRowIdColumn);
		}
		return 0;
	}

	@Override
	public void setHasStableIds(boolean hasStableIds) {
		super.setHasStableIds(true);
	}

	public abstract void onBindViewHolder(VH viewHolder, Cursor cursor, int position);

	@Override
	public void onBindViewHolder(VH viewHolder, int position) {
		if (!mDataValid) {
			throw new IllegalStateException("this should only be called when the cursor is valid");
		}
		if (!mCursor.moveToPosition(position)) {
			throw new IllegalStateException("couldn't move cursor to position " + position);
		}
		onBindViewHolder(viewHolder, mCursor, position);
	}

	public void changeCursor(Cursor cursor) {
		Cursor old = swapCursor(cursor);
		if (old != null) {
			old.close();
		}
	}

	public Cursor swapCursor(Cursor newCursor) {
		if (newCursor == mCursor) {
			return null;
		}
		final Cursor oldCursor = mCursor;
		if (oldCursor != null && mDataSetObserver != null) {
			oldCursor.unregisterDataSetObserver(mDataSetObserver);
		}
		mCursor = newCursor;
		if (mCursor != null) {
			if (mDataSetObserver != null) {
				mCursor.registerDataSetObserver(mDataSetObserver);
			}
			mRowIdColumn = newCursor.getColumnIndexOrThrow("_id");
			mDataValid = true;
			notifyDataSetChanged();
		} else {
			mRowIdColumn = -1;
			mDataValid = false;
			notifyDataSetChanged();
		}
		return oldCursor;
	}

	public Context getContext() {
		return mContext;
	}

	private class NotifyingDataSetObserver extends DataSetObserver {
		@Override
		public void onChanged() {
			super.onChanged();
			mDataValid = true;
			notifyDataSetChanged();
		}

		@Override
		public void onInvalidated() {
			super.onInvalidated();
			mDataValid = false;
			notifyDataSetChanged();
		}
	}
}
