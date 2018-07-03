package com.hdpsolution.koreancommunication.Broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.hdpsolution.koreancommunication.Service.ShowLockScreen;

public class





ScreenBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case Intent.ACTION_SCREEN_ON:
                //làm gì khi on
                break;
            case Intent.ACTION_SCREEN_OFF:
                //Làm gì khi tắt màn hình
                Intent startService = new Intent(context, ShowLockScreen.class);
                context.startService(startService);
                break;
        }
    }
}
