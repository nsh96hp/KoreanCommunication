package com.hdpsolution.koreancommunication.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Spinner;

import com.hdpsolution.koreancommunication.R;
import com.hdpsolution.koreancommunication.Utils.KUtils;
import com.hdpsolution.koreancommunication.data.DataFav;
import com.hdpsolution.koreancommunication.data.DatabaseHelper;
import com.hdpsolution.koreancommunication.data.Korean;

import java.io.IOException;
import java.util.ArrayList;

public class SplashScreen extends AppCompatActivity {
    private ArrayList<Korean> arrKR;
    private ArrayList<Korean> arrFAV;

    private DatabaseHelper db;
    private DataFav dbf;

    private int keyFav = 0;
    private SharedPreferences preLanguage;
    private SharedPreferences.Editor editLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        Thread myThread=new Thread(){
            @Override
            public void run() {
                super.run();

                preLanguage = getSharedPreferences(KUtils.LANGUAGE_KEY, MODE_PRIVATE);
                keyFav = preLanguage.getInt(KUtils.F_KEY, 0);

                db = new DatabaseHelper(SplashScreen.this, getFilesDir().getAbsolutePath());
                try {
                    db.prepareDataBase();
                } catch (IOException io) {
                    throw new Error("Unable to create Database");
                }
                arrKR = new ArrayList<>();
                arrKR = db.getAllKorean();

                dbf=new DataFav(SplashScreen.this);
                arrFAV=new ArrayList<>();
                if(keyFav==0){
                    for (int i=0;i<arrKR.size();i++){
                        dbf.Add_Data(arrKR.get(i));
                        arrFAV.add(arrKR.get(i));

                        preLanguage = getSharedPreferences(KUtils.LANGUAGE_KEY, MODE_PRIVATE);
                        editLanguage = preLanguage.edit();
                        editLanguage.putInt(KUtils.F_KEY,1);
                        editLanguage.commit();
                    }
                }

                try {
                    sleep(KUtils.TIME_SPLASH_SCREEN);
                    Intent intent= new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();
    }
}
