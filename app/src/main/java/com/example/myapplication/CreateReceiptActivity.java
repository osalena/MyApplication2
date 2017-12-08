package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
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

import com.example.myapplication.dataBase.InfoReceipt;
import com.example.myapplication.dataBase.InfoUser;
import com.example.myapplication.dataBase.MyInfoManager;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CreateReceiptActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView   imageView;
    private Button      loadImageButton;
    private EditText    editTextTitle;
    private EditText    editTextDescription;
    private Bitmap      bitmap = null;
    private File        output=null;
    private InfoUser    user;


    //private ActionBar actionBar;
    public  static final int GET_FROM_GALLERY = 1;
    private static final int REQUEST_TAKE_PHOTO = 111;
    private static final int PHOTO_W = 500;
    private static final int PHOTO_H = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_receipt);
        /* to display BACK button */
        android.app.ActionBar actionBar = getActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //actionBar.setIcon(null);
        imageView           = (ImageView)findViewById(R.id.uploadImage);
        loadImageButton     = (Button)findViewById(R.id.creat_rec_add_photo);
        editTextDescription = (EditText)findViewById(R.id.editTextRecipeDescription);
        editTextTitle       = (EditText)findViewById(R.id.editTextRecipeTitle);
        imageView.setOnClickListener(this);
        loadImageButton.setOnClickListener(cameraOnClickListener);

        user = new InfoUser("Test", null, "123");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_create_receipt, menu);
        return true;
    }

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
                InfoReceipt receipt = new InfoReceipt(editTextTitle.getText().toString(), editTextDescription.getText().toString(), bitmap);
                MyInfoManager.getInstance().createReceipt(user, receipt);
                Toast.makeText(CreateReceiptActivity.this, getResources().getText(R.string.cr_recipe_save), Toast.LENGTH_SHORT).show();
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
        MyInfoManager.getInstance().openDataBase(this);
        super.onResume();

    }

    @Override
    protected void onPause() {
        MyInfoManager.getInstance().closeDataBase();
        super.onPause();
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
}
