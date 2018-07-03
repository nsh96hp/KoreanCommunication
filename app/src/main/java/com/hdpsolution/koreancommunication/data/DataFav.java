package com.hdpsolution.koreancommunication.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hdpsolution.koreancommunication.Utils.KUtils;

import java.util.ArrayList;

public class DataFav extends SQLiteOpenHelper {

    private Context mContext;
    public DataFav(Context context) {
        super(context, KUtils.DATABASE_NAME, null, KUtils.VERSION_NAME);
        this.mContext=context;
    }

    private String CREATE_TABLE_FAV="CREATE TABLE "+KUtils.TABlE_FAV+"(\n"+
            KUtils.fav_ID+" INTEGER, \n"+
            KUtils.favorite+" INTEGER );\n";


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_FAV);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void Add_Data(Korean korean){
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues values= new ContentValues();
        values.put(KUtils.fav_ID,korean.get_id());
        values.put(KUtils.favorite,0);
        db.insert(KUtils.TABlE_FAV,null,values);
        db.close();
    }
    public ArrayList<Korean> GetAllFAV(){
        SQLiteDatabase db= this.getWritableDatabase();
        String sql="SELECT * FROM "+KUtils.TABlE_FAV+";";
        Cursor cursor = db.rawQuery(sql,null);
        ArrayList<Korean> lst= new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                Korean k=new Korean(cursor.getInt(0),cursor.getInt(1));
                lst.add(k);
            }while (cursor.moveToNext());
        }
        db.close();
        return lst;
    }

    public ArrayList<Korean> GetFAV(){
        SQLiteDatabase db= this.getWritableDatabase();
    String sql="SELECT * FROM "+KUtils.TABlE_FAV+" WHERE "+KUtils.favorite+" = 1;";
    Cursor cursor = db.rawQuery(sql,null);
    ArrayList<Korean> lst= new ArrayList<>();
        if(cursor.moveToFirst()){
        do{
            Korean k=new Korean(cursor.getInt(0),cursor.getInt(1));
            lst.add(k);
        }while (cursor.moveToNext());
    }
        db.close();
        return lst;
}

    public void Handle_Fav(int key){
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues values= new ContentValues();
        values.put(KUtils.favorite,1);
        db.update(KUtils.TABlE_FAV,values,""+KUtils.fav_ID+"="+key,null);
        db.close();
    }

    public void Handle_UnFav(int key){
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues values= new ContentValues();
        values.put(KUtils.favorite,0);
        db.update(KUtils.TABlE_FAV,values,""+KUtils.fav_ID+"="+key,null);
        db.close();
    }
}
