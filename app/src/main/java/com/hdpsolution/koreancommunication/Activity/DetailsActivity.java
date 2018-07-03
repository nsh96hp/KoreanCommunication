package com.hdpsolution.koreancommunication.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hdpsolution.koreancommunication.Adapter.SearchResultsListAdapter;
import com.hdpsolution.koreancommunication.R;
import com.hdpsolution.koreancommunication.Utils.KUtils;
import com.hdpsolution.koreancommunication.data.DataFav;
import com.hdpsolution.koreancommunication.data.DatabaseHelper;
import com.hdpsolution.koreancommunication.data.Korean;

import java.io.IOException;
import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity {
    private int category_key;
    private int language;
    private DatabaseHelper db;
    private DataFav dbf;
    private ArrayList<Korean> arrKR;
    private ArrayList<Korean> arrFAV;
    private ArrayList<Korean> lstFAV;
    private ArrayList<Korean> lstKR;
    private RecyclerView rcv_details;
    private SearchResultsListAdapter adapter;
    private boolean isSound=true;

    private SharedPreferences preLanguage;

    private MediaPlayer m;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = findViewById(R.id.d_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findID();
        Intent intent = getIntent();
        category_key = intent.getIntExtra(KUtils.CATEGORY_KEY, -1);

        preLanguage = getSharedPreferences(KUtils.LANGUAGE_KEY, MODE_PRIVATE);
        language = preLanguage.getInt(KUtils.L_KEY, 0);

        dbf=new DataFav(DetailsActivity.this);
        arrFAV=new ArrayList<>();
        arrFAV=dbf.GetAllFAV();
        adapter=new SearchResultsListAdapter(language,DetailsActivity.this,arrFAV);
        rcv_details.setAdapter(adapter);
        rcv_details.setLayoutManager(new LinearLayoutManager(DetailsActivity.this));
        handleCategory(category_key);


    }

    private void findID() {
        rcv_details=findViewById(R.id.rcv_details);
    }

    private void handleCategory(int category_key) {
        db = new DatabaseHelper(DetailsActivity.this, getFilesDir().getAbsolutePath());
        try {
            db.prepareDataBase();
        } catch (IOException io) {
            throw new Error("Unable to create Database");
        }
        arrKR=new ArrayList<>();
        switch (category_key) {
            case 0:
                getSupportActionBar().setTitle(getResources().getString(R.string.Favorites));
                arrKR=db.getAllKorean();
                lstFAV=new ArrayList<>();
                lstKR=new ArrayList<>();
                lstFAV=dbf.GetFAV();
                for (int i=0;i<lstFAV.size();i++){
                    lstKR.add(arrKR.get(lstFAV.get(i).get_id()-1));
                }
                arrKR.clear();
                arrKR=lstKR;
                break;
            case 1:
                getSupportActionBar().setTitle(getResources().getString(R.string.General_Conversation));
                arrKR=db.getKoreanByCategory(1);
                break;
            case 2:
                getSupportActionBar().setTitle(getResources().getString(R.string.Greetings));
                arrKR=db.getKoreanByCategory(2);
                break;
            case 3:
                getSupportActionBar().setTitle(getResources().getString(R.string.Numbers));
                arrKR=db.getKoreanByCategory(3);
                break;
            case 4:
                getSupportActionBar().setTitle(getResources().getString(R.string.Time_and_Date));
                arrKR=db.getKoreanByCategory(4);
                break;
            case 5:
                getSupportActionBar().setTitle(getResources().getString(R.string.Directions_Places));
                arrKR=db.getKoreanByCategory(5);
                break;
            case 6:
                getSupportActionBar().setTitle(getResources().getString(R.string.Transportation));
                arrKR=db.getKoreanByCategory(6);
                break;
            case 7:
                getSupportActionBar().setTitle(getResources().getString(R.string.Accommodation));
                arrKR=db.getKoreanByCategory(7);
                break;
            case 8:
                getSupportActionBar().setTitle(getResources().getString(R.string.Eating_Out));
                arrKR=db.getKoreanByCategory(8);
                break;
            case 9:
                getSupportActionBar().setTitle(getResources().getString(R.string.Shopping));
                arrKR=db.getKoreanByCategory(9);
                break;
            case 10:
                getSupportActionBar().setTitle(getResources().getString(R.string.Colours));
                arrKR=db.getKoreanByCategory(10);
                break;
            case 11:
                getSupportActionBar().setTitle(getResources().getString(R.string.Cities_and_Provinces));
                arrKR=db.getKoreanByCategory(11);
                break;
            case 12:
                getSupportActionBar().setTitle(getResources().getString(R.string.Countries));
                arrKR=db.getKoreanByCategory(12);
                break;
            case 13:
                getSupportActionBar().setTitle(getResources().getString(R.string.Tourist_Attractions));
                arrKR=db.getKoreanByCategory(13);
                break;
            case 14:
                getSupportActionBar().setTitle(getResources().getString(R.string.Family));
                arrKR=db.getKoreanByCategory(14);
                break;
            case 15:
                getSupportActionBar().setTitle(getResources().getString(R.string.Dating));
                arrKR=db.getKoreanByCategory(15);
                break;
            case 16:
                getSupportActionBar().setTitle(getResources().getString(R.string.Emergency));
                arrKR=db.getKoreanByCategory(16);
                break;
            case 17:
                getSupportActionBar().setTitle(getResources().getString(R.string.Feeling_Sick));
                arrKR=db.getKoreanByCategory(17);
                break;
        }
        adapter.swapData(arrKR);
        adapter.setItemsOnClickListener(new SearchResultsListAdapter.OnItemClickListener() {
            @Override
            public void onClick(Korean korean, int position) {
                adapter.Touch(position);
            }
        });

        adapter.setItemsOnClickSpeakerListener(new SearchResultsListAdapter.OnSpeakerClickListener() {
            @Override
            public void onClick(final Korean korean, int pos) {

                final Dialog dialog= new Dialog(DetailsActivity.this, R.style.mydialogstyle);
                dialog.setContentView(R.layout.dialog_speak);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);
                m=new MediaPlayer();

                TextView txt_name,txt_namekr,txt_pinyin_speak,txtResult;
                ImageButton btn_speaker, btn_sound;
                Button btn_exit;

                txt_name =dialog.findViewById(R.id.txt_name);
                txt_namekr =dialog.findViewById(R.id.txt_namekr);
                txt_pinyin_speak =dialog.findViewById(R.id.txt_pinyin_speak);
                btn_speaker =dialog.findViewById(R.id.btn_speaker);
                btn_sound =dialog.findViewById(R.id.btn_sound);
                btn_exit =dialog.findViewById(R.id.btn_exit);
                txtResult =dialog.findViewById(R.id.txtResult);
                
                String newText= editText(korean.getTextKR());
                Log.e("NEW TEXT", newText);

                switch (language){
                    case 0:
                        txt_name.setText(korean.getTextVN());
                        break;
                    case 1:
                        txt_name.setText(korean.getTextCH());
                        break;
                    case 2:
                        txt_name.setText(korean.getTextEN());
                        break;
                }

                txt_namekr.setText(korean.getTextKR());
                txt_pinyin_speak.setText(korean.getPinyin());

                btn_exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                btn_sound.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playSound(korean.getVoice());
                    }
                });
                btn_speaker.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        m.stop();
                        m.release();
                        m=new MediaPlayer();
                    }
                });

                dialog.show();
            }
        });

    }

    private String editText(String text) {
        StringBuilder newText= new StringBuilder();
        for (int i=0;i<text.length();i++){
            String temp=text.charAt(i)+"";
            if(temp.equals(".") || temp.equals("?") || temp.equals(".")){
                //K lay
            }else {
                newText.append(text.charAt(i));
            }
        }
        return newText+"";
    }

    public void playSound(String fileName) {
        AssetFileDescriptor descriptor = null;
        if (m.isPlaying()) {
            m.pause();
            m.stop();
            m.release();
            m = new MediaPlayer();        }

        try {
            m.release();
            m = new MediaPlayer();
            descriptor = getAssets().openFd(fileName + "_f.ogg");
            m.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            descriptor.close();
            m.prepare();
            m.setVolume(1f, 1f);
            m.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.menu_detail,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.action_voice_detail:
                int icon = 0;
                if(isSound){
                    icon=R.drawable.ic_volume_off;
                    isSound=false;
                    adapter.changeSound();
                }else {
                    icon=R.drawable.ic_vol_dialog;
                    isSound=true;
                    adapter.changeSound();
                }
                item.setIcon(icon);
                break;
            case R.id.action_test:

                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
