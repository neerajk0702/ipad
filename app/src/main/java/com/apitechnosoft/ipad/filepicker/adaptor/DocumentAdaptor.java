package com.apitechnosoft.ipad.filepicker.adaptor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.apitechnosoft.ipad.R;
import com.apitechnosoft.ipad.component.ASTImageView;
import com.apitechnosoft.ipad.component.ASTTextView;
import com.apitechnosoft.ipad.filepicker.listener.OnItemClickListener;
import com.apitechnosoft.ipad.filepicker.model.MediaFile;
import com.apitechnosoft.ipad.utils.FNObjectUtil;

import java.util.ArrayList;

/**
 * Created 03-07-2017
 *
 * @author AST Inc.
 */
public class DocumentAdaptor extends RecyclerView.Adapter<DocumentAdaptor.DocumentViewHolder> {

    protected final LayoutInflater inflater;
    protected OnItemClickListener itemClickListener;
    private Context mContext;
    private ArrayList<MediaFile> documents;

    public DocumentAdaptor(Context context, ArrayList<MediaFile> documents, OnItemClickListener itemClickListener) {
        this.mContext = context;
        this.itemClickListener = itemClickListener;
        this.documents = documents;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public DocumentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.picker_item_doc, parent, false);
        return new DocumentViewHolder(itemView, itemClickListener);
    }

    @Override
    public void onBindViewHolder(DocumentViewHolder viewHolder, int position) {
        MediaFile document = getItem(position);
        viewHolder.file = document;
        viewHolder.itemView.setVisibility(View.VISIBLE);
        viewHolder.docNameView.setText(document.getTitle());
        viewHolder.docSizeView.setText(Formatter.formatShortFileSize(mContext, document.getSize()));
        viewHolder.imageView.setImageResource(document.getResource());
        viewHolder.setSelectionView();
    }

    public MediaFile getItem(int position) {
        return documents.get(position);
    }

    @Override
    public int getItemCount() {
        return FNObjectUtil.isEmpty(documents) ? 0 : documents.size();
    }

    public static class DocumentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private OnItemClickListener itemClickListener;
        private ASTImageView imageView;
        private ASTTextView docNameView;
        private ASTTextView docSizeView;
        private View alphaView;
        private ASTImageView alphaImage;
        private MediaFile file;

        public DocumentViewHolder(View itemView, OnItemClickListener itemClickListener) {
            super(itemView);
            imageView = itemView.findViewById(R.id.doc_image);
            docNameView = itemView.findViewById(R.id.doc_name);
            docSizeView = itemView.findViewById(R.id.doc_size);
            alphaView = itemView.findViewById(R.id.view_alpha);
            alphaImage = itemView.findViewById(R.id.image_alpha);
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
