package com.example.raiderdelivery_v4.ui.account;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.raiderdelivery_v4.R;
import com.example.raiderdelivery_v4.ui.global.AesCipher;
import com.example.raiderdelivery_v4.ui.global.Ajax;
import com.example.raiderdelivery_v4.ui.global.Globalvars;
import org.json.JSONArray;
import org.json.JSONException;

import static com.example.raiderdelivery_v4.ui.global.Globalvars.secretKey;

public class ForgotPassword extends AppCompatActivity {
    EditText et_fp_username;
    Button btn_fp_submit;
    String str_username;
    Ajax mo;
    ProgressDialog pd;
    Globalvars globalvars;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgotpassword);
        et_fp_username = (EditText)findViewById(R.id.et_fp_username);
        btn_fp_submit = (Button)findViewById(R.id.btn_fp_submit);
        pd = new ProgressDialog(this);
        globalvars = new Globalvars((Context)this,(Activity)this);

        btn_fp_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_username = AesCipher.encrypt(secretKey, et_fp_username.getText().toString()).toString();
                send_username();
            }
        });
    }

    public void send_username() {

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
                    if (thedata.length() > 0) {
                        pd.dismiss();
                        globalvars.set("fp_username", str_username);
                        Toast toast = Toast.makeText(getApplicationContext(), "OTP CODE has been successfully sent.", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 80);
                        toast.show();
                        Intent i = new Intent(ForgotPassword.this, OTPCode.class);
                        startActivity(i);
                    }else{
                        Toast toast = Toast.makeText(getApplicationContext(), "Username not found.", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 80);
                        toast.show();
                        pd.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    et_fp_username.setText("");
                }
            }
        });

        mo.adddata("et_username", str_username);
        mo.execute(Globalvars.online_link + "search_credential");
    }
}
