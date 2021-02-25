package com.example.Stopi.tools;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import java.util.HashMap;

public class GenDialogFragment extends BaseDialogFragment<GenDialogFragment.OnDialogFragmentClickListener> {

    private View view;

    private MaterialButton                  confirm;
    private MaterialButton                  cancel;

    private HashMap<Integer, EditText>      editTextsMap;
    private HashMap<Integer, TextView>      textViewsMap;
    private HashMap<Integer, ImageView>     imageViewsMap;

    private RecyclerView                    itemsList;

    public interface OnDialogFragmentClickListener {
        void onOkClicked(GenDialogFragment dialog);
        void onCancelClicked(GenDialogFragment dialog);
    }

    //======================================

    public static GenDialogFragment newInstance(int layoutId) {
        GenDialogFragment frag = new GenDialogFragment();

        Bundle args = new Bundle();
        args.putInt("layoutId", layoutId);
        frag.setArguments(args);

        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int layoutId = getArguments().getInt("layoutId");
        view = inflater.inflate(layoutId, container, false);

        return view;
    }

    //======================================

    public void setTVtext(int textView_layout_id, String text){ this.textViewsMap.get(textView_layout_id).setText(text); }

    public String getTVtext(int textView_layout_id){ return this.textViewsMap.get(textView_layout_id).getText().toString(); }

    public TextView getTextView(int textView_layout_id){ return this.textViewsMap.get(textView_layout_id); }

    public GenDialogFragment addTextViews(int[] layout_id_arr) {
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

    public GenDialogFragment addEditTexts(int[] layout_id_arr) {
        if (editTextsMap == null)
            this.editTextsMap = new HashMap<>();
        for (int layout_id : layout_id_arr)
            this.editTextsMap.put(layout_id, // key
                    view.findViewById(layout_id)); // editText
        return this;
    }

    //======================================

    public ImageView getImageView(int imageView_layout_id){ return this.imageViewsMap.get(imageView_layout_id); }

    public GenDialogFragment addImageViews(int[] layout_id_arr) {
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
    public GenDialogFragment setRecyclerViewAdapter(RecyclerView.Adapter listAdapter) throws NullPointerException {
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
    public GenDialogFragment setConfirmListener(View.OnClickListener listener) throws NullPointerException {
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
    public GenDialogFragment setCancelListener(View.OnClickListener listener) throws NullPointerException {
        if(cancel != null) {
            cancel.setOnClickListener(listener);
            return this;
        }
        throw new NullPointerException("Cancel button is null");
    }

    //======================================

    public GenDialogFragment findRecyclerViewById(int list_view_layout_id) { itemsList = view.findViewById(list_view_layout_id); return this; }

    public GenDialogFragment findConfirmButtonById(int btn_layout_id) { confirm = view.findViewById(btn_layout_id); return this; }

    public GenDialogFragment findCancelButtonById(int btn_layout_id) { cancel = view.findViewById(btn_layout_id); return this; }

    //======================================

    public MaterialButton getConfirmButton() { return confirm; }

    public MaterialButton getCancelButton() { return cancel; }

}
