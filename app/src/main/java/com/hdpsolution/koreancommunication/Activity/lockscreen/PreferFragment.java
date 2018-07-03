package com.hdpsolution.koreancommunication.Activity.lockscreen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;

import com.hdpsolution.koreancommunication.Activity.MainActivity;
import com.hdpsolution.koreancommunication.Service.LockScreenService;
import com.hdpsolution.koreancommunication.R;
import com.hdpsolution.koreancommunication.Utils.KUtils;

public class PreferFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {
    SwitchPreference switchTurnon;
    boolean isTurrnOn;
    Preference pre;
    Preference prePass;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_lock_setting);


        preferences = getActivity().getSharedPreferences(KUtils.LANGUAGE_KEY, Context.MODE_PRIVATE);

        isTurrnOn = pullState(KUtils.ENABLE_LOCK_SCREEN);

        initData();
        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
        isTurrnOn = pullState(KUtils.ENABLE_LOCK_SCREEN);

        updateGui();

    }

    private void updateGui() {
        if (isTurrnOn) {
            switchTurnon.setChecked(true);

        } else {
            switchTurnon.setChecked(false);
        }
        turnOnLockScreen(isTurrnOn);
    }

    private void initData() {
    }

    private void initView() {
        switchTurnon = (SwitchPreference) findPreference(getString(R.string.key_turn_on));
        switchTurnon.setOnPreferenceClickListener(this);

        pre=findPreference(getString(R.string.key_turn_off));
        pre.setOnPreferenceClickListener(this);

        prePass=findPreference(getString(R.string.key_using_pass));
        prePass.setOnPreferenceClickListener(this);

        updateGui();
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference == null) {
            return false;
        } else {
            switch (preference.getTitleRes()) {
                case R.string.title_turn_on:
                    if (isTurrnOn) {
                        isTurrnOn = false;
                        pushState(KUtils.ENABLE_LOCK_SCREEN, false);
                    } else {
                        isTurrnOn = true;
                        pushState(KUtils.ENABLE_LOCK_SCREEN, true);
                    }
                    updateGui();
                    turnOnLockScreen(isTurrnOn);
                    break;
                case R.string.key_turn_off_lock_security:
                    Log.e("onPreferenceClick: ", "offlock");
                    Intent intent= new Intent(Settings.ACTION_SECURITY_SETTINGS);
                    getActivity().startActivity(intent);
                    break;

                case R.string.key_using_pass_security:
                    Log.e("onPreferenceClick: ", "passs");
                    Intent intentPass= new Intent(getActivity(),PasswordActivity.class);
                    startActivity(intentPass);
                    break;
            }

        }
        return false;
    }

    private void turnOnLockScreen(boolean b) {
        //Service
        if(b == true){
            if(getActivity() instanceof LockScreenSetting) {
                if (LockScreenSetting.isCandraw){
                    Intent intent = new Intent(getActivity(), LockScreenService.class);
                    getActivity().startService(intent);
                }
            }

        }else{
            Intent intent = new Intent(getActivity(), LockScreenService.class);
            getActivity().stopService(intent);
        }
    }

    /**
     * Láy dữ liệu
     */
    private boolean pullState(String key) {
        return isTurrnOn = preferences.getBoolean(key, false);
    }

    /**
     * Lưu dữ liệu
     */
    private void pushState(String key, boolean b) {
        editor = preferences.edit();
        editor.putBoolean(key, b);
        editor.apply();
    }
}
