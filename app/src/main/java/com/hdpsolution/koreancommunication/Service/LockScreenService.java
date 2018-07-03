package com.hdpsolution.koreancommunication.Service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.hdpsolution.koreancommunication.Broadcast.ScreenBroadcast;
import com.hdpsolution.koreancommunication.Utils.KUtils;

public class LockScreenService extends Service {

    ScreenBroadcast screenBroadcast;
    SharedPreferences preferences;
    boolean isTurnOn;

    @Override
    public void onCreate() {
        super.onCreate();

        preferences = getSharedPreferences(KUtils.LANGUAGE_KEY, MODE_PRIVATE);
        isTurnOn = preferences.getBoolean(KUtils.ENABLE_LOCK_SCREEN, false);
        Log.e("In isTurnOn", isTurnOn+"" );
        if (isTurnOn) {

            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Intent.ACTION_SCREEN_ON);
            intentFilter.addAction(Intent.ACTION_SCREEN_OFF);

            screenBroadcast = new ScreenBroadcast();

            registerReceiver(screenBroadcast, intentFilter);
        }

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        isTurnOn = preferences.getBoolean("enable_lock_screen", false);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        try {
            unregisterReceiver(screenBroadcast);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
        stopSelf();

    }
}
