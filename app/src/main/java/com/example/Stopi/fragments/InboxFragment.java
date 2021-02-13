package com.example.Stopi.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.Stopi.App;
import com.example.Stopi.R;
import com.example.Stopi.callBacks.OnEmailReceived;
import com.example.Stopi.objects.Email;
import com.example.Stopi.objects.adapters.InboxAdapter;
import com.example.Stopi.objects.dataManage.DBupdater;
import com.example.Stopi.objects.dataManage.Refs;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;

public class InboxFragment extends Fragment {

    private View view;
    private Context context;

    private RecyclerView inbox_list;
    private InboxAdapter inbox_adapter;

    private TextView noEmails;

    private OnEmailReceived onEmailReceived;

    public interface OnEmailsUpdate { void updateEmails(HashMap<String,Email> emails); }

    //====================================================

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onEmailReceived = (OnEmailReceived) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_inbox, container, false);
        context = this.getContext();

        inbox_list = view.findViewById(R.id.inbox_list);
        noEmails = view.findViewById(R.id.no_emails_lbl);

        inbox_list.setLayoutManager(new LinearLayoutManager(context));
        inbox_adapter = new InboxAdapter(context);
        inbox_list.setAdapter(inbox_adapter);

        refreshEmailFeed(onEmailsUpdate, noEmails);

        return view;
    }

    //====================================================

    private OnEmailsUpdate onEmailsUpdate = new OnEmailsUpdate() {
        @Override
        public void updateEmails(HashMap<String, Email> emails) {
            inbox_adapter.setEmails(emails);
            inbox_adapter.notifyDataSetChanged();
        }
    };

    private void refreshEmailFeed(@NonNull OnEmailsUpdate onEmailsUpdate, TextView no_emails) {
        Refs.getEmailsRef()
                .child(App.getLoggedUser().getUid())
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String,Email> emailsList = new HashMap<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Email email = snapshot.getValue(Email.class);
                    emailsList.put(snapshot.getKey(),email);
                }
                onEmailsUpdate.updateEmails(emailsList);
                onEmailReceived.updateEmailCounter(emailsList.size());
                int visibility;
                if(emailsList.size() == 0) visibility=View.VISIBLE;
                else                        visibility=View.INVISIBLE;
                no_emails.setVisibility(visibility);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }
}