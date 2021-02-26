package com.example.Stopi.social.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.Stopi.R;
import com.example.Stopi.tools.App;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;

    private List<Message>   mChat;

    //=============================

    public MessageAdapter(List<Message> mChat){ this.mChat = mChat; }

    //=============================

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int msg_type;

        if(viewType == MSG_TYPE_RIGHT)      msg_type = R.layout.item_chat_right;
        else                                msg_type = R.layout.item_chat_left;

        View view = LayoutInflater.from(parent.getContext()).inflate(msg_type, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message message         = mChat.get(position);
        holder.show_message     .setText(message.getMessage());
    }

    //=============================

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(mChat.get(position).getSender().equals(App.getLoggedUser().getUid()))
            return MSG_TYPE_RIGHT;
        else
            return MSG_TYPE_LEFT;
    }

    //=============================

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView    show_message;

        public ViewHolder(View itemView){
            super(itemView);
            show_message    = itemView.findViewById(R.id.show_message);
        }
    }

    //=============================

}