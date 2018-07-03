package com.hdpsolution.koreancommunication.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.hdpsolution.koreancommunication.Utils.KUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {


    private final static String TAG = "DatabaseHelper";
    private SQLiteDatabase myDataBase;
    private String myPath;
    private Context myContext;


    public DatabaseHelper(Context context) {
        super(context, KUtils.DATABASE_NAME, null, KUtils.VERSION_NAME);
    }

    public DatabaseHelper(Context context, String filePath) {
        super(context, KUtils.DATABASE_NAME, null, KUtils.VERSION_NAME);
        this.myContext = context;
        myPath = String.valueOf(filePath + "/" + KUtils.DATABASE_NAME);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void prepareDataBase() throws IOException {
        //here we are checking database already on specified path or not
        boolean dbExist = checkDataBase();
        if (dbExist) {
            Log.d(TAG, "Database exists.");
            try {
                copyDataBase();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
        } else {
            Log.d(TAG, "Database not exists.");
            try {
                copyDataBase();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }

    private boolean checkDataBase() {
        boolean checkDB = false;
        try {
            File dbFile = new File(myPath);
            checkDB = dbFile.exists();
        } catch (SQLiteException ignored) {
        }
        return checkDB;
    }

    private void copyDataBase() throws IOException {
        OutputStream myOutput = new FileOutputStream(myPath);
        InputStream myInput = myContext.getAssets().open("databases/" + KUtils.DATABASE_NAME);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    //Opening DB and Getting DATA from DB
    public ArrayList<Korean> getAllKorean() {
        myDataBase = SQLiteDatabase.openDatabase(myPath,
                null, SQLiteDatabase.OPEN_READONLY);
        Log.e("Path", myPath);
        String query = "SELECT * FROM " + KUtils.TABlE_PHRASE + ";";
        Cursor cursor = myDataBase.rawQuery(query, null);
        ArrayList<Korean> arrKR = new ArrayList<>();
        while (cursor.moveToNext()) {
            Korean k = new Korean(cursor.getInt(0), cursor.getInt(1),
                    cursor.getString(8), cursor.getString(2),
                    cursor.getString(9), cursor.getString(4), cursor.getString(3)
                    , cursor.getString(6), cursor.getInt(5), cursor.getInt(7));
            arrKR.add(k);
        }
        myDataBase.close();
        cursor.close();
        return arrKR;
    }

    //Opening DB and Getting DATA from DB
    public Korean getKorean(int id) {
        myDataBase = SQLiteDatabase.openDatabase(myPath,
                null, SQLiteDatabase.OPEN_READONLY);
        Log.e("Path", myPath);
        String query = "SELECT * FROM " + KUtils.TABlE_PHRASE + " WHERE " + KUtils._id + " = " + id + ";";
        Cursor cursor = myDataBase.rawQuery(query, null);

        cursor.moveToFirst();
        Korean k = new Korean(cursor.getInt(0), cursor.getInt(1),
                cursor.getString(8), cursor.getString(2),
                cursor.getString(9), cursor.getString(4), cursor.getString(3)
                , cursor.getString(6), cursor.getInt(5), cursor.getInt(7));

        myDataBase.close();
        cursor.close();
        return k;
    }

    public ArrayList<Korean> getKoreanByCategory(int category_id) {
        myDataBase = SQLiteDatabase.openDatabase(myPath,
                null, SQLiteDatabase.OPEN_READONLY);
        String query = "SELECT * FROM " + KUtils.TABlE_PHRASE + " WHERE " + KUtils.category_id + "=" + category_id + ";";
        Cursor cursor = myDataBase.rawQuery(query, null);
        ArrayList<Korean> arrKR = new ArrayList<>();
        while (cursor.moveToNext()) {
            Korean k = new Korean(cursor.getInt(0), cursor.getInt(1),
                    cursor.getString(8), cursor.getString(2),
                    cursor.getString(9), cursor.getString(4), cursor.getString(3)
                    , cursor.getString(6), cursor.getInt(5), cursor.getInt(7));
            arrKR.add(k);
        }
        myDataBase.close();
        cursor.close();
        return arrKR;
    }

    public ArrayList<Korean> getKoreanFavorite() {
        myDataBase = SQLiteDatabase.openDatabase(myPath,
                null, SQLiteDatabase.OPEN_READONLY);
        String query = "SELECT * FROM " + KUtils.TABlE_PHRASE + " WHERE " + KUtils.favorite + "=" + 1 + ";";
        Cursor cursor = myDataBase.rawQuery(query, null);
        ArrayList<Korean> arrKR = new ArrayList<>();
        while (cursor.moveToNext()) {
            Korean k = new Korean(cursor.getInt(0), cursor.getInt(1),
                    cursor.getString(8), cursor.getString(2),
                    cursor.getString(9), cursor.getString(4), cursor.getString(3)
                    , cursor.getString(6), cursor.getInt(5), cursor.getInt(7));
            arrKR.add(k);
        }
        myDataBase.close();
        cursor.close();
        return arrKR;
    }

}
