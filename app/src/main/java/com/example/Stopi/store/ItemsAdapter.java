package com.example.Stopi.store;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Stopi.R;
import com.example.Stopi.tools.App;
import com.example.Stopi.tools.Utils;
import com.example.Stopi.profile.User;
import com.example.Stopi.dataBase.DBreader;
import com.example.Stopi.dataBase.DBupdater;
import com.example.Stopi.dataBase.KEYS;
import com.furkanakdemir.surroundcardview.SurroundCardView;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.StoreViewHolder> {

    private View            view;
    private LayoutInflater  inflater;

    private List<StoreItem> storeItems;

    private OnCoinsChanged  onCoinsChanged;

    //====================================================

    public ItemsAdapter(Context context, List<StoreItem> store_items){
        this.storeItems     = store_items;
        this.inflater       = LayoutInflater.from(context);
    }

    public void setClickListener(OnCoinsChanged onCoinsChanged) { this.onCoinsChanged = onCoinsChanged; }

    //====================================================

    @Override
    public int getItemCount() { return storeItems.size(); }

    @NonNull
    @Override
    public ItemsAdapter.StoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = inflater.inflate(R.layout.item_store,parent,false);
        return new StoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemsAdapter.StoreViewHolder holder, int position) {
        StoreItem storeItem         = storeItems.get(position);
        holder.store_item_title     .setText(storeItem.getTitle());
        holder.store_item_price     .setText(""+ storeItem.getPrice());

        if(onCoinsChanged != null)  // store fragment
            holder.svc              .setOnClickListener(v -> beginPurchase(storeItem));
        else                        // bought_items fragment
            holder.svc              .setOnClickListener(v -> Utils.getInstance().onCardClick(holder.svc));

        DBreader.getInstance()      .readPic(KEYS.STORE,holder.store_item_photo, storeItem.getTitle());
    }

    private void beginPurchase(StoreItem storeItem){
        User user = DBreader.getInstance().getUser();
        if(storeItem.getPrice() > user.getCoins()){
            App.toast("Not Enough Coins!");
            return;
        }
        Store.getInstance().buyItem(user, storeItem);
        DBupdater.getInstance().saveLoggedUser();
        onCoinsChanged.updateWallet();
    }

    //====================================================

    public class StoreViewHolder extends RecyclerView.ViewHolder {

        TextView            store_item_title;
        TextView            store_item_price;
        CircleImageView     store_item_photo;
        SurroundCardView    svc;

        StoreViewHolder(View itemView) {
            super(itemView);
            findViews();
        }

        //==================================================

        void findViews(){
            store_item_title    = itemView.findViewById(R.id.store_item_title);
            store_item_price    = itemView.findViewById(R.id.store_item_price);
            store_item_photo    = itemView.findViewById(R.id.store_item_photo);
            svc                 = itemView.findViewById(R.id.store_item_svc);
        }

        //==================================================

    }
}