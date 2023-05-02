package com.lisa.carpartner.host;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.lisa.carpartner.host.conversation.ConversationManager;
import com.lisa.carpartner.host.conversation.ConversationMsg;

import java.util.List;

public class ConversationRvAdapter extends RecyclerView.Adapter<ConversationRvAdapter.ViewHolder> {
    private List<ConversationMsg> mData;

    public ConversationRvAdapter(List<ConversationMsg> data) {
        mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.conversation_rv_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText(mData.get(position).speaker + ":" + mData.get(position).content);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.textview);
        }
    }
}
