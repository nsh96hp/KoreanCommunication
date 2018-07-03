package com.hdpsolution.koreancommunication.Activity.lockscreen;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hdpsolution.koreancommunication.Activity.MainActivity;
import com.hdpsolution.koreancommunication.R;
import com.hdpsolution.koreancommunication.Utils.KUtils;

public class PasswordActivity extends AppCompatActivity {
    private LinearLayout ll_newpass,ll_oldpass;
    private EditText edt_pass1,edt_pass2,edt_pincode;
    private Button btn_change_pass_c,btn_change_pass_p,btn_change_pin, btn_ok_pass,btn_remove_pass;
    private String key_pass;
    private String key_pin;
    SharedPreferences preferences;
    SharedPreferences.Editor preEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        Toolbar toolbar = findViewById(R.id.toolbar_passlock_setting);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.passLock);

        findId();
        initGUI();
        handleNewPass();
        handleOldPass();


    }

    private void handleOldPass() {

        btn_remove_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog= new Dialog(PasswordActivity.this,R.style.mydialogstyle);
                dialog.setContentView(R.layout.dialog_remove_pass);

                Button btn_cancel,btn_ok;
                final EditText edt_old_pass;

                btn_cancel=dialog.findViewById(R.id.btn_cancel_r);
                btn_ok=dialog.findViewById(R.id.btn_ok_r);
                edt_old_pass=dialog.findViewById(R.id.edt_old_pin);

                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(edt_old_pass.length()==4){
                            if(edt_old_pass.getText().toString().equals(key_pin)){
                                preEdit.putString(KUtils.P_KEY,"");
                                preEdit.commit();
                                key_pass="";
                                initGUI();
                                dialog.dismiss();
                                Toast.makeText(PasswordActivity.this, getResources().getString(R.string.change_done), Toast.LENGTH_SHORT).show();
                            }else {
                                edt_old_pass.setHint(R.string.faild);
                                edt_old_pass.setText("");
                            }
                        }else {
                            edt_old_pass.setHint(R.string.faild);
                            edt_old_pass.setText("");
                        }
                    }
                });

                dialog.show();
            }
        });

        btn_change_pass_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass_current();
            }
        });
        btn_change_pass_p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass_pin();
            }
        });

        btn_change_pin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pin();
            }
        });
    }

    private void pin() {
        final Dialog dialog= new Dialog(PasswordActivity.this,R.style.mydialogstyle);
        dialog.setContentView(R.layout.dialog_pass_current);

        dialog.show();

        Button btn_cancel,btn_ok;
        TextView txt_title_pass;
        final EditText edt_old_pass,edt_new_pass1,edt_new_pass2;

        btn_cancel=dialog.findViewById(R.id.btn_cancel);
        btn_ok=dialog.findViewById(R.id.btn_ok);
        txt_title_pass=dialog.findViewById(R.id.txt_title_pass);
        edt_old_pass=dialog.findViewById(R.id.edt_old_pass);
        edt_new_pass1=dialog.findViewById(R.id.edt_new_pass1);
        edt_new_pass2=dialog.findViewById(R.id.edt_new_pass2);

        txt_title_pass.setText(R.string.change_pin);
        edt_old_pass.setHint(R.string.inputpass_c_pin);
        edt_new_pass1.setHint(R.string.inputpin_new);
        edt_new_pass2.setHint(R.string.inputpin_renew);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edt_old_pass.getText().toString().length()==4 && edt_new_pass1.getText().toString().length()==4 && edt_new_pass2.getText().toString().length()==4 ){
                    if(edt_old_pass.getText().toString().equals(key_pin)){
                        if(edt_new_pass1.getText().toString().equals(edt_new_pass2.getText().toString())){
                            preEdit.putString(KUtils.PIN_KEY,edt_new_pass1.getText().toString());
                            preEdit.commit();
                            key_pin=edt_new_pass1.getText().toString();
                            Toast.makeText(PasswordActivity.this, getResources().getString(R.string.change_done), Toast.LENGTH_SHORT).show();

                            dialog.dismiss();
                        }else {
                            edt_old_pass.setText("");
                            edt_new_pass1.setText("");
                            edt_new_pass2.setText("");
                            edt_old_pass.setHint(R.string.faild);
                            edt_new_pass1.setHint("****");
                            edt_new_pass2.setHint("****");
                        }
                    }else {
                        edt_old_pass.setText("");
                        edt_new_pass1.setText("");
                        edt_new_pass2.setText("");
                        edt_old_pass.setHint(R.string.faild);
                        edt_new_pass1.setHint("****");
                        edt_new_pass2.setHint("****");
                    }
                }else {
                    edt_old_pass.setText("");
                    edt_new_pass1.setText("");
                    edt_new_pass2.setText("");
                    edt_old_pass.setHint(R.string.faild);
                    edt_new_pass1.setHint("****");
                    edt_new_pass2.setHint("****");
                }
            }

        });


    }

    private void pass_pin() {

        final Dialog dialog= new Dialog(PasswordActivity.this,R.style.mydialogstyle);
        dialog.setContentView(R.layout.dialog_pass_current);

        dialog.show();

        Button btn_cancel,btn_ok;
        TextView txt_title_pass;
        final EditText edt_old_pass,edt_new_pass1,edt_new_pass2;

        btn_cancel=dialog.findViewById(R.id.btn_cancel);
        btn_ok=dialog.findViewById(R.id.btn_ok);
        txt_title_pass=dialog.findViewById(R.id.txt_title_pass);
        edt_old_pass=dialog.findViewById(R.id.edt_old_pass);
        edt_new_pass1=dialog.findViewById(R.id.edt_new_pass1);
        edt_new_pass2=dialog.findViewById(R.id.edt_new_pass2);

        txt_title_pass.setText(R.string.changepass_pin);
        edt_old_pass.setHint(R.string.inputpass_pin);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edt_old_pass.getText().toString().length()==4 && edt_new_pass1.getText().toString().length()==4 && edt_new_pass2.getText().toString().length()==4 ){
                    if(edt_old_pass.getText().toString().equals(key_pin)){
                        if(edt_new_pass1.getText().toString().equals(edt_new_pass2.getText().toString())){
                            preEdit.putString(KUtils.P_KEY,edt_new_pass1.getText().toString());
                            preEdit.commit();
                            key_pass=edt_new_pass1.getText().toString();
                            Toast.makeText(PasswordActivity.this, getResources().getString(R.string.change_done), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }else {
                            edt_old_pass.setText("");
                            edt_new_pass1.setText("");
                            edt_new_pass2.setText("");
                            edt_old_pass.setHint(R.string.faild);
                            edt_new_pass1.setHint("****");
                            edt_new_pass2.setHint("****");
                        }
                    }else {
                        edt_old_pass.setText("");
                        edt_new_pass1.setText("");
                        edt_new_pass2.setText("");
                        edt_old_pass.setHint(R.string.faild);
                        edt_new_pass1.setHint("****");
                        edt_new_pass2.setHint("****");
                    }
                }else {
                    edt_old_pass.setText("");
                    edt_new_pass1.setText("");
                    edt_new_pass2.setText("");
                    edt_old_pass.setHint(R.string.faild);
                    edt_new_pass1.setHint("****");
                    edt_new_pass2.setHint("****");
                }
            }

        });

    }

    private void pass_current() {
        final Dialog dialog= new Dialog(PasswordActivity.this,R.style.mydialogstyle);
        dialog.setContentView(R.layout.dialog_pass_current);

        dialog.show();

        Button btn_cancel,btn_ok;
        final EditText edt_old_pass,edt_new_pass1,edt_new_pass2;

        btn_cancel=dialog.findViewById(R.id.btn_cancel);
        btn_ok=dialog.findViewById(R.id.btn_ok);

        edt_old_pass=dialog.findViewById(R.id.edt_old_pass);
        edt_new_pass1=dialog.findViewById(R.id.edt_new_pass1);
        edt_new_pass2=dialog.findViewById(R.id.edt_new_pass2);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edt_old_pass.getText().toString().length()==4 && edt_new_pass1.getText().toString().length()==4 && edt_new_pass2.getText().toString().length()==4 ){
                    if(edt_old_pass.getText().toString().equals(key_pass)){
                        if(edt_new_pass1.getText().toString().equals(edt_new_pass2.getText().toString())){
                            preEdit.putString(KUtils.P_KEY,edt_new_pass1.getText().toString());
                            preEdit.commit();
                            key_pass=edt_new_pass1.getText().toString();
                            Toast.makeText(PasswordActivity.this, getResources().getString(R.string.change_done), Toast.LENGTH_SHORT).show();

                            dialog.dismiss();
                        }else {
                            edt_old_pass.setText("");
                            edt_new_pass1.setText("");
                            edt_new_pass2.setText("");
                            edt_old_pass.setHint(R.string.faild);
                            edt_new_pass1.setHint("****");
                            edt_new_pass2.setHint("****");
                        }
                    }else {
                        edt_old_pass.setText("");
                        edt_new_pass1.setText("");
                        edt_new_pass2.setText("");
                        edt_old_pass.setHint(R.string.faild);
                        edt_new_pass1.setHint("****");
                        edt_new_pass2.setHint("****");
                    }
                }else {
                    edt_old_pass.setText("");
                    edt_new_pass1.setText("");
                    edt_new_pass2.setText("");
                    edt_old_pass.setHint(R.string.faild);
                    edt_new_pass1.setHint("****");
                    edt_new_pass2.setHint("****");
                }
            }
        });

    }

    private void handleNewPass() {
        btn_ok_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edt_pass1.getText().toString().length()==4 && edt_pass2.getText().toString().length()==4 && edt_pincode.getText().toString().length()==4){
                    if (edt_pass1.getText().toString().equals(edt_pass2.getText().toString())){
                        preEdit.putString(KUtils.P_KEY,edt_pass1.getText().toString());
                        preEdit.putString(KUtils.PIN_KEY,edt_pincode.getText().toString());
                        preEdit.commit();
                        initGUI();
                    }
                }else {
                    edt_pass1.setText("");
                    edt_pass2.setText("");
                    edt_pincode.setText("");
                    edt_pass1.setHint(R.string.faild);
                    edt_pass2.setHint("****");
                    edt_pincode.setHint("****");
                }
            }
        });
    }

    private void initGUI() {
        preferences = getSharedPreferences(KUtils.LANGUAGE_KEY, MODE_PRIVATE);
        key_pass = preferences.getString(KUtils.P_KEY, "");
        key_pin = preferences.getString(KUtils.PIN_KEY, "");
        Log.e("initGUI: ", key_pass+ "---"+key_pin);
        preEdit=preferences.edit();

        if (TextUtils.isEmpty(key_pass)){
            ll_oldpass.setVisibility(View.GONE);
            ll_newpass.setVisibility(View.VISIBLE);
            btn_ok_pass.setVisibility(View.VISIBLE);
        }else {
            ll_oldpass.setVisibility(View.VISIBLE);
            ll_newpass.setVisibility(View.GONE);
            btn_ok_pass.setVisibility(View.GONE);
        }
    }

    private void findId() {
        ll_newpass=findViewById(R.id.ll_newpass);

        edt_pass1=findViewById(R.id.edt_pass1);
        edt_pass2=findViewById(R.id.edt_pass2);
        edt_pincode=findViewById(R.id.edt_pincode);
        btn_ok_pass=findViewById(R.id.btn_ok_pass);

        ll_oldpass=findViewById(R.id.ll_oldpass);

        btn_change_pass_c=findViewById(R.id.btn_change_pass_c);
        btn_change_pass_p=findViewById(R.id.btn_change_pass_p);
        btn_change_pin=findViewById(R.id.btn_change_pin);
        btn_remove_pass=findViewById(R.id.btn_remove_pass);
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
}
