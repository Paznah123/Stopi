package com.example.Stopi.tools;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import java.util.HashMap;

public class DialogView {

    private View view;

    private AlertDialog                     dialog;
    private AlertDialog.Builder             builder;

    private MaterialButton                  confirm;
    private MaterialButton                  cancel;

    private HashMap<Integer, EditText>      editTextsMap;
    private HashMap<Integer, TextView>      textViewsMap;
    private HashMap<Integer, ImageView>     imageViewsMap;

    private RecyclerView                    itemsList;

    //======================================

    public DialogView(View view) {
        this.view       = view;
        this.builder    = new AlertDialog.Builder(view.getContext());
        this.dialog     = builder.create();
        this.dialog     .setView(view);
        this.dialog     .getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public void show(){ if(!((Activity) view.getContext()).isFinishing()) dialog.show(); }

    public void dismiss(){ dialog.dismiss(); }

    //======================================

    public void setTVtext(int textView_layout_id, String text){ this.textViewsMap.get(textView_layout_id).setText(text); }

    public String getTVtext(int textView_layout_id){ return this.textViewsMap.get(textView_layout_id).getText().toString(); }

    public TextView getTextView(int textView_layout_id){ return this.textViewsMap.get(textView_layout_id); }

    public DialogView addTextViews(int[] layout_id_arr) {
        if (textViewsMap == null)
            this.textViewsMap = new HashMap<>();
        for (int layout_id : layout_id_arr)
            this.textViewsMap.put(layout_id, // key
                    view.findViewById(layout_id)); // textview
        return this;
    }

    //======================================

    public EditText getEditText(int editText_layoutId) { return editTextsMap.get(editText_layoutId); }

    public void setEThint(int editText_layoutId, String hint) { editTextsMap.get(editText_layoutId).setHint(hint); }

    public String getETtext(int editText_layoutId) { return editTextsMap.get(editText_layoutId).getText().toString(); }

    public void setETerror(int editText_layoutId, String error){ editTextsMap.get(editText_layoutId).setError(error); }

    public DialogView addEditTexts(int[] layout_id_arr) {
        if (editTextsMap == null)
            this.editTextsMap = new HashMap<>();
        for (int layout_id : layout_id_arr)
            this.editTextsMap.put(layout_id, // key
                    view.findViewById(layout_id)); // editText
        return this;
    }

    //======================================

    public ImageView getImageView(int imageView_layout_id){ return this.imageViewsMap.get(imageView_layout_id); }

    public DialogView addImageViews(int[] layout_id_arr) {
        if (textViewsMap == null)
            this.textViewsMap = new HashMap<>();
        for (int layout_id : layout_id_arr)
            this.textViewsMap.put(layout_id, // key
                    view.findViewById(layout_id)); // imageView
        return this;
    }

    //======================================

    /**
     * @param listAdapter adapter for list items
     * @throws NullPointerException if items list not set by id
     */
    public DialogView setRecyclerViewAdapter(RecyclerView.Adapter listAdapter) throws NullPointerException {
        if (itemsList != null) {
            itemsList.setLayoutManager(new LinearLayoutManager(view.getContext()));
            itemsList.setAdapter(listAdapter);
            return this;
        }
        throw new NullPointerException("Items list is null");
    }

    //======================================

    /**
     * @param listener button click listener
     * @throws NullPointerException if confirm button not set by id
     */
    public DialogView setConfirmListener(View.OnClickListener listener) throws NullPointerException {
        if (confirm != null) {
            confirm.setOnClickListener(listener);
            return this;
        }
        throw new NullPointerException("Confirm button is null");
    }

    /**
     * @param listener button click listener
     * @throws NullPointerException if cancel button not set by id
     */
    public DialogView setCancelListener(View.OnClickListener listener) throws NullPointerException {
        if(cancel != null) {
            cancel.setOnClickListener(listener);
            return this;
        }
        throw new NullPointerException("Cancel button is null");
    }

    //======================================

    public DialogView findRecyclerViewById(int list_view_layout_id) { itemsList = view.findViewById(list_view_layout_id); return this; }

    public DialogView findConfirmButtonById(int btn_layout_id) { confirm = view.findViewById(btn_layout_id); return this; }

    public DialogView findCancelButtonById(int btn_layout_id) { cancel = view.findViewById(btn_layout_id); return this; }

    //======================================

    public MaterialButton getConfirmButton() { return confirm; }

    public MaterialButton getCancelButton() { return cancel; }

    public AlertDialog getAlertDialog() { return dialog; }

}

