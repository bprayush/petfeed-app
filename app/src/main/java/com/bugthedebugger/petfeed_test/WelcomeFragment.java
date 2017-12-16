package com.bugthedebugger.petfeed_test;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class WelcomeFragment extends Fragment {


    String petName;

    @SuppressLint("ValidFragment")
    public WelcomeFragment(String petName) {
        // Required empty public constructor
        this.petName = petName;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_welcome, container, false);
        view.setBackgroundColor(Color.WHITE);

        TextView petNameTv = view.findViewById(R.id.petNameTv);
        petNameTv.setText(this.petName);

        return view;
    }

}
