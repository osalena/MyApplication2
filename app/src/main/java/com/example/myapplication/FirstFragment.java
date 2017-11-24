package com.example.myapplication;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class FirstFragment extends Fragment {


    public FirstFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.first_fragment_layout, container, false);

        TextView noTextView = (TextView) view.findViewById(R.id.noTxtView);

        Bundle args = getArguments();
        if(args!=null) {
            String no = String.valueOf(args.getInt("no"));
            noTextView.setText(no);
        }
        return view;
    }

}
