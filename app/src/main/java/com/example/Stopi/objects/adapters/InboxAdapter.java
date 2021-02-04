package com.example.Stopi.objects.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.Stopi.R;
import com.example.Stopi.Utils;
import com.example.Stopi.objects.Email;
import com.example.Stopi.objects.dataManage.DBreader;
import com.example.Stopi.objects.dataManage.DBupdater;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;
import java.util.HashMap;
import de.hdodenhof.circleimageview.CircleImageView;

public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.InboxViewHolder> {

    private View view;
    private LayoutInflater inflater;

    private HashMap<String,Email> emails;

    //==================================================

    @Override
    public int getItemCount() { return emails.size(); }

    public InboxAdapter(Context context){
        this.inflater = LayoutInflater.from(context);
        this.emails = new HashMap<>();
    }

    public void setEmails(HashMap<String,Email> emails){
        this.emails = emails;
    }

    //==================================================

    @NonNull
    @Override
    public InboxAdapter.InboxViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = inflater.inflate(R.layout.item_inbox,parent,false);
        return new InboxAdapter.InboxViewHolder(view);
    }

    @Override
    public void onBindViewHolder(InboxAdapter.InboxViewHolder holder, int position) {
        String key = new ArrayList<>(emails.keySet()).get(position);
        Email email = emails.get(key);
        holder.email_title.setText(email.getTitle());
        holder.email_msg.setText(email.getMsg());

        holder.delete.setOnClickListener(v ->  DBupdater.getInstance().deleteEmail(key));

        holder.reply.setOnClickListener(v -> {
            String Uid = (String)email.getItemPhotoUrl().subSequence(0,email.getItemPhotoUrl().length()-4);
            Utils.createEmailDialog(inflater,Uid,null).show();
        });

        DBreader.getInstance().readProfilePic(holder.email_user_photo,email.getItemPhotoUrl());
    }

    public class InboxViewHolder extends RecyclerView.ViewHolder {

        CircleImageView email_user_photo;
        TextView email_title;
        TextView email_msg;
        MaterialButton delete;
        MaterialButton reply;

        InboxViewHolder(View itemView) {
            super(itemView);
            findViews();
        }

        //==================================================

        void findViews(){
            email_user_photo = itemView.findViewById(R.id.email_user_photo);
            email_title = itemView.findViewById(R.id.email_title);
            email_msg = itemView.findViewById(R.id.email_msg);
            delete = itemView.findViewById(R.id.email_delete);
            reply = itemView.findViewById(R.id.email_reply);
        }

        //==================================================

    }
}
