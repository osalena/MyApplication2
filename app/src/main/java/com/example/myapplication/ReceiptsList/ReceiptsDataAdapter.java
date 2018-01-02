package com.example.myapplication.ReceiptsList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.dataBase.InfoReceipt;
import com.example.myapplication.dataBase.InfoUser;
import com.example.myapplication.dataBase.MyInfoManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;


public  class ReceiptsDataAdapter extends ArrayAdapter<InfoReceipt> {

    //private LayoutInflater mLayoutInflater;


   /* protected int[] mImageResIds;
    protected String[] mNames;
    protected String[] mDescriptions;
    protected String[] mPosters;
    protected String[] mDates;
    protected String[] mLiked;
    protected String[] mCooked; */
    private ImageView   likeImage;
    private ImageView   bookmarkedImage;
    private ImageView   sendImage;

    private TextView    title;
    private ImageView   receiptImage;
    private ImageView   userImage;
    private TextView    poster;

    protected Context   context;


    private List<InfoReceipt> receipts;



    public ReceiptsDataAdapter(Context context, int resource, List<InfoReceipt> objects) {
        super(context, resource, objects);
        this.context = context;
        receipts = objects;
        MyInfoManager.getInstance().openDataBase(context);


    }

    @Override
    public int getCount() {
        return receipts.size();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View rootView = mInflater.inflate(R.layout.recycler_item, null);
        MyInfoManager.getInstance().openDataBase(context);

        final InfoReceipt currentReceipt = getItem(position);

        title           =   (TextView) rootView.findViewById(R.id.name);
        receiptImage    =   (ImageView) rootView.findViewById(R.id.animal_image);
        userImage       =   (ImageView) rootView.findViewById(R.id.user_image);
        poster          =   (TextView) rootView.findViewById(R.id.poster);
        title.setText(currentReceipt.getTitle());
        receiptImage.setImageBitmap(currentReceipt.getImage());
        try{
        userImage.setImageBitmap(MyInfoManager.getInstance().readUser(currentReceipt.getUserId()).getImage1());
        poster.setText(MyInfoManager.getInstance().readUser(currentReceipt.getUserId()).getUsername());}
        catch (NullPointerException e){
            userImage = null;
            poster = null;
        }
        TextView date = (TextView) rootView.findViewById(R.id.date);
        //TO DO
        date.setText(null);
        TextView by_cook = (TextView) rootView.findViewById(R.id.by_cook);
        //TO DO
        by_cook.setText(null);
        TextView liked = (TextView) rootView.findViewById(R.id.liked);
        //TO DO
        liked.setText(null);
        TextView cooked = (TextView) rootView.findViewById(R.id.cooked);
        //TO DO
        cooked.setText(null);

        likeImage       =   (ImageView)rootView.findViewById(R.id.like_icon);
        sendImage       =   (ImageView)rootView.findViewById(R.id.send_icon);
        bookmarkedImage =   (ImageView)rootView.findViewById(R.id.bookmark_icon);


        //likeImage.setOnClickListener(likeImageOnClickListener);
        likeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Liked", Toast.LENGTH_SHORT).show();
            }
        });
        bookmarkedImage.setOnClickListener(bookmarkedOnClickListener);
        sendImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, "Sent", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                //intent.putExtra(Intent.EXTRA_EMAIL, addresses);
                intent.putExtra(Intent.EXTRA_SUBJECT, currentReceipt.getTitle());
                //File f = shareBitmap(currentReceipt.getImage1(), currentReceipt.getTitle());
               // FileProvider p = new FileProvider();
                //p.getUriForFile(f);
                //String pathofBmp = MediaStore.Images.Media.insertImage(context.getContentResolver(), currentReceipt.getImage1(),"title", null);
                //Uri attachment = Uri.parse(pathofBmp);
                //intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                String randomNameOfPic = Calendar.DAY_OF_YEAR+ DateFormat.getTimeInstance().toString();
                File file = new File(context.getCacheDir(), "slip"+  randomNameOfPic+ ".jpg");
                FileOutputStream fOut = null;
                try {
                    fOut = new FileOutputStream(file);
                    Bitmap myPic =  currentReceipt.getImage();
                    myPic.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                    fOut.flush();
                    fOut.close();
                    file.setReadable(true, false);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //String fileExtension = ".tmp";
                //File temporaryFile = File.createTempFile( fileName, fileExtension, context.getExternalCacheDir() );
                //FileUtils.copyFile(f, temporaryFile);
                //if (Uri.fromFile(f) != null) {
                    // Grant temporary read permission to the content URI
                    //intent.addFlags(
                            //Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    //intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(f));
                //}
                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                //intent.putExtra(Intent.EXTRA_TEXT, currentReceipt.getDescription());
                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent);
                }
            }
        });







/*
        ImageView deleteIcon = (ImageView) rootView.findViewById(R.id.deleteIcon);
        deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String title = "Delete Folder";
                final String msg = "Are you sure?";
                AlertDialog.Builder  builder=  new AlertDialog.Builder(context);
                builder.setTitle(title);
                builder.setMessage(msg);

                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        MyInfoManager.getInstance().deleteFolder(currentFolder);
                        InfoFolderListAdapter.this.remove(currentFolder);
                        InfoFolderListAdapter.this.notifyDataSetChanged();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog .show();
            }
        }); */


        return rootView;
    }

    private File shareBitmap (Bitmap bitmap, String fileName) {
        try {
            File file = new File(getContext().getCacheDir(), fileName + ".png");
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true, false);
            //final Intent intent = new Intent(     android.content.Intent.ACTION_SEND);
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            //intent.setType("image/png");
            //startActivity(intent);
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**************** LISTENERS ****************/

   /* private View.OnClickListener likeImageOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //TO DO
            Toast.makeText(context, "Liked", Toast.LENGTH_SHORT).show();
        }
    };*/


    private View.OnClickListener bookmarkedOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //TO DO
            Toast.makeText(context, "Marked", Toast.LENGTH_SHORT).show();
        }
    };





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



