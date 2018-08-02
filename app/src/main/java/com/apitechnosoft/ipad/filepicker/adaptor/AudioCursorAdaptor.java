package com.apitechnosoft.ipad.filepicker.adaptor;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;


import com.apitechnosoft.ipad.R;
import com.apitechnosoft.ipad.component.ASTImageView;
import com.apitechnosoft.ipad.component.ASTTextView;
import com.apitechnosoft.ipad.filepicker.listener.OnItemClickListener;
import com.apitechnosoft.ipad.filepicker.model.MediaFile;


/**
 * Created 22-06-2017
 *
 * @author AST Inc.
 */
public class AudioCursorAdaptor extends CursorRecyclerViewAdapter<AudioCursorAdaptor.AudioViewHolder> {

    protected int mediaType;

    public AudioCursorAdaptor(Context context, Cursor cursor, int mediaType, OnItemClickListener itemClickListener) {
        super(context, cursor, itemClickListener);
        this.mediaType = mediaType;
    }

    @Override
    public void onBindViewHolder(AudioViewHolder viewHolder, Cursor cursor, int position) {
        MediaFile file = MediaFile.deviceFile(cursor, mediaType);
        viewHolder.file = file;
        viewHolder.itemView.setVisibility(View.VISIBLE);
        viewHolder.docNameView.setText(file.getTitle());
        viewHolder.docSizeView.setText(Formatter.formatShortFileSize(getContext(), file.getSize()));
        viewHolder.imageView.setImageResource(file.getResource());
        viewHolder.setSelectionView();
    }

    @Override
    public AudioViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.picker_item_doc, parent, false);
        return new AudioViewHolder(itemView, itemClickListener);
    }

    public static class AudioViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private OnItemClickListener itemClickListener;
        private ASTImageView imageView;
        private ASTTextView docNameView;
        private ASTTextView docSizeView;
        private View alphaView;
        private ASTImageView alphaImage;
        private MediaFile file;

        public AudioViewHolder(View itemView, OnItemClickListener itemClickListener) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.doc_image);
            this.docNameView = itemView.findViewById(R.id.doc_name);
            this.docSizeView = itemView.findViewById(R.id.doc_size);
            this.alphaView = itemView.findViewById(R.id.view_alpha);
            this.alphaImage = itemView.findViewById(R.id.image_alpha);
            this.itemClickListener = itemClickListener;
            itemView.setOnClickListener(this);
        }

        public void setSelectionView() {
            if (itemClickListener.isSelected(file)) {
                alphaView.setAlpha(0.3f);
                alphaImage.setVisibility(View.VISIBLE);
            } else {
                alphaView.setAlpha(0.0f);
                alphaImage.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View v) {
            this.itemClickListener.onFileClick(v, file);
            this.setSelectionView();
        }
    }
}
