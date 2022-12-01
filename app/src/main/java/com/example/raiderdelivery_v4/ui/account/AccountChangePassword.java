package com.example.raiderdelivery_v4.ui.account;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.raiderdelivery_v4.ui.global.AesCipher;
import com.example.raiderdelivery_v4.ui.global.Ajax;
import com.example.raiderdelivery_v4.ui.global.Globalvars;
import com.example.raiderdelivery_v4.R;
import com.example.raiderdelivery_v4.ui.login.Login;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.raiderdelivery_v4.ui.global.Globalvars.EmptyEncrypted;
import static com.example.raiderdelivery_v4.ui.global.Globalvars.secretKey;

public class AccountChangePassword extends AppCompatActivity {
    EditText et_old_pass, et_new_pass, et_confirm_pass;
    Button btn_submit, btn_changepass_cancel;
    CheckBox cb_showchangepassword;
    //TextView tv_password_message;
    Ajax mo;
    Globalvars globalvars;
    ProgressDialog pd;
    String str_old_pass, str_new_pass, str_confirm_pass;
    Handler handler;
    Runnable r;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_changepass);
        et_old_pass = findViewById(R.id.et_old_password);
        et_new_pass = findViewById(R.id.et_new_password);
        et_confirm_pass = findViewById(R.id.et_confirm_password);
        btn_submit = findViewById(R.id.btn_submit);
        btn_changepass_cancel = findViewById(R.id.btn_changepass_cancel);
        cb_showchangepassword = findViewById(R.id.cb_showchangepassword);
        //tv_password_message = findViewById(R.id.tv_password_message);
        globalvars = new Globalvars((Context)this,(Activity)this);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_old_pass = AesCipher.encrypt(secretKey, et_old_pass.getText().toString()).toString();
                str_new_pass = AesCipher.encrypt(secretKey, et_new_pass.getText().toString()).toString();
                str_confirm_pass = AesCipher.encrypt(secretKey, et_confirm_pass.getText().toString()).toString();

                if ((str_old_pass.equalsIgnoreCase(EmptyEncrypted))){
                    et_old_pass.setError("Please fill up field.");
                    return;
                }else if((str_new_pass.equalsIgnoreCase(EmptyEncrypted))){
                    et_new_pass.setError("Please fill up field.");
                    return;
                }else if((str_confirm_pass.equalsIgnoreCase(EmptyEncrypted))){
                    et_confirm_pass.setError("Please fill up field.");
                    return;
                }else{
                    if(isValidPassword(AesCipher.decrypt(secretKey, str_new_pass).toString())){
                        validate_new_password();
                    }else{
//                      tv_password_message.setVisibility(View.VISIBLE);
                        Toast toast = Toast.makeText(getApplicationContext(),"Password must be 8-16 characters and contain both numbers and special characters/letters with upper and lower case.",Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,80);
                        toast.show();
                    }
                }
            }
        });

        btn_changepass_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AccountChangePassword.this, AccountActivity.class);
                startActivity(i);
            }
        });

        handler = new Handler();
        r = new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                if(!isFinishing()) {

                    //Logout user...
                    globalvars.logout();

                    //before inflating the custom alert dialog layout, we will get the current activity viewgroup
                    ViewGroup viewGroup = findViewById(android.R.id.content);

                    //then we will inflate the custom alert dialog xml that we created
                    View dialogView = LayoutInflater.from(AccountChangePassword.this).inflate(R.layout.my_dialog_session_timedout, viewGroup, false);
                    TextView tv_dialog_message = dialogView.findViewById(R.id.tv_dialog_message);
                    tv_dialog_message.setText("Your session has timed out. Please login again.");
                    //Now we need an AlertDialog.Builder object
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(AccountChangePassword.this);
                    builder.setCancelable(false);
                    //setting the view of the builder to our custom view that we already inflated
                    builder.setView(dialogView);

                    //finally creating the alert dialog and displaying it
                    final androidx.appcompat.app.AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                    Button btn_ok = dialogView.findViewById(R.id.btn_ok);
                    btn_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.cancel();
                            Intent intent = new Intent(AccountChangePassword.this, Login.class);
                            startActivity(intent);
                        }
                    });
                }
            }
        };
        startHandler();
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch (view.getId()) {
            case R.id.cb_showchangepassword:
                if (checked) {
                    et_old_pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    et_new_pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    et_confirm_pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    et_old_pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    et_new_pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    et_confirm_pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                break;
        }
    }

    @Override
    public void onUserInteraction() {
        // TODO Auto-generated method stub
        super.onUserInteraction();
        stopHandler();//stop first and then start
        startHandler();
    }
    public void stopHandler() {
        handler.removeCallbacks(r);
    }
    public void startHandler() {
        handler.postDelayed(r, 30*60*1000); //Set Session timeout for 10 minutes...
    }

    public boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,16}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

    public void validate_new_password(){
        if(str_new_pass.equals(str_confirm_pass)) {
            validate_password();
        }else{
            Toast toast = Toast.makeText(getApplicationContext(), "New password doesn`t match.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 80);
            toast.show();
        }
    }

    public void validate_password(){
        pd = new ProgressDialog(this);
        pd.setMessage("Please wait...");
        pd.show();
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        mo = new Ajax();
        mo.setCustomObjectListener(new Ajax.MyCustomObjectListener() {
            @Override
            public void onerror() {
                pd.dismiss();
                Toast toast = Toast.makeText(getApplicationContext(), "Unable to connect. Please check your connection.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 80);
                toast.show();
            }

            @Override
            public void onsuccess(String data) {
                JSONArray thedata;
                try {
                    thedata = new JSONArray(data);
                    if (thedata.length() > 0)
                    {
                        update_password();
                    }
                    else
                    {
                        pd.dismiss();
                        Toast toast = Toast.makeText(getApplicationContext(),"Old password is incorrect!",Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,80);
                        toast.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {

                }
            }
        });

        mo.adddata("r_id_num", AesCipher.encrypt(secretKey, globalvars.get("r_id_num")).toString());
        mo.adddata("old_pass",str_old_pass);
        mo.execute(Globalvars.online_link + "verify_old_password");
    }

    public void update_password(){
        mo = new Ajax();
        mo.setCustomObjectListener(new Ajax.MyCustomObjectListener() {
            @Override
            public void onerror() {
                pd.dismiss();
                Toast toast = Toast.makeText(getApplicationContext(), "Unable to connect. Please check your connection.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 80);
                toast.show();
            }

            @Override
            public void onsuccess(String data) {
                try {
                    pd.dismiss();
//                    Toast toast = Toast.makeText(getApplicationContext(), "Password has been successfully updated.", Toast.LENGTH_LONG);
//                    toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 80);
//                    toast.show();
                    //before inflating the custom alert dialog layout, we will get the current activity viewgroup
                    ViewGroup viewGroup = findViewById(android.R.id.content);

                    //then we will inflate the custom alert dialog xml that we created
                    View dialogView = LayoutInflater.from(AccountChangePassword.this).inflate(R.layout.my_dialog, viewGroup, false);
                    TextView tv_dialog_message = dialogView.findViewById(R.id.tv_dialog_message);
                    tv_dialog_message.setText("Password has been successfully updated.");
                    //Now we need an AlertDialog.Builder object
                    AlertDialog.Builder builder = new AlertDialog.Builder(AccountChangePassword.this);
                    builder.setCancelable(false);
                    //setting the view of the builder to our custom view that we already inflated
                    builder.setView(dialogView);

                    //finally creating the alert dialog and displaying it
                    final AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                    Button btn_ok = dialogView.findViewById(R.id.btn_ok);
                    btn_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.cancel();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {

                }
            }
        });

        mo.adddata("r_id_num", AesCipher.encrypt(secretKey, globalvars.get("r_id_num")).toString());
        mo.adddata("old_pass",str_old_pass);
        mo.adddata("new_pass",str_new_pass);
        mo.execute(Globalvars.online_link + "change_password");
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed(); commented this line in order to disable back press
    }
}
