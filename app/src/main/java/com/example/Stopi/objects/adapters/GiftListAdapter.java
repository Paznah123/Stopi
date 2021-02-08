package com.example.Stopi.objects.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.Stopi.App;
import com.example.Stopi.R;
import com.example.Stopi.objects.StoreItem;
import java.util.ArrayList;
import java.util.HashMap;
import de.hdodenhof.circleimageview.CircleImageView;

public class GiftListAdapter extends BaseAdapter {

    LayoutInflater inflater;

    HashMap<String, StoreItem> items;
    ArrayList<String> keys;

    CircleImageView itemPhoto;
    TextView itemTitle;

    //=============================

    public GiftListAdapter(Context ctx, HashMap<String, StoreItem> giftsList){
        this.inflater = LayoutInflater.from(ctx);
        this.items = giftsList;
        this.keys = new ArrayList<>(giftsList.keySet());
    }

    //=============================

    @Override
    public int getCount() { return items.size(); }

    @Override
    public Object getItem(int position) { return items.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    //=============================

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View item = inflater.inflate(R.layout.item_gift,parent,false);

        StoreItem storeItem = items.get(keys.get(position));

        itemPhoto = item.findViewById(R.id.item_gift_photo);
        itemTitle = item.findViewById(R.id.item_gift_title);

        itemTitle.setText(storeItem.getTitle());

        Glide.with(App.getAppContext())
                .load(storeItem.getPhotoUrl())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .centerInside()
                .into(itemPhoto);

        return item;
    }
}
