package com.example.Stopi.tools;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import java.util.HashMap;

public class GenericDialog {

    private View                                view;

    private AlertDialog                         dialog;
    private AlertDialog.Builder                 builder;

    private MaterialButton                      confirm_btn;
    private MaterialButton                      cancel_btn;

    private HashMap<Integer, EditText>          editTextsMap;
    private HashMap<Integer, TextView>          textViewsMap;
    private HashMap<Integer, ImageView>         imageViewsMap;
    private HashMap<Integer, CheckBox>          checkBoxesMap;
    private HashMap<Integer, MaterialButton>    buttonsMap;

    private RecyclerView                        itemsList;

    //======================================

    public GenericDialog(View inflatedView) {
        this.view       = inflatedView;
        this.builder    = new AlertDialog.Builder(view.getContext());
        this.dialog     = builder.create();
        this.dialog     .setView(view);
        this.dialog     .getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    /**
     * show the dialog
     */
    public void show(){ if(!((Activity)view.getContext()).isFinishing()) dialog.show(); }

    /**
     * dismiss the dialog
     */
    public void dismiss(){ dialog.dismiss(); }

    //======================================

    /**
     * sets text view text
     * @param textView_layout_id text view layout id
     */
    public void setTVtext(int textView_layout_id, String text){ textViewsMap.get(textView_layout_id).setText(text); }

    /**
     * @param textView_layout_id text view layout id
     * @return text view text
     */
    public String getTVtext(int textView_layout_id){ return textViewsMap.get(textView_layout_id).getText().toString(); }

    /**
     * @param textView_layout_id text view layout id
     * @return text view object
     */
    public TextView getTextView(int textView_layout_id){ return textViewsMap.get(textView_layout_id); }

    /**
     * adds text views
     * @param layout_id_arr text views layout id array
     */
    public GenericDialog addTextViews(int[] layout_id_arr) {
        if (textViewsMap == null)
            textViewsMap = new HashMap<>();
        for (int layout_id : layout_id_arr)
            textViewsMap.put(layout_id, // key
                    view.findViewById(layout_id)); // textview
        return this;
    }

    //======================================

    /**
     * @param editText_layoutId edit text layout id
     * @return edit text corresponds with given id
     */
    public EditText getEditText(int editText_layoutId) { return editTextsMap.get(editText_layoutId); }

    /**
     * sets edit text hint
     * @param editText_layoutId edit text layout id
     */
    public void setEThint(int editText_layoutId, String hint) { editTextsMap.get(editText_layoutId).setHint(hint); }

    /**
     * @param editText_layoutId edit text layout id
     * @return edit text text
     */
    public String getETtext(int editText_layoutId) { return editTextsMap.get(editText_layoutId).getText().toString(); }

    /**
     * sets edit text error
     * @param editText_layoutId edit text layout id
     */
    public void setETerror(int editText_layoutId, String error){ editTextsMap.get(editText_layoutId).setError(error); }

    /**
     * adds edit texts
     * @param layout_id_arr edit texts layout id array
     */
    public GenericDialog addEditTexts(int[] layout_id_arr) {
        if (this.editTextsMap == null)
            this.editTextsMap = new HashMap<>();
        for (int layout_id : layout_id_arr)
            this.editTextsMap.put(layout_id, // key
                    view.findViewById(layout_id)); // editText
        return this;
    }

    //======================================

    /**
     * @param imageView_layout_id image view layout id
     * @return image view corresponds with given id
     */
    public ImageView getImageView(int imageView_layout_id){ return this.imageViewsMap.get(imageView_layout_id); }

    /**
     * adds image views
     * @param layout_id_arr image views layout id array
     */
    public GenericDialog addImageViews(int[] layout_id_arr) {
        if (this.imageViewsMap == null)
            this.imageViewsMap = new HashMap<>();
        for (int layout_id : layout_id_arr)
            this.imageViewsMap.put(layout_id, // key
                    view.findViewById(layout_id)); // imageView
        return this;
    }

    //======================================

    /**
     * @param checkBox_layout_id image view layout id
     * @return image view corresponds with given id
     */
    public CheckBox getCheckBox(int checkBox_layout_id){ return this.checkBoxesMap.get(checkBox_layout_id); }

    /**
     * adds image views
     * @param layout_id_arr image views layout id array
     */
    public GenericDialog addCheckBoxes(int[] layout_id_arr) {
        if (checkBoxesMap == null)
            checkBoxesMap = new HashMap<>();
        for (int layout_id : layout_id_arr)
            checkBoxesMap.put(layout_id, // key
                    view.findViewById(layout_id)); // imageView
        return this;
    }

    //======================================

    /**
     * @param btn_layout_id button layout id
     * @return the button corresponds with given id
     */
    public MaterialButton getButton(int btn_layout_id){ return this.buttonsMap.get(btn_layout_id); }

    /**
     * adds material buttons
     * @param layout_id_arr button layout id array
     */
    public GenericDialog addButtons(int[] layout_id_arr) {
        if (buttonsMap == null)
            buttonsMap = new HashMap<>();
        for (int layout_id : layout_id_arr)
            buttonsMap.put(layout_id, // key
                    view.findViewById(layout_id)); // button
        return this;
    }

    /**
     * set button click listener
     * @param btn_layout_id button layout id
     * @param listener OnClickListener listener
     */
    public void setButtonClickListener(int btn_layout_id, View.OnClickListener listener){
        if(buttonsMap != null && buttonsMap.get(btn_layout_id) != null)
            buttonsMap.get(btn_layout_id).setOnClickListener(listener);
    }

    //======================================
    /**
     * @param listAdapter adapter for list items
     * @throws NullPointerException if items list not set by id
     */
    public GenericDialog setRecyclerViewAdapter(RecyclerView.Adapter listAdapter) throws NullPointerException {
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
    public GenericDialog setConfirmListener(View.OnClickListener listener) throws NullPointerException {
        if (confirm_btn != null) {
            confirm_btn.setOnClickListener(listener);
            return this;
        }
        throw new NullPointerException("Confirm button is null");
    }

    /**
     * @param listener button click listener
     * @throws NullPointerException if cancel button not set by id
     */
    public GenericDialog setCancelListener(View.OnClickListener listener) throws NullPointerException {
        if(cancel_btn != null) {
            cancel_btn.setOnClickListener(listener);
            return this;
        }
        throw new NullPointerException("Cancel button is null");
    }

    //======================================

    public GenericDialog findRecyclerViewById(int list_view_layout_id) { itemsList = view.findViewById(list_view_layout_id); return this; }

    public GenericDialog findConfirmButtonById(int btn_layout_id) { confirm_btn = view.findViewById(btn_layout_id); return this; }

    public GenericDialog findCancelButtonById(int btn_layout_id) { cancel_btn = view.findViewById(btn_layout_id); return this; }

}

