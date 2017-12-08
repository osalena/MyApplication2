package com.example.myapplication.dataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;



public class MyDataBase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "MyDB";

    // Users
    private static final String TABLE_USERS_NAME = "users";
    private static final String USERS_COLUMN_ID = "id";
    private static final String USERS_COLUMN_USERNAME = "username";
    private static final String USERS_COLUMN_USERPIC = "userpic";
    private static final String USERS_COLUMN_PASSWORD = "password";
    private static final String[] TABLE_USER_COLUMNS = {USERS_COLUMN_ID, USERS_COLUMN_USERNAME, USERS_COLUMN_USERPIC,
            USERS_COLUMN_PASSWORD};

    // receipt table
    private static final String TABLE_RECEIPTS_NAME = "receipts";
    private static final String RECEIPT_COLUMN_ID = "id";
    private static final String RECEIPT_COLUMN_TITLE = "title";
    private static final String RECEIPT_COLUMN_DESCRIPTION = "description";
    private static final String RECEIPT_COLUMN__IMAGE1 = "image1";
    private static final String RECEIPT_COLUMN_USERID = "user_id";

    private static final String[] TABLE_RECEIPT_COLUMNS = {RECEIPT_COLUMN_ID, RECEIPT_COLUMN_TITLE,
            RECEIPT_COLUMN_DESCRIPTION, RECEIPT_COLUMN__IMAGE1, RECEIPT_COLUMN_USERID};


    private SQLiteDatabase db = null;

    public MyDataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            // SQL statement to create receipts table
            String CREATE_RECEIPTS_TABLE = "create table if not exists " + TABLE_RECEIPTS_NAME +" ( "
                    + RECEIPT_COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + RECEIPT_COLUMN_TITLE +" TEXT, "
                    + RECEIPT_COLUMN_DESCRIPTION + " TEXT, "
                    + RECEIPT_COLUMN__IMAGE1 + " BLOB, "
                    + RECEIPT_COLUMN_USERID + " INTEGER)";
            db.execSQL(CREATE_RECEIPTS_TABLE);

            if (!isTableExist(TABLE_USERS_NAME, db)) {
                // SQL statement to create users table
                String CREATE_USERS_TABLE = "create table if not exists "+ TABLE_USERS_NAME+" ( "
                        + USERS_COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + USERS_COLUMN_USERNAME + " TEXT, "
                        + USERS_COLUMN_USERPIC + " BLOB, "
                        + USERS_COLUMN_PASSWORD + " TEXT)";

                db.execSQL(CREATE_USERS_TABLE);
            }


        } catch (Throwable t) {
            t.printStackTrace();
        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            // drop item table if already exists
            //db.execSQL("DROP TABLE IF EXISTS items");
            //db.execSQL("DROP TABLE IF EXISTS folders");
        } catch (Throwable t) {
            t.printStackTrace();
        }
        //onCreate(db);
    }

    public void createReceipt(InfoUser user,InfoReceipt receipt) {

        try {
            // make values to be inserted
            ContentValues values = new ContentValues();
            values.put(RECEIPT_COLUMN_TITLE, receipt.getTitle());
            values.put(RECEIPT_COLUMN_DESCRIPTION, receipt.getDescription());
            values.put(RECEIPT_COLUMN_USERID, user.getId());

            //images
            Bitmap image1 = receipt.getImage1();
            if (image1 != null) {
                byte[] data = getBitmapAsByteArray(image1);
                if (data != null && data.length > 0) {
                    values.put(RECEIPT_COLUMN__IMAGE1, data);
                }
            }

            // insert item
            db.insert(TABLE_RECEIPTS_NAME, null, values);


        } catch (Throwable t) {
            t.printStackTrace();
        }


    }

    private byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    public void createUser(InfoUser user) {

        try {
            // make values to be inserted
            ContentValues values = new ContentValues();
            values.put(USERS_COLUMN_USERNAME, user.getUsername());
            //images
            Bitmap image1 = user.getImage1();
            if (image1 != null) {
                byte[] data = getBitmapAsByteArray(image1);
                if (data != null && data.length > 0) {
                    values.put(USERS_COLUMN_USERPIC, data);
                }
            }
            values.put(USERS_COLUMN_PASSWORD, user.getPassword());
            // insert folder
            db.insert(TABLE_USERS_NAME, null, values);
        } catch (Throwable t) {
            t.printStackTrace();
        }

    }


    public InfoReceipt readReceipt(int id) {
        InfoReceipt receipt = null;
        Cursor cursor = null;
        try {
            /// get reference of the itemDB database

            // get  query
            cursor = db
                    .query(TABLE_RECEIPTS_NAME,
                            TABLE_RECEIPT_COLUMNS, RECEIPT_COLUMN_ID + " = ?",
                            new String[] { String.valueOf(id) }, null, null,
                            null, null);



            // if results !=null, parse the first one
            if(cursor!=null && cursor.getCount()>0){

                cursor.moveToFirst();

                receipt = new InfoReceipt();
                receipt.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(RECEIPT_COLUMN_ID))));
                receipt.setTitle(cursor.getString(1));
                receipt.setDescription(cursor.getString(2));

                //images
                byte[] img1Byte = cursor.getBlob(3);
                if (img1Byte != null && img1Byte.length > 0) {
                    Bitmap image1 = BitmapFactory.decodeByteArray(img1Byte, 0, img1Byte.length);
                    if (image1 != null) {
                        receipt.setImage1(image1);
                    }
                }


                receipt.setUserId(Integer.parseInt(cursor.getString(4)));
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        finally {
            if(cursor!=null){
                cursor.close();
            }
        }
        return receipt;
    }

    public InfoUser readUserByUserName(String userName) {
        InfoUser user = null;
        Cursor cursor = null;
        try {
            // get reference of the folderDB database

            // get  query
            cursor = db
                    .query(TABLE_USERS_NAME, // a. table
                            TABLE_USER_COLUMNS, USERS_COLUMN_USERNAME + " = ?",
                            new String[] { userName }, null, null,
                            null, null);

            // if results !=null, parse the first one
            if (cursor != null)
                cursor.moveToFirst();

            user = new InfoUser();
            user.setId(Integer.parseInt(cursor.getString(0)));
            user.setUsername(cursor.getString(1));

            //images
            byte[] img1Byte = cursor.getBlob(2);
            if (img1Byte != null && img1Byte.length > 0) {
                Bitmap image1 = BitmapFactory.decodeByteArray(img1Byte, 0, img1Byte.length);
                if (image1 != null) {
                    user.setImage1(image1);
                }
                else user.setImage1(null);
            }

            user.setPassword(cursor.getString(3));

        } catch (Throwable t) {
            t.printStackTrace();
        }
        finally {
            if(cursor!=null){
                cursor.close();
            }
        }
        return user;
    }

    public InfoUser readUser(int id) {
        InfoUser user = null;
        Cursor cursor = null;
        try {
            // get reference of the folderDB database

            // get  query
            cursor = db
                    .query(TABLE_USERS_NAME, // a. table
                            TABLE_USER_COLUMNS, RECEIPT_COLUMN_ID + " = ?",
                            new String[] { String.valueOf(id) }, null, null,
                            null, null);

            // if results !=null, parse the first one
            if (cursor != null)
                cursor.moveToFirst();

            user = new InfoUser();
            user.setId(Integer.parseInt(cursor.getString(0)));
            user.setUsername(cursor.getString(1));

            //images
            byte[] img1Byte = cursor.getBlob(2);
            if (img1Byte != null && img1Byte.length > 0) {
                Bitmap image1 = BitmapFactory.decodeByteArray(img1Byte, 0, img1Byte.length);
                if (image1 != null) {
                    user.setImage1(image1);
                }
            }

            user.setPassword(cursor.getString(3));

        } catch (Throwable t) {
            t.printStackTrace();
        }
        finally {
            if(cursor!=null){
                cursor.close();
            }
        }
        return user;
    }

    public List<InfoReceipt> getAllReceipts() {
        List<InfoReceipt> result = new ArrayList<InfoReceipt>();
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_RECEIPTS_NAME, TABLE_RECEIPT_COLUMNS, null, null,
                    null, null, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                InfoReceipt receipt = cursorToReceipt(cursor);
                result.add(receipt);
                cursor.moveToNext();
            }

        } catch (Throwable t) {
            t.printStackTrace();
        }
        finally {
            // make sure to close the cursor
            if(cursor!=null){
                cursor.close();
            }
        }

        return result;
    }

    private InfoReceipt cursorToReceipt(Cursor cursor) {
        InfoReceipt result = new InfoReceipt();
        try {
            result.setId(Integer.parseInt(cursor.getString(0)));
            result.setTitle(cursor.getString(1));
            result.setDescription(cursor.getString(2));

            //images
            byte[] img1Byte = cursor.getBlob(3);
            if (img1Byte != null && img1Byte.length > 0) {
                Bitmap image1 = BitmapFactory.decodeByteArray(img1Byte, 0, img1Byte.length);
                if (image1 != null) {
                    result.setImage1(image1);
                }
            }

            result.setUserId(Integer.parseInt(cursor.getString(4)));
        } catch (Throwable t) {
            t.printStackTrace();
        }

        return result;
    }

    public List<InfoUser> getAllUsers() {
        List<InfoUser> result = new ArrayList<InfoUser>();
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_USERS_NAME, TABLE_USER_COLUMNS, null, null,
                    null, null, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                InfoUser user = cursorToUser(cursor);
                result.add(user);
                cursor.moveToNext();
            }

        } catch (Throwable t) {
            t.printStackTrace();
        }
        finally {
            // make sure to close the cursor
            if(cursor!=null){
                cursor.close();
            }
        }
        return result;
    }

    private InfoUser cursorToUser(Cursor cursor) {
        InfoUser result = new InfoUser();
        try {
            result.setId(Integer.parseInt(cursor.getString(0)));
            result.setUsername(cursor.getString(1));
            //images
            byte[] img1Byte = cursor.getBlob(2);
            if (img1Byte != null && img1Byte.length > 0) {
                Bitmap image1 = BitmapFactory.decodeByteArray(img1Byte, 0, img1Byte.length);
                if (image1 != null) {
                    result.setImage1(image1);
                }
            }
            result.setPassword(cursor.getString(3));
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return result;
    }

    public int updateReceipt(InfoReceipt receipt) {
        int cnt = 0;
        try {

            // make values to be inserted
            ContentValues values = new ContentValues();
            values.put(RECEIPT_COLUMN_TITLE, receipt.getTitle());
            values.put(RECEIPT_COLUMN_DESCRIPTION, receipt.getDescription());

            //images
            Bitmap image1 = receipt.getImage1();
            if (image1 != null) {
                byte[] data = getBitmapAsByteArray(image1);
                if (data != null && data.length > 0) {
                    values.put(RECEIPT_COLUMN__IMAGE1, data);
                }
            }
            else{
                values.putNull(RECEIPT_COLUMN__IMAGE1);
            }

            // update
            cnt = db.update(TABLE_RECEIPTS_NAME, values, RECEIPT_COLUMN_ID + " = ?",
                    new String[] { String.valueOf(receipt.getId()) });
        } catch (Throwable t) {
            t.printStackTrace();
        }

        return cnt;
    }

    public int updateUser(InfoUser user) {
        int i = 0;
        try {


            // make values to be inserted
            ContentValues values = new ContentValues();
            values.put(USERS_COLUMN_USERNAME, user.getUsername());

            // update
            i = db.update(TABLE_USERS_NAME, values, USERS_COLUMN_ID + " = ?",
                    new String[] { String.valueOf(user.getId()) });
        } catch (Throwable t) {
            t.printStackTrace();
        }

        return i;
    }

    public void deleteReceipt(InfoReceipt receipt) {

        try {

            // delete item
            db.delete(TABLE_RECEIPTS_NAME, RECEIPT_COLUMN_ID + " = ?",
                    new String[] { String.valueOf(receipt.getId()) });
        } catch (Throwable t) {
            t.printStackTrace();
        }

    }

    public void deleteUser(InfoUser user) {
        boolean succeded = false;
        try {

            // delete folder
            int rowAffected = db.delete(TABLE_USERS_NAME, USERS_COLUMN_ID + " = ?",
                    new String[] { String.valueOf(user.getId()) });
            if(rowAffected>0) {
                succeded = true;
            }

        } catch (Throwable t) {
            succeded = false;
            t.printStackTrace();
        } finally {
            if(succeded){
                deleteUserItems(user);
            }
        }

    }

    private void deleteUserItems(InfoUser user) {

        try {

            // delete items
            db.delete(TABLE_RECEIPTS_NAME, RECEIPT_COLUMN_USERID + " = ?",
                    new String[] { String.valueOf(user.getId()) });
        } catch (Throwable t) {
            t.printStackTrace();
        }

    }

    private boolean isTableExist(String name, SQLiteDatabase db) {

        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+ name + "'", null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }


    public List<InfoReceipt> getAllReceiptsOfUser(InfoUser user) {
        List<InfoReceipt> result = new ArrayList<InfoReceipt>();
        Cursor cursor = null;
        try {
            int floderId = user.getId();
            cursor = db.query(TABLE_RECEIPTS_NAME, TABLE_RECEIPT_COLUMNS, RECEIPT_COLUMN_USERID +" = ?",
                    new String[] { String.valueOf(floderId) }, null, null,
                    null, null);

            if(cursor!=null && cursor.getCount()>0){
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    InfoReceipt receipt = cursorToReceipt(cursor);
                    result.add(receipt);
                    cursor.moveToNext();
                }
            }

        } catch (Throwable t) {
            t.printStackTrace();
        }
        finally {
            if(cursor!=null) {
                // make sure to close the cursor
                cursor.close();
            }
        }
        return result;
    }

    public void open() {
        try {
            db = getWritableDatabase();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public void close() {
        try {
            db.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
