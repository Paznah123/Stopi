package com.example.Stopi.tools;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.Stopi.R;
import java.util.ArrayList;

public class ThemesAdapter extends RecyclerView.Adapter<ThemesAdapter.ThemesViewHolder>{

    private View                view;
    private Context             context;
    private ArrayList<Theme>    themes;

    //====================================================

    public ThemesAdapter(Context context){
        this.context = context;
        this.themes = new ArrayList<>();
        this.themes.add(new Theme("Warm",getColorResource(R.color.purple_200),getColorResource(R.color.purple_500)));
        this.themes.add(new Theme("Cold",getColorResource(R.color.teal_200),getColorResource(R.color.teal_700)));
        this.themes.add(new Theme("Cool",getColorResource(R.color.blue_200),getColorResource(R.color.blue_500)));
    }

    private int getColorResource(int colorId) {
        return context.getResources().getColor(colorId);
    }

    //====================================================

    public class Theme {
        private String title;
        private int primary;
        private int variant;

        public Theme(String title, int primary, int variant){
            this.title      = title;
            this.primary    = primary;
            this.variant    = variant;
        }
    }

    //====================================================

    @NonNull
    @Override
    public ThemesAdapter.ThemesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_theme,parent,false);
        return new ThemesAdapter.ThemesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ThemesAdapter.ThemesViewHolder holder, int position) {
        Theme theme = themes.get(position);

        holder.theme_title              .setText(theme.title);
        holder.color_primary            .setBackgroundColor(theme.primary);
        holder.color_primary_variant    .setBackgroundColor(theme.variant);

        holder.itemView                 .setOnClickListener(v -> {
                    switch (position){
                        case KEYS.THEME_WARM:
                            Utils.get().changeToTheme(((Activity)context),KEYS.THEME_WARM);
                            break;
                        case KEYS.THEME_COLD:
                            Utils.get().changeToTheme(((Activity)context),KEYS.THEME_COLD);
                            break;
                        case KEYS.THEME_COOL:
                            Utils.get().changeToTheme(((Activity)context),KEYS.THEME_COOL);
                            break;
                    }
                });
    }

    //====================================================

    @Override
    public int getItemCount() { return themes.size(); }

    //====================================================

    public class ThemesViewHolder extends RecyclerView.ViewHolder {

        TextView    theme_title;
        ImageView   color_primary;
        ImageView   color_primary_variant;

        ThemesViewHolder(View itemView) {
            super(itemView);
            findViews();
        }

        //====================================================

        void findViews(){
            theme_title             = itemView.findViewById(R.id.item_theme_title);
            color_primary           = itemView.findViewById(R.id.item_theme_color_primary);
            color_primary_variant   = itemView.findViewById(R.id.item_theme_color_primary_variant);
        }

        //====================================================

    }
}
