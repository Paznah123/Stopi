package com.example.Stopi.objects;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;

public class DialogView {

    private View view;

    private AlertDialog dialog;
    private AlertDialog.Builder builder;

    private MaterialButton confirm;
    private MaterialButton cancel;

    private ArrayList<EditText> editTexts;
    private ArrayList<TextView> textViews;

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

    /**
     * set text to textView(i)
     */
    public void setText(int textViewPosition, String text) { this.textViews.get(textViewPosition).setText(text); }

    public DialogView addTextViews(int amount, int[] layout_id_arr) {
        if (textViews == null) {
            this.textViews = new ArrayList<>();
            for (int i = 0; i < amount; i++)
                this.textViews.add(view.findViewById(layout_id_arr[i]));
        }
        return this;
    }

    //======================================

    /**
     * get text from editText(i)
     */
    public String getText(int editTextPosition) {
        return editTexts.get(editTextPosition).getText().toString();
    }

    public void setError(int editTextPosition, String error){
        editTexts.get(editTextPosition).setError(error);
    }

    public DialogView addEditTexts(int amount, int[] layout_id_arr) {
        if (editTexts == null) {
            this.editTexts = new ArrayList<>();
            for (int i = 0; i < amount; i++)
                this.editTexts.add(view.findViewById(layout_id_arr[i]));
        }
        return this;
    }

    //======================================

    public DialogView setListItemsClickListener(AdapterView.OnItemClickListener listener) {
        if (itemsList != null)
            itemsList.setOnItemClickListener(listener);
        return this;
    }

    public DialogView setListAdapter(BaseAdapter listAdapter) {
        if (itemsList != null)
            itemsList.setAdapter(listAdapter);
        return this;
    }

    //======================================

    public DialogView setConfirmListener(View.OnClickListener listener) { confirm.setOnClickListener(listener); return this; }

    public DialogView setCancelListener(View.OnClickListener listener) { cancel.setOnClickListener(listener); return this; }

    //======================================

    public DialogView findListViewById(int list_view_layout_id) { itemsList = view.findViewById(list_view_layout_id); return this; }

    public DialogView findConfirmButtonById(int btn_layout_id) { confirm = view.findViewById(btn_layout_id); return this; }

    public DialogView findCancelButtonById(int btn_layout_id) { cancel = view.findViewById(btn_layout_id); return this; }

    //======================================

    public MaterialButton getConfirm() { return confirm; }

    public MaterialButton getCancel() { return cancel; }

    public AlertDialog getDialog() { return dialog; }

}

