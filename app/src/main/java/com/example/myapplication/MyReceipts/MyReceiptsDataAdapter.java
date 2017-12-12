package com.example.myapplication.MyReceipts;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.dataBase.InfoReceipt;
import com.example.myapplication.dataBase.MyInfoManager;

import java.util.List;

/* DataAdapter for My Receipts list
* receive:
*   MyReceiptActivity context
*   list of receipts
* */
public class MyReceiptsDataAdapter extends ArrayAdapter {

    protected Context           context;
    private List<InfoReceipt>   receipts;


    public MyReceiptsDataAdapter(Context context, int resource, List<InfoReceipt> objects) {
        super(context, resource, objects);
        this.context = context;
        receipts = objects;
    }

    @Override
    public int getCount() {
        return receipts.size();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View rootView = mInflater.inflate(R.layout.my_receipts_layout, null);
        final InfoReceipt currentReceipt = getItem(position);
        TextView title = (TextView) rootView.findViewById(R.id.my_rec_title);
        title.setText(currentReceipt.getTitle());
        //ImageView receiptImage = (ImageView) rootView.findViewById(R.id.animal_image);
        //receiptImage.setImageBitmap(currentReceipt.getImage1());
        //ImageView userImage = (ImageView) rootView.findViewById(R.id.user_image);
        //TO DO
       // userImage.setImageBitmap(null);
        //TextView poster = (TextView) rootView.findViewById(R.id.poster);
        //TO DO
        //poster.setText(null);
        //TextView date = (TextView) rootView.findViewById(R.id.date);
        //TO DO
       // date.setText(null);
       // TextView by_cook = (TextView) rootView.findViewById(R.id.by_cook);
        //TO DO
       // by_cook.setText(null);
       // TextView liked = (TextView) rootView.findViewById(R.id.liked);
        //TO DO
       // liked.setText(null);
       // TextView cooked = (TextView) rootView.findViewById(R.id.cooked);
        //TO DO
       // cooked.setText(null);





        /* Delete My Receipt function
        *  from "Garbage" icon
        *  displays Toast
        *  use deleteReceipt from MyInfoManager*/
        ImageView deleteIcon = (ImageView) rootView.findViewById(R.id.my_rec_delete_icon);
        deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String title = "Delete Receipt";
                final String msg = "Are you sure?";
                AlertDialog.Builder  builder=  new AlertDialog.Builder(context);
                builder.setTitle(title);
                builder.setMessage(msg);

                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        MyInfoManager.getInstance().deleteReceipt(currentReceipt);
                        MyReceiptsDataAdapter.this.remove(currentReceipt);
                        MyReceiptsDataAdapter.this.notifyDataSetChanged();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog .show();
            }
        });


        return rootView;
    }




    @Override
    public InfoReceipt getItem(int position) {
        return receipts.get(position);
    }


    /*
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.recycler_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        final int imageResId = mImageResIds[position];
        final String name = mNames[position];
        final String description = mDescriptions[position];
        final String poster = mPosters[position];
        final String date = mDates[position];
        final String liked = mLiked[position];
        final String cooked = mCooked[position];
        viewHolder.setData(imageResId, name, poster, date, liked, cooked);

        viewHolder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AnimalDetailsFragment detailsFragment = new AnimalDetailsFragment();
                // send data to fragment
                Bundle args = new Bundle();
                args.putInt(AnimalDetailsFragment.ARGUMENT_IMAGE_RES_ID, imageResId);
                args.putString(AnimalDetailsFragment.ARGUMENT_NAME, name);
                args.putString(AnimalDetailsFragment.ARGUMENT_DESCRIPTION, description);
                args.putString(AnimalDetailsFragment.ARGUMENT_POSTER, poster);
                args.putString(AnimalDetailsFragment.ARGUMENT_DATE, date);
                detailsFragment.setArguments(args);
                // open fragment
                FragmentManager fm = ((Activity)context).getFragmentManager();
                FragmentTransaction tr= fm.beginTransaction();
                tr.replace(R.id.root_layout, detailsFragment);
                tr.addToBackStack(null);
                tr .commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mNames.length;
    }

}

class ViewHolder extends RecyclerView.ViewHolder {
    // Views
    public ImageView mImageView;
    public TextView mNameTextView;
    public TextView mPosterTextView;
    public TextView mDateTextView;
    public TextView mByCookTextView;
    public ImageView mLogoImageView;
    public TextView mLikedTextView;
    public TextView mCookedTextView;


    public ViewHolder(View itemView) {
        super(itemView);

        // Get references to image and name.
        mImageView = (ImageView) itemView.findViewById(R.id.animal_image);
        mNameTextView = (TextView) itemView.findViewById(R.id.name);
        mPosterTextView = (TextView) itemView.findViewById(R.id.poster);
        mDateTextView = (TextView) itemView.findViewById(R.id.date);
        mByCookTextView = (TextView) itemView.findViewById(R.id.by_cook);
        mLogoImageView = (ImageView) itemView.findViewById(R.id.user_image);
        mLikedTextView = (TextView) itemView.findViewById(R.id.liked);
        mCookedTextView = (TextView) itemView.findViewById(R.id.cooked);

    }

    public void setData(int imageResId, String name, String poster, String date, String liked, String cooked) {
        mImageView.setImageResource(imageResId);
        mNameTextView.setText(name);
        mPosterTextView.setText(poster);
        mDateTextView.setText(date);
        mByCookTextView.setText(poster);
        mLogoImageView.setImageResource(imageResId);
        mLikedTextView.setText(liked);
        mCookedTextView.setText(cooked);



    } */
}
