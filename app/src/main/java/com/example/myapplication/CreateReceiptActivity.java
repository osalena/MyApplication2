package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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

import java.io.IOException;


public class CreateReceiptActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView   imageView;
    private Button      loadImageButton;
    private EditText    editTextTitle;
    private EditText    editTextDescription;
    private Bitmap      bitmap = null;
    private InfoUser    user;

    //private ActionBar actionBar;
    public static final int GET_FROM_GALLERY = 1;

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
        loadImageButton.setOnClickListener(this);

        user = new InfoUser("Alena", null, "123");
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
                Toast.makeText(getApplicationContext(), "ghjgkhjg", Toast.LENGTH_SHORT);
               // createNewReceipt();
                //Intent intent = new Intent(CreateReceiptActivity.this, activity_main_cookpad.class);
                //CreateReceiptActivity.this.startActivity(intent);
                return true;
            case R.id.create_rec_save:

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void createNewReceipt() {
        InfoReceipt receipt = new InfoReceipt(editTextTitle.getText().toString(), editTextDescription.getText().toString(), bitmap);
        MyInfoManager.getInstance().createReceipt(user, receipt);
        //Toast.makeText(this, "ghjgkhjg", Toast.LENGTH_SHORT);
    }

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
           // else if (requestCode == REQUEST_CAMERA)
             //   onCaptureImageResult(data);
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
}
