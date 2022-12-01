package com.example.raiderdelivery_v4.ui.account;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.raiderdelivery_v4.R;
import com.example.raiderdelivery_v4.ui.global.Ajax;
import com.example.raiderdelivery_v4.ui.global.Globalvars;
import com.example.raiderdelivery_v4.ui.login.Login;

public class DataPrivacyConsent extends AppCompatActivity {
    Ajax mo;
    ProgressDialog pd;
    Button btn_iagree;
    String str_firstname, str_lastname, str_birthdate, str_sex, str_permanentaddress, str_mobileno, str_username, str_password;
    String str_license_type, str_otherdetails, str_brand, str_model, str_color, str_plateno, str_otherdetails2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dataprivacyconsent);
        pd = new ProgressDialog(this);

        btn_iagree = (Button)findViewById(R.id.btn_iagree);
        btn_iagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_user();
            }
        });

        Intent intent = getIntent();
        str_firstname = intent.getExtras().getString("str_firstname");
        str_lastname = intent.getExtras().getString("str_lastname");
        str_birthdate = intent.getExtras().getString("str_birthdate");
        str_sex = intent.getExtras().getString("str_sex");
        str_permanentaddress = intent.getExtras().getString("str_permanentaddress");
        str_mobileno = intent.getExtras().getString("str_mobileno");
        str_username = intent.getExtras().getString("str_username");
        str_password = intent.getExtras().getString("str_password");
        str_license_type = intent.getExtras().getString("str_license_type");
        str_otherdetails = intent.getExtras().getString("str_otherdetails");
        str_brand = intent.getExtras().getString("str_brand");
        str_model = intent.getExtras().getString("str_model");
        str_color = intent.getExtras().getString("str_color");
        str_plateno = intent.getExtras().getString("str_plateno");
        str_otherdetails2 = intent.getExtras().getString("str_otherdetails2");
    }

    private void save_user ()
    {
        pd.setMessage("Please wait...");
        pd.show();
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        mo = new Ajax();
        mo.setCustomObjectListener(new Ajax.MyCustomObjectListener()
        {
            @Override
            public void onerror()
            {
                pd.dismiss();
                Toast toast = Toast.makeText(getApplicationContext(),"Unable to connect. Please check your connection.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL,0,80);
                toast.show();
            }
            @Override
            public void onsuccess(String data)
            {
                pd.dismiss();
                Toast toast = Toast.makeText(getApplicationContext(),"You have successfully registered.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL,0,80);
                toast.show();
                Intent i = new Intent(DataPrivacyConsent.this, Login.class);
                startActivity(i);
            }
        });

        mo.adddata("et_firstname", str_firstname);
        mo.adddata("et_lastname",str_lastname);
        mo.adddata("et_birthdate",str_birthdate);
        mo.adddata("rb_sex",str_sex);
        mo.adddata("et_permanentaddress",str_permanentaddress);
        mo.adddata("et_mobileno",str_mobileno);
        mo.adddata("et_username",str_username);
        mo.adddata("et_password",str_password);
        mo.adddata("spin_license_type",str_license_type);
        mo.adddata("et_otherdetails",str_otherdetails);
        mo.adddata("et_brand",str_brand);
        mo.adddata("et_model",str_model);
        mo.adddata("et_color",str_color);
        mo.adddata("et_plateno",str_plateno);
        mo.adddata("et_otherdetails2",str_otherdetails2);
        mo.execute(Globalvars.online_link + "savenewuser");
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed(); commented this line in order to disable back press
    }
}
