package com.example.Stopi.objects;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.google.android.material.button.MaterialButton;
import java.util.HashMap;

public class DialogView {

    private View view;

    private AlertDialog dialog;
    private AlertDialog.Builder builder;

    private MaterialButton confirm;
    private MaterialButton cancel;

    private HashMap<Integer, EditText> editTextsMap;
    private HashMap<Integer, TextView> textViewsMap;

    private ListView itemsList;

    //======================================

    public DialogView(View view) {
        this.view = view;
        this.builder = new AlertDialog.Builder(view.getContext());
        dialog = builder.create();
        dialog.setView(view);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    //======================================

    public void setTextViewText(int textView_layout_id, String text){ this.textViewsMap.get(textView_layout_id).setText(text); }

    public String getTextViewText(int textView_layout_id){ return this.textViewsMap.get(textView_layout_id).getText().toString(); }

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

    public void getEditTextText(int editText_layoutId, String text) { editTextsMap.get(editText_layoutId).setText(text); }

    public String getEditTextText(int editText_layoutId) { return editTextsMap.get(editText_layoutId).getText().toString(); }

    public void setEditTextError(int editText_layoutId, String error){ editTextsMap.get(editText_layoutId).setError(error); }

    public DialogView addEditTexts(int[] layout_id_arr) {
        if (editTextsMap == null)
            this.editTextsMap = new HashMap<>();
        for (int layout_id : layout_id_arr)
            this.editTextsMap.put(layout_id, // key
                    view.findViewById(layout_id)); // editText
        return this;
    }

    //======================================

    /**
     * @param listener item click listener
     * @throws NullPointerException if items list not set by id
     */
    public DialogView setListItemsClickListener(AdapterView.OnItemClickListener listener) throws NullPointerException {
        if (itemsList != null) {
            itemsList.setOnItemClickListener(listener);
            return this;
        }
        throw new NullPointerException("Items list is null");
    }

    /**
     * @param listAdapter adapter for list items
     * @throws NullPointerException if items list not set by id
     */
    public DialogView setListAdapter(BaseAdapter listAdapter) throws NullPointerException {
        if (itemsList != null) {
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

    public DialogView findListViewById(int list_view_layout_id) { itemsList = view.findViewById(list_view_layout_id); return this; }

    public DialogView findConfirmButtonById(int btn_layout_id) { confirm = view.findViewById(btn_layout_id); return this; }

    public DialogView findCancelButtonById(int btn_layout_id) { cancel = view.findViewById(btn_layout_id); return this; }

    //======================================

    public MaterialButton getConfirmButton() { return confirm; }

    public MaterialButton getCancelButotn() { return cancel; }

    public AlertDialog getAlertDialog() { return dialog; }

}

