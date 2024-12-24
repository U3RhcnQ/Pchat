package com.example.myapplication2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private final List<Message> messages;

    public MessageAdapter(List<Message> messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_item, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messages.get(position);  // Get the Message object
        Log.d("Adapter", "Binding message at position " + position + ": " + message.getContent());
        holder.messageTextView.setText(message.getContent());
        holder.senderNameTextView.setText(message.getSenderName());  // Set sender's name

        // Adjust alignment for sent or received messages
        RelativeLayout.LayoutParams containerParams = (RelativeLayout.LayoutParams) holder.messageContainer.getLayoutParams();

        if (message.isSent()) {
            // Align container to the right for sent messages
            containerParams.addRule(RelativeLayout.ALIGN_PARENT_END);
            containerParams.removeRule(RelativeLayout.ALIGN_PARENT_START);
            holder.messageTextView.setBackgroundResource(R.drawable.message_bubble_sent);
        } else {
            // Align container to the left for received messages
            containerParams.addRule(RelativeLayout.ALIGN_PARENT_START);
            containerParams.removeRule(RelativeLayout.ALIGN_PARENT_END);
            holder.messageTextView.setBackgroundResource(R.drawable.message_bubble_received);
        }

        holder.messageContainer.setLayoutParams(containerParams);
    }

    @Override
    public int getItemCount() {
        Log.d("Adapter", "Item count: " + messages.size());
        return messages.size(); // Ensure this returns the correct size
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView;
        TextView senderNameTextView;
        LinearLayout messageContainer;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            senderNameTextView = itemView.findViewById(R.id.senderNameTextView);
            messageContainer = itemView.findViewById(R.id.messageContainer);
        }
    }
}
