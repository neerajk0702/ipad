package com.apitechnosoft.ipad.filepicker.adaptor;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;


import com.apitechnosoft.ipad.R;
import com.apitechnosoft.ipad.component.ASTImageView;
import com.apitechnosoft.ipad.filepicker.helper.ASTImageUtil;
import com.apitechnosoft.ipad.filepicker.listener.OnItemClickListener;
import com.apitechnosoft.ipad.filepicker.model.MediaFile;
import com.apitechnosoft.ipad.utils.ASTUIUtil;

/**
 * Created 21-06-2017
 *
 * @author AST Inc.
 */
public class MediaCursorAdaptor extends CursorRecyclerViewAdapter<MediaCursorAdaptor.MediaViewHolder> {

	protected int imageSize;
	protected int mediaType;

	public MediaCursorAdaptor(Context context, Cursor cursor, int imageSize, int mediaType, OnItemClickListener itemClickListener) {
		super(context, cursor, itemClickListener);
		this.imageSize = ASTImageUtil.imageSize(context, imageSize);
		this.mediaType = mediaType;
	}

	@Override
	public MediaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View itemView = inflater.inflate(R.layout.picker_item_photo, parent, false);
		return new MediaViewHolder(itemView, itemClickListener);
	}

	@Override
	public void onBindViewHolder(MediaViewHolder viewHolder, Cursor cursor, int position) {
		MediaFile file = MediaFile.deviceFile(cursor, mediaType);
		viewHolder.file = file;
		viewHolder.imageView.setSize(imageSize, imageSize);
		viewHolder.imageView.setImageFile(file.getFilePath(), R.drawable.image_placeholder);
		viewHolder.setSelectionView();
	}

	public static class MediaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

		protected ASTImageView imageView;
		protected View alphaView;
		protected MediaFile file;
		OnItemClickListener itemClickListener;

		public MediaViewHolder(View itemView, OnItemClickListener itemClickListener) {
			super(itemView);
			imageView = itemView.findViewById(R.id.image_view);
			alphaView = itemView.findViewById(R.id.view_alpha);
			this.itemClickListener = itemClickListener;
			itemView.setOnClickListener(this);
		}

		public void setSelectionView() {
			if (itemClickListener.isSelected(file)) {
				alphaView.setAlpha(0.5f);
				Drawable checkDrawable = ASTUIUtil.getDrawable(R.drawable.ic_done_white);
				if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
					itemView.setForeground(checkDrawable);
				} else {
					((FrameLayout) itemView).setForeground(checkDrawable);
				}
			} else {
				alphaView.setAlpha(0.0f);
				if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
					itemView.setForeground(null);
				} else {
					((FrameLayout) itemView).setForeground(null);
				}
			}
		}

		@Override
		public void onClick(View v) {
			itemClickListener.onFileClick(v, file);
		}
	}
}
