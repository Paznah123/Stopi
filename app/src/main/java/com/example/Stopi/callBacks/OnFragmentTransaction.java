package com.example.Stopi.callBacks;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

public interface OnFragmentTransaction {

     /**
      * sets fragment in view by layout id
      */
     void setFragmentToView(Fragment fragment, int layout_id);

}
