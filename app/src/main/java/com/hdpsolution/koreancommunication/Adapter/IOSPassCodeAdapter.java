package com.hdpsolution.koreancommunication.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hdpsolution.koreancommunication.R;
import com.hdpsolution.koreancommunication.data.PassCodeItemIos;
import com.nirigo.mobile.view.passcode.adapters.PasscodeAdapter;
import com.nirigo.mobile.view.passcode.adapters.PasscodeBaseAdapter;
import com.nirigo.mobile.view.passcode.models.PasscodeItem;
import com.nirigo.mobile.view.passcode.models.PasscodeItemEmpty;

import java.util.Arrays;

public class IOSPassCodeAdapter extends PasscodeBaseAdapter {
    LayoutInflater inflater;

    public IOSPassCodeAdapter(Context context) {
        super(Arrays.asList(
                new PassCodeItemIos("1", PasscodeItem.TYPE_NUMBER),
                new PassCodeItemIos("2", PasscodeItem.TYPE_NUMBER),
                new PassCodeItemIos("3", PasscodeItem.TYPE_NUMBER),
                new PassCodeItemIos("4", PasscodeItem.TYPE_NUMBER),
                new PassCodeItemIos("5", PasscodeItem.TYPE_NUMBER),
                new PassCodeItemIos("6", PasscodeItem.TYPE_NUMBER),
                new PassCodeItemIos("7", PasscodeItem.TYPE_NUMBER),
                new PassCodeItemIos("8", PasscodeItem.TYPE_NUMBER),
                new PassCodeItemIos("9", PasscodeItem.TYPE_NUMBER),
                new PasscodeItemEmpty(),
                new PassCodeItemIos("0", PasscodeItem.TYPE_NUMBER),
                new PasscodeItemEmpty()
        ));
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        if (convertView == null || convertView.getTag() != PassCodeItemIos.class) {
            convertView = inflater.inflate(R.layout.layout_button_passcode_ios, parent, false);
            convertView.setTag(PassCodeItemIos.class);
        }

        PasscodeItem item = getItem(i);

        AppCompatTextView numberTextView = (AppCompatTextView) convertView.findViewById(R.id.number);
        numberTextView.setText(item.getValue());
        if (item instanceof PassCodeItemIos) {
            numberTextView.setGravity(Gravity.CENTER);
        }

        convertView.setVisibility(item.getType() == PasscodeItem.TYPE_EMPTY ? View.INVISIBLE : View.VISIBLE);

        return convertView;
    }
}
