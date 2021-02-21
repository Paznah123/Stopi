package com.example.Stopi.tools;

import androidx.fragment.app.Fragment;

public interface OnFragmentTransaction {

     /**
      * sets fragment in view by layout id
      */
     void setFragmentToView(Fragment fragment, int layout_id);

}
