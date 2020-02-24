package com.toandv98.checksum.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.toandv98.checksum.R;
import com.toandv98.checksum.data.entities.HashResult;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ItemViewHolder> {

    private Context mContext;
    private List<HashResult> mData;
    private int mPos = -1;

    public ResultAdapter(Context mContext, List<HashResult> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    public void updateData(List<HashResult> newData) {
        mData.clear();
        mData.addAll(newData);
        notifyDataSetChanged();
    }

    public void setEqual(HashResult equal) {
        mPos = equal == null || equal.getResult() == null ? -1 : mData.indexOf(equal);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_hash_result, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.tvResult.setCompoundDrawablesRelativeWithIntrinsicBounds(mPos == position ? R.drawable.ic_done_all : 0, 0, R.drawable.ic_content_copy, 0);
        holder.tvLabel.setText(mData.get(position).getType().name());
        holder.tvResult.setText(mData.get(position).getResult());
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_item_label)
        TextView tvLabel;
        @BindView(R.id.tv_item_result)
        TextView tvResult;
        ClipboardManager clipboard;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        }

        @OnClick(R.id.tv_item_result)
        void onClick() {
            HashResult result = mData.get(getAdapterPosition());
            ClipData clipData = ClipData.newPlainText(result.getType().name(), result.getResult());
            clipboard.setPrimaryClip(clipData);
            Toast.makeText(mContext, R.string.msg_copied, Toast.LENGTH_SHORT).show();
        }
    }
}
