package com.example.raiderdelivery_v4.ui.account;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.raiderdelivery_v4.R;
import com.example.raiderdelivery_v4.ui.global.AesCipher;
import com.example.raiderdelivery_v4.ui.global.Ajax;
import com.example.raiderdelivery_v4.ui.global.Globalvars;
import com.example.raiderdelivery_v4.ui.login.Login;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.raiderdelivery_v4.ui.global.Globalvars.EmptyEncrypted;
import static com.example.raiderdelivery_v4.ui.global.Globalvars.secretKey;

public class NewPassword extends AppCompatActivity {
    Ajax mo;
    ProgressDialog pd;
    Globalvars globalvars;
    EditText et_newpassword, et_confirm_newpassword;
    CheckBox cb_shownewpassword;
    Button btn_newpassword_submit;
    String str_newpassword, str_confirm_newpassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newpassword);
        et_newpassword = findViewById(R.id.et_newpassword);
        et_confirm_newpassword = findViewById(R.id.et_confirm_newpassword);
        cb_shownewpassword = findViewById(R.id.cb_shownewpassword);
        btn_newpassword_submit = findViewById(R.id.btn_newpassword_submit);
        globalvars = new Globalvars(this, this);
        pd = new ProgressDialog(this);

        btn_newpassword_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_newpassword = AesCipher.encrypt(secretKey, et_newpassword.getText().toString()).toString();
                str_confirm_newpassword = AesCipher.encrypt(secretKey, et_confirm_newpassword.getText().toString()).toString();

                if (str_newpassword.equalsIgnoreCase(EmptyEncrypted) || str_confirm_newpassword.equalsIgnoreCase(EmptyEncrypted)) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Please fill up all required fields.", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 80);
                    toast.show();
                } else {
                    if (!str_newpassword.equalsIgnoreCase(str_confirm_newpassword)) {
                        Toast toast = Toast.makeText(getApplicationContext(), "Your password doesn`t match.", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 80);
                        toast.show();

                    } else {
                        if (isValidPassword(AesCipher.decrypt(secretKey, str_newpassword).toString())) {
                            UpdatePassword();
                        } else {
                            Toast toast = Toast.makeText(getApplicationContext(), "Password must be 8-16 characters and contain both numbers and special characters/letters with upper and lower case.", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 80);
                            toast.show();
                        }
                    }
                }


            }
        });
    }

    private void UpdatePassword() {

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
                if(globalvars.get("OTPType").equalsIgnoreCase("AccountBlocked"))
                {
                    Toast toast = Toast.makeText(getApplicationContext(), "Your account has been successfully recovered.", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 80);
                    toast.show();
                    Intent i = new Intent(NewPassword.this, Login.class);
                    startActivity(i);
                }
                else
                {
                    Toast toast = Toast.makeText(getApplicationContext(), "Your password has been successfully updated.", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 80);
                    toast.show();
                    Intent i = new Intent(NewPassword.this, Login.class);
                    startActivity(i);
                }
            }
        });
        mo.adddata("username", globalvars.get("fp_username"));
        mo.adddata("password", str_newpassword);
        mo.execute(Globalvars.online_link + "update_password");
    }

    public boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,16}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch (view.getId()) {
            case R.id.cb_shownewpassword:
                if (checked) {
                    et_newpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    et_confirm_newpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    et_newpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    et_confirm_newpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed(); commented this line in order to disable back press
    }
}
