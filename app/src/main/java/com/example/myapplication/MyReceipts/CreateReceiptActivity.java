package com.example.myapplication.MyReceipts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication.Interface.LoadListContainer;
import com.example.myapplication.Interface.LoadListener;
import com.example.myapplication.R;
import com.example.myapplication.dataBase.InfoReceipt;
import com.example.myapplication.dataBase.InfoUser;
import com.example.myapplication.dataBase.MyInfoManager;
import com.example.myapplication.utils.NetworkConnector;
import com.example.myapplication.utils.NetworkResListener;
import com.example.myapplication.utils.ResStatus;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class CreateReceiptActivity extends AppCompatActivity implements View.OnClickListener, NetworkResListener, LoadListener {

    private ImageView   imageView;
    private Button      loadImageButton;
    private EditText    editTextTitle;
    private EditText    editTextDescription;
    private Bitmap      bitmap = null;
    private File        output=null;
    private InfoUser    curUser;
    private Menu        menu;
    /* receipt ID from bind
    * -1 for new receipt*/
    private String      id;
    /*1 for non-editable*/
    private int         edit_flag = 0;
    private int userid;
    //private ActionBar actionBar;
    public  static final int GET_FROM_GALLERY = 1;
    private static final int REQUEST_TAKE_PHOTO = 111;
    private static final int PHOTO_W = 100;
    private static final int PHOTO_H = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        NetworkConnector.getInstance().initialize(this);
        final Context cont = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_receipt);
       MyInfoManager.getInstance().openDataBase(this);
        /* to display BACK button */
        android.app.ActionBar actionBar = getActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        //actionBar.setIcon(null);
        imageView           = (ImageView)findViewById(R.id.uploadImage);
        loadImageButton     = (Button)findViewById(R.id.creat_rec_add_photo);
        editTextDescription = (EditText)findViewById(R.id.editTextRecipeDescription);
        editTextTitle       = (EditText)findViewById(R.id.editTextRecipeTitle);
        imageView.setOnClickListener(this);
        loadImageButton.setOnClickListener(cameraOnClickListener);

        Bundle b = getIntent().getExtras();
        id = b.getString("id");

        edit_flag = b.getInt("edit_flag");
         /* get current user info */
       curUser= MyInfoManager.getInstance().readUser(String.valueOf(b.getInt("user")));
        userid = b.getInt("user");
        /* existing receipt to edit*/
        try {
            if(Integer.valueOf(id) > -1) {

                final InfoReceipt rec = new InfoReceipt();
                rec.setId(id);
                NetworkConnector.getInstance().sendRequestToServer(NetworkConnector.GET_RECEIPT_REQ,rec,this);
                NetworkConnector.getInstance().sendRequestToServer(NetworkConnector.GET_RECEIPT_IMAGE_REQ, rec, this);
            }
        }
        catch(NullPointerException e) {
            /* to display Receipt details without editing */
            if (edit_flag == 1) {
                editTextTitle.setEnabled(false);
                editTextDescription.setEnabled(false);
                imageView.setClickable(false);
                loadImageButton.setVisibility(View.INVISIBLE);
                //Toast.makeText(CreateReceiptActivity.this, rec.getDate(), Toast.LENGTH_LONG).show();
            }
        }
       // }



        getSupportActionBar().setTitle(curUser.getUsername());
    }


    /* Menu creator*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_create_receipt, menu);
        this.menu = menu;
        /* to display Receipt details without editing */
        if(edit_flag == 1) {
            menu.findItem(R.id.create_rec_save).setVisible(false);
            menu.findItem(R.id.create_rec_hat).setVisible(false);
        }
        //super.onCreateOptionsMenu(menu, inflater);
        return true;
    }

    /* Listener for menu's items*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.create_rec_hat:
                //Toast.makeText(getApplicationContext(), "ghjgkhjg", Toast.LENGTH_SHORT);
                //createNewReceipt();
                //Intent intent = new Intent(CreateReceiptActivity.this, activity_main_cookpad.class);
                //CreateReceiptActivity.this.startActivity(intent);
                return true;
            case R.id.create_rec_save:
                /* new receipt to add */
                if (id.equals("-1") && edit_flag == 0) {
                    /* photo is empty*/

//                    if(bitmap == null){
//
//                        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
//                        bitmap = drawable.getBitmap();
//                    }

                    InfoReceipt receipt = new InfoReceipt(editTextTitle.getText().toString(), editTextDescription.getText().toString(), bitmap);

                    receipt.setUserId(""+userid);

                   MyInfoManager.getInstance().createReceipt(curUser, receipt);
                   //NetworkConnector.getInstance().sendRequestToServer(NetworkConnector.INSERT_RECEIPT_REQ, receipt,this);

                    Toast.makeText(CreateReceiptActivity.this, getResources().getText(R.string.cr_recipe_save), Toast.LENGTH_SHORT).show();
                }
                /* existing receipt to edit */
                else if (id.equals("-1") && edit_flag == 0){
                    BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
                    Bitmap bitmap2 = drawable.getBitmap();
                    InfoReceipt updatedReceipt =  new InfoReceipt(editTextTitle.getText().toString(), editTextDescription.getText().toString(), bitmap2);
                    updatedReceipt.setId(id);
                    //MyInfoManager.getInstance().updateReceipt(updatedReceipt);
                    Toast.makeText(CreateReceiptActivity.this, getResources().getText(R.string.cr_recipe_edit), Toast.LENGTH_SHORT).show();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private View.OnClickListener cameraOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View arg0) {

            Intent i=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File dir= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "img_" + timeStamp + ".jpeg";
            output=new File(dir, imageFileName);
            i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output));

            startActivityForResult(i, REQUEST_TAKE_PHOTO);

        }
    };



   /* public void openGalleryOnClick (View view){
        //startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI),
        // GET_FROM_GALLERY);
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }*/

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GET_FROM_GALLERY)
                if(data != null) {
                   // Uri selectedImage = data.getData();
                    //imageView.setImageURI(selectedImage);
                    Uri imageUri = data.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageUri);
                        //bitmap = getScaledImageFromFilePath(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    imageView.setImageBitmap(bitmap);
                }
           else if (requestCode == REQUEST_TAKE_PHOTO){
                    try {

                            bitmap =  getScaledImageFromFilePath(output.getAbsolutePath());
                            if(bitmap!=null){
                                //imageView.setImageBitmap(bitmap);
                                //imageView.setVisibility(View.VISIBLE);
                                imageView.setImageResource(R.drawable.bookmark);
                            }
                            else if (bitmap == null){
                                imageView.setImageBitmap(null);
                            }


                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }

        }
    }

    @SuppressWarnings("deprecation")
   /* private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ivImage.setImageBitmap(bm);
    } */

   /* private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ivImage.setImageBitmap(thumbnail);
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.uploadImage:
                Intent galleryIntent = new Intent
                        (Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, GET_FROM_GALLERY);
                break;
            case R.id.creat_rec_add_photo:
                break;
        }

    }

    @Override
    protected void onResume() {
       // MyInfoManager.getInstance().openDataBase(this);
        super.onResume();

    }

    @Override
    protected void onPause() {
      //  MyInfoManager.getInstance().closeDataBase();
        super.onPause();
    }

    private void setImage(Bitmap bm) {
        imageView.setImageBitmap(bm);
    }

    private Bitmap getScaledImageFromFilePath(String imagePath) {
        // Get the dimensions of the View
        Bitmap scaledBitmap = null;
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);

            Matrix matrix = new Matrix();
            matrix.postRotate(90);

            Bitmap rotatedBitmap =  Bitmap.createScaledBitmap(bitmap, PHOTO_W, PHOTO_H, false);
            scaledBitmap = Bitmap.createBitmap(rotatedBitmap , 0, 0, rotatedBitmap.getWidth(), rotatedBitmap.getHeight(), matrix, true);

        } catch (Throwable e) {
            e.printStackTrace();
        }
        return scaledBitmap;
    }

    private Bitmap getScaledImageFromFilePath(Bitmap bitmap) {
        Bitmap scaledBitmap = null;
        //Bitmap d = new BitmapDrawable(ctx.getResources() , w.photo.getAbsolutePath()).getBitmap();
        Matrix matrix = new Matrix();
        matrix.postRotate(0);

        scaledBitmap =  Bitmap.createScaledBitmap(bitmap, 10, 10, false);

        // Get the dimensions of the View
        /*Bitmap scaledBitmap = null;
        try {


            Matrix matrix = new Matrix();
            matrix.postRotate(90);

            Bitmap rotatedBitmap =  Bitmap.createScaledBitmap(bitmap, PHOTO_W, PHOTO_H, false);
            scaledBitmap = Bitmap.createBitmap(rotatedBitmap , 0, 0, rotatedBitmap.getWidth(), rotatedBitmap.getHeight(), matrix, true);

        } catch (Throwable e) {
            e.printStackTrace();
        }*/
        return scaledBitmap;
    }

    @Override
    public void onPostLoad(List<LoadListContainer> list) {

    }

    @Override
    public void onPreUpdate() {

    }

    @Override
    public void onPostUpdate(byte[] res, ResStatus status) {
        String content = null;
        try{
            content = new String(res, "UTF-8");
            System.out.println(content);
        }
        catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }

        LoadListContainer.registerLoadListener(this);
        List<LoadListContainer> listOfReceipts = InfoReceipt.parseJson(content);
        List<InfoReceipt> r=new ArrayList<>();
        for (LoadListContainer l : listOfReceipts) {
            r.add((InfoReceipt)l);
            System.out.println("NEW RECEIPT: "+((InfoReceipt)l).getTitle());
        }
        System.out.println(content);
        editTextTitle.setText(r.get(0).getTitle());
        editTextDescription.setText(r.get(0).getDescription());
    }

    @Override
    public void onPostUpdate(JSONObject res, ResStatus status) {

    }

    @Override
    public void onPostUpdate(Bitmap res, ResStatus status) {
        Toast.makeText(CreateReceiptActivity.this,"Sync download img finished...status " + status.toString(),Toast.LENGTH_SHORT).show();
        if(status == ResStatus.SUCCESS){
            if(res!=null) {

                setImage(res);
                //MyInfoManager.getInstance().updateReceipt(rec);
            }
        }
    }


}
