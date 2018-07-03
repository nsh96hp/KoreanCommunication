package com.hdpsolution.koreancommunication.Service;

import android.app.Dialog;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.hdpsolution.koreancommunication.Activity.MainActivity;
import com.hdpsolution.koreancommunication.Adapter.IOSPassCodeAdapter;
import com.hdpsolution.koreancommunication.R;
import com.hdpsolution.koreancommunication.Utils.KUtils;
import com.hdpsolution.koreancommunication.data.DatabaseHelper;
import com.hdpsolution.koreancommunication.data.Korean;
import com.nirigo.mobile.view.passcode.PasscodeIndicator;
import com.nirigo.mobile.view.passcode.PasscodeView;

import java.io.IOException;
import java.util.Random;

public class ShowLockScreen extends Service {

    DatabaseHelper db;
    Dialog myDialog;
    MediaPlayer m;
    View subview;
    Korean korean;

    TextView tvKorean;
    TextView tvMean;
    TextView tvPhienAm;

    TextView tvTime;
    TextView tvDate;

    ImageView imgSpecker;
    ImageView imgRandom;
    ImageView imgExit;


    SharedPreferences preferences;
    int key_language;
    String key_pass;

    StringBuilder pass;

    @Override
    public void onCreate() {
        super.onCreate();

        preferences = getSharedPreferences(KUtils.LANGUAGE_KEY, MODE_PRIVATE);

        db = new DatabaseHelper(this, getFilesDir().getAbsolutePath());
        try {
            db.prepareDataBase();
        } catch (IOException io) {
        }

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        key_language = preferences.getInt(KUtils.L_KEY, 0);
        key_pass = preferences.getString(KUtils.P_KEY, "");

        korean = randomKorean();
        initView();

        return START_STICKY;
    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        subview = inflater.inflate(R.layout.dialog_lock, null);
        myDialog = new Dialog(this, R.style.LockScreenDialog);

        myDialog.setCancelable(false);
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            myDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            myDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        } else {
            myDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_PHONE);
        }
        myDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);

        myDialog.setContentView(subview);

        myDialog.show();

        findAllViewById();

        updateSubView();

        actionSubView();
    }

    private void actionSubView() {
        m = new MediaPlayer();

        imgExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleExit();
            }
        });

        imgRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                randomKorean();
                updateSubView();
            }
        });

        imgSpecker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound(korean.getVoice());
            }
        });
    }

    private void handleExit() {
        if (TextUtils.isEmpty(key_pass)) {
            myDialog.dismiss();
        } else {
            myDialog.dismiss();
            initDialogPass();
        }
    }

    Dialog pDialog;

    private void initDialogPass() {
        pass = new StringBuilder();
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        subview = inflater.inflate(R.layout.dialog_pass, null);
        pDialog = new Dialog(this, R.style.LockScreenDialog);

        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            pDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            pDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        } else {
            pDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_PHONE);
        }
        pDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);

        pDialog.setContentView(subview);

        final TextView txt= pDialog.findViewById(R.id.txtPass);

        pDialog.show();
        IOSPassCodeAdapter iosPassCodeAdapter = new IOSPassCodeAdapter(getApplicationContext());
        final PasscodeIndicator passcodeIndicator = subview.findViewById(R.id.indicator);

        PasscodeView passcodeView = subview.findViewById(R.id.passcode_view);
        passcodeView.setAdapter(iosPassCodeAdapter);
        passcodeView.setOnItemClickListener(new PasscodeView.OnItemClickListener() {
            @Override
            public void onItemClick(PasscodeView passcodeView, int i, View view, Object o) {
                if (!passcodeIndicator.isAnimationInProgress()) {

                    pass.append(o.toString());
                    passcodeIndicator.setIndicatorLevel(pass.length());
                    Log.e("onItemClick: ", pass.toString());
                    if (pass.length() == passcodeIndicator.getIndicatorLength()) {
                        if (pass.toString().equals(key_pass)) {
                            myDialog.dismiss();
                            pDialog.dismiss();
                            onDestroy();
                        } else {
                            passcodeIndicator.wrongPasscode();
                            passcodeIndicator.setIndicatorLevel(0);
                            pass.delete(0, pass.length());
                            txt.setText(R.string.faild);
                        }
                    }

                }
            }
        });


    }

    public void playSound(String fileName) {
        AssetFileDescriptor descriptor = null;
        if (m.isPlaying()) {
            m.pause();
            m.stop();
            m.release();
            m = new MediaPlayer();
        }

        try {
            m.release();
            m = new MediaPlayer();
            descriptor = getApplicationContext().getAssets().openFd(fileName + "_f.ogg");
            m.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            descriptor.close();
            m.prepare();
            m.setVolume(1f, 1f);
            m.start();
            m.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Korean randomKorean() {
        korean = new Korean();

        Random random = new Random();
        int num = random.nextInt(923);
        Log.e("randomKorean: ", num + "");
        korean = db.getKorean(num + 1);
        return korean;
    }

    private void updateSubView() {
        Log.e("updateSubView: ", korean.toString());
        switch (key_language) {
            case 0:
                tvMean.setText(korean.getTextVN());
                break;
            case 1:
                tvMean.setText(korean.getTextCH());
                break;
            case 2:
                tvMean.setText(korean.getTextEN());
                break;
        }

        tvPhienAm.setText(korean.getPinyin());
        tvKorean.setText(korean.getTextKR());
    }

    /**
     * Hiển thị view
     */
    private void findAllViewById() {
        tvKorean = subview.findViewById(R.id.tvKorean);
        tvMean = subview.findViewById(R.id.tvMean);
        tvPhienAm = subview.findViewById(R.id.tvPhienAm);

        tvTime = subview.findViewById(R.id.tvTime);
        tvDate = subview.findViewById(R.id.tvDateOfWeed);

        imgSpecker = subview.findViewById(R.id.imgSpecker);
        imgRandom = subview.findViewById(R.id.imgRandom);
        imgExit = subview.findViewById(R.id.imgExit);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            myDialog.dismiss();
            pDialog.dismiss();
        }catch (Exception e){
            e.printStackTrace();
        }
        stopSelf();
    }
}
