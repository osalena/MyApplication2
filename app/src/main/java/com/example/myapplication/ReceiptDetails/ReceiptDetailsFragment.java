package com.example.myapplication.ReceiptDetails;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;

public class ReceiptDetailsFragment extends Fragment {

  public static final String ARGUMENT_IMAGE_RES_ID = "imageResId";
  public static final String ARGUMENT_NAME = "name";
  public static final String ARGUMENT_DESCRIPTION = "description";
    public static final String ARGUMENT_POSTER = "poster";
    public static final String ARGUMENT_DATE = "date";


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

     View view = inflater.inflate(R.layout.fragment_animals_details, container, false);

     ImageView imageView = (ImageView) view.findViewById(R.id.animal_image);
     TextView nameTextView = (TextView) view.findViewById(R.id.name);
     TextView descriptionTextView = (TextView) view.findViewById(R.id.description);
      TextView posterTextView = (TextView) view.findViewById(R.id.poster);
      TextView dateTextView = (TextView) view.findViewById(R.id.date);

    Bundle args = getArguments();
    imageView.setImageResource(args.getInt(ARGUMENT_IMAGE_RES_ID));
    nameTextView.setText(args.getString(ARGUMENT_NAME));
      posterTextView.setText(args.getString(ARGUMENT_POSTER));
      dateTextView.setText(args.getString(ARGUMENT_DATE));
    String descText=  args.getString(ARGUMENT_DESCRIPTION);
    descriptionTextView.setText(descText);
    return view;
  }
}
