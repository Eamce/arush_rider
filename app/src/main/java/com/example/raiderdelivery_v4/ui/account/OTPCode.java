package com.example.raiderdelivery_v4.ui.account;

import android.app.ProgressDialog;
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

public class OTPCode extends AppCompatActivity {
    EditText et_otpcode;
    Button btn_otp_submit;
    String str_otp;
    Ajax mo;
    ProgressDialog pd;
    Globalvars globalvars;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otpcode);
        et_otpcode = (EditText)findViewById(R.id.et_otpcode);
        btn_otp_submit = (Button)findViewById(R.id.btn_otp_submit);
        pd = new ProgressDialog(this);
        globalvars = new Globalvars(this, this);

        btn_otp_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_otp = AesCipher.encrypt(secretKey, et_otpcode.getText().toString()).toString();
                send_otp();
            }
        });
    }

    private void send_otp() {
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
                    String row = thedata.getString(0);
                    AesCipher dencrypted_username = AesCipher.decrypt(secretKey, row);


//                    AesCipher encrypted_username1 = AesCipher.encrypt(secretKey, "wala");
//                    String a = encrypted_username1.toString();
//                    AesCipher encrypted_username2 = AesCipher.encrypt(secretKey, "wala");
//                    String a1 = encrypted_username2.toString();
//
//                    AesCipher encrypted_username3 = AesCipher.encrypt(secretKey, "wala");
//                    String a2 = encrypted_username3.toString();
//                    AesCipher encrypted_username4 = AesCipher.encrypt(secretKey, "wala");
//                    String a3 = encrypted_username4.toString();



                    if (row.equalsIgnoreCase("naa")) {
                        pd.dismiss();
                        Toast toast = Toast.makeText(getApplicationContext(), "OTP CODE matched.", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 80);
                        toast.show();
                        Intent i = new Intent(OTPCode.this, NewPassword.class);
                        startActivity(i);
                    }else{
                        Toast toast = Toast.makeText(getApplicationContext(), "Invalid OTP CODE.", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 80);
                        toast.show();
                        pd.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    et_otpcode.setText("");
                }
            }
        });

        mo.adddata("otp", str_otp);
        mo.adddata("username", globalvars.get("fp_username"));
        mo.execute(Globalvars.online_link + "search_otp");
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed(); commented this line in order to disable back press
    }
}
