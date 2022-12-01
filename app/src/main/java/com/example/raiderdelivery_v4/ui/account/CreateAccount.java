package com.example.raiderdelivery_v4.ui.account;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.raiderdelivery_v4.R;
import com.example.raiderdelivery_v4.ui.global.AesCipher;
import com.example.raiderdelivery_v4.ui.global.Ajax;
import com.example.raiderdelivery_v4.ui.global.Globalvars;
import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static com.example.raiderdelivery_v4.ui.global.Globalvars.EmptyEncrypted;
import static com.example.raiderdelivery_v4.ui.global.Globalvars.secretKey;

public class CreateAccount extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    Spinner spin_licensetype;
    EditText et_firstname, et_lastname, et_birthdate, et_permanentaddress, et_mobileno, et_otherdetails, et_brand, et_model, et_color, et_plateno, et_otherdetails2, et_username, et_password, et_confirmpassword;
    RadioGroup rg_sex;
    RadioButton rb_male, rb_female, rb_sex;
    Button btn_createaccountsubmit, btn_createaccountcancel;
    CheckBox cb_showcreateaccountpass;
    Ajax mo;
    String str_firstname, str_lastname, str_birthdate, str_sex, str_permanentaddress, str_mobileno, str_license_type, str_username, str_password, str_confirmpassword;
    String str_otherdetails, str_brand, str_model, str_color, str_plateno, str_otherdetails2;
    private static final String[] paths = {"Professional", "Non-Professional", "Student-Permit"};
    String license_type = "";
    ProgressDialog pd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accountregistration);
        spin_licensetype = (Spinner)findViewById(R.id.spin_licensetype);
        et_firstname = (EditText)findViewById(R.id.et_firstname);
        et_lastname = (EditText)findViewById(R.id.et_lastname);
        et_birthdate = (EditText)findViewById(R.id.et_birthdate);
        et_permanentaddress = (EditText)findViewById(R.id.et_permanentaddress);
        et_mobileno = (EditText)findViewById(R.id.et_mobileno);
        et_username = (EditText)findViewById(R.id.et_username);
        et_password = (EditText)findViewById(R.id.et_password);
        et_confirmpassword = (EditText)findViewById(R.id.et_confirmpassword);
        et_otherdetails = (EditText)findViewById(R.id.et_otherdetails);
        et_brand = (EditText)findViewById(R.id.et_brand);
        et_model = (EditText)findViewById(R.id.et_model);
        et_color = (EditText)findViewById(R.id.et_color);
        et_plateno = (EditText)findViewById(R.id.et_plateno);
        et_otherdetails2 = (EditText)findViewById(R.id.et_otherdetails2);
        rg_sex = (RadioGroup) findViewById(R.id.rg_sex);
        cb_showcreateaccountpass = findViewById(R.id.cb_showcreateaccountpass);
//        rb_male = (RadioButton) findViewById(R.id.rb_male);
//        rb_female = (RadioButton) findViewById(R.id.rb_female);
        btn_createaccountsubmit = (Button)findViewById(R.id.btn_createaccountsubmit);
        //btn_createaccountcancel = (Button)findViewById(R.id.btn_createaccountcancel);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CreateAccount.this,
                android.R.layout.simple_spinner_item,paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_licensetype.setAdapter(adapter);
        spin_licensetype.setOnItemSelectedListener(this);

        btn_createaccountsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selectedId = rg_sex.getCheckedRadioButtonId();
                rb_sex = (RadioButton)findViewById(selectedId);

                str_firstname = AesCipher.encrypt(secretKey, et_firstname.getText().toString()).toString();
                str_lastname = AesCipher.encrypt(secretKey, et_lastname.getText().toString()).toString();
                str_birthdate = AesCipher.encrypt(secretKey, et_birthdate.getText().toString()).toString();
                str_sex = AesCipher.encrypt(secretKey, rb_sex.getText().toString()).toString();
                str_permanentaddress = AesCipher.encrypt(secretKey, et_permanentaddress.getText().toString()).toString();
                str_mobileno = AesCipher.encrypt(secretKey, et_mobileno.getText().toString()).toString();
                str_license_type = AesCipher.encrypt(secretKey, license_type).toString();
                str_username = AesCipher.encrypt(secretKey, et_username.getText().toString()).toString();
                str_password = AesCipher.encrypt(secretKey, et_password.getText().toString()).toString();
                str_confirmpassword = AesCipher.encrypt(secretKey, et_confirmpassword.getText().toString()).toString();
                str_otherdetails = AesCipher.encrypt(secretKey, et_otherdetails.getText().toString()).toString();
                str_brand = AesCipher.encrypt(secretKey, et_brand.getText().toString()).toString();
                str_model = AesCipher.encrypt(secretKey, et_model.getText().toString()).toString();
                str_color = AesCipher.encrypt(secretKey, et_color.getText().toString()).toString();
                str_plateno = AesCipher.encrypt(secretKey, et_plateno.getText().toString()).toString();
                str_otherdetails2 = AesCipher.encrypt(secretKey, et_otherdetails2.getText().toString()).toString();

                if(str_firstname.equalsIgnoreCase(EmptyEncrypted) || str_lastname.equalsIgnoreCase(EmptyEncrypted) || str_birthdate.equalsIgnoreCase(EmptyEncrypted) || str_permanentaddress.equalsIgnoreCase(EmptyEncrypted) || str_mobileno.equalsIgnoreCase(EmptyEncrypted) || str_username.equalsIgnoreCase(EmptyEncrypted) || str_password.equalsIgnoreCase(EmptyEncrypted) || str_confirmpassword.equalsIgnoreCase(EmptyEncrypted) || str_otherdetails.equalsIgnoreCase(EmptyEncrypted) || str_brand.equalsIgnoreCase(EmptyEncrypted) || str_model.equalsIgnoreCase(EmptyEncrypted) || str_color.equalsIgnoreCase(EmptyEncrypted) || str_plateno.equalsIgnoreCase(EmptyEncrypted) || str_otherdetails2.equalsIgnoreCase(EmptyEncrypted))
                {
                    Toast toast = Toast.makeText(getApplicationContext(),"Please fill up all required fields.", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL,0,80);
                    toast.show();
                }
                else
                {
                    if(!str_password.equalsIgnoreCase(str_confirmpassword))
                    {
                        Toast toast = Toast.makeText(getApplicationContext(),"Your password doesn`t match.", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL,0,80);
                        toast.show();
                    }
                    else
                    {
                        if(isValidPassword(AesCipher.decrypt(secretKey, str_password).toString())){
                            validate_username_if_duplicate();
                        }else{
                            Toast toast = Toast.makeText(getApplicationContext(),"Password must be 8-16 characters and contain both numbers and special characters/letters with upper and lower case.",Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,80);
                            toast.show();
                        }
                    }
                }
            }
        });
        pd = new ProgressDialog(this);
        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "yyyy/MM/dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                et_birthdate.setText(sdf.format(myCalendar.getTime()));
            }

        };

        et_birthdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(CreateAccount.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();
        // Check which checkbox was clicked
        switch (view.getId()) {
            case R.id.cb_showcreateaccountpass:
                if (checked) {
                    et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    et_confirmpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    et_confirmpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                break;
        }
    }

    public void validate_username_if_duplicate() {
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
                        Toast toast = Toast.makeText(getApplicationContext(), "Username is already exist.", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 80);
                        toast.show();
                    }else{
                        Intent i = new Intent(CreateAccount.this, DataPrivacyConsent.class);
                        i.putExtra("str_firstname", str_firstname);
                        i.putExtra("str_lastname",str_lastname);
                        i.putExtra("str_birthdate",str_birthdate);
                        i.putExtra("str_sex",str_sex);
                        i.putExtra("str_permanentaddress",str_permanentaddress);
                        i.putExtra("str_mobileno",str_mobileno);
                        i.putExtra("str_username",str_username);
                        i.putExtra("str_password",str_password);
                        i.putExtra("str_license_type",str_license_type);
                        i.putExtra("str_otherdetails",str_otherdetails);
                        i.putExtra("str_brand",str_brand);
                        i.putExtra("str_model",str_model);
                        i.putExtra("str_color",str_color);
                        i.putExtra("str_plateno",str_plateno);
                        i.putExtra("str_otherdetails2",str_otherdetails2);
                        startActivity(i);
                        pd.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                }
            }
        });
        mo.adddata("et_username", str_username);
        mo.execute(Globalvars.online_link + "validateUsername");
    }

    public boolean isValidPassword(final String password) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,16}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        license_type = paths[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
