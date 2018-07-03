package com.hdpsolution.koreancommunication.Activity.lockscreen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.hdpsolution.koreancommunication.R;
import com.hdpsolution.koreancommunication.Utils.KUtils;

public class LockScreenSetting extends AppCompatActivity {
    SharedPreferences preferences;
    public static boolean isCandraw;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_setting);

        Toolbar toolbar = findViewById(R.id.toolbar_lock_setting);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.locktitle);

        preferences = getSharedPreferences(KUtils.LANGUAGE_KEY, MODE_PRIVATE);
        canDrawOverlay();
        initData();
        initView();
    }

    /**
     * Lấy dữ liệu
     */
    private void initData() {

    }

    /**
     * View
     */
    private void initView() {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_lock, new PreferFragment())
                .commit();

    }

    public void canDrawOverlay() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (!Settings.canDrawOverlays(getApplicationContext())) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                /** request permission via start activity for result */
                startActivityForResult(intent, 1);
            }else {
                isCandraw=true;
            }
        }
    }

    @RequiresApi(23)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (Settings.canDrawOverlays(this)) {
                // continue here - permission was granted
                if(isCandraw != true){
                    isCandraw = true;
                }
            }
        }
    }

    /**
     * Cập nhạt trạng tháu của view
     */
    private void upDateGui() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
