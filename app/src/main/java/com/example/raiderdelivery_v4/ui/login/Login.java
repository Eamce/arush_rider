package com.example.raiderdelivery_v4.ui.login;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.raiderdelivery_v4.ui.account.CreateAccount;
import com.example.raiderdelivery_v4.ui.account.ForgotPassword;
import com.example.raiderdelivery_v4.ui.global.AesCipher;
import com.example.raiderdelivery_v4.ui.global.Ajax;
import com.example.raiderdelivery_v4.ui.global.Globalvars;
import com.example.raiderdelivery_v4.ui.home.HomeActivity;
import com.example.raiderdelivery_v4.R;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.File;
import static com.example.raiderdelivery_v4.ui.global.Globalvars.secretKey;

public class Login extends AppCompatActivity {

    SQLiteDatabase mydatabase;
    TextView et_username, et_password, tv_createaccount, tv_forget_password;
    Button btn_login, btn_cancel;
    CheckBox cb_showpassword;
    String username, password;
    Ajax mo;
    ProgressDialog pd;
    int status = 0, attempt = 0;
    Activity activity = this;
    Context context = this;
    Globalvars globalvars;
    User u = new User();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        et_username = (TextView)findViewById(R.id.txtusername);
        et_password = (TextView)findViewById(R.id.txtpassword);
        btn_login = (Button)findViewById(R.id.btnlogin);
        btn_cancel = (Button)findViewById(R.id.btncancel);
        cb_showpassword = (CheckBox)findViewById(R.id.cb_showpassword);
        tv_createaccount = (TextView)findViewById(R.id.tv_createaccount);
        tv_forget_password = (TextView)findViewById(R.id.tv_forgot_password);

        mydatabase = openOrCreateDatabase("alturas_rider.db",MODE_PRIVATE,null);
        createdatabases();

        globalvars = new Globalvars((Context)this,(Activity)this);
        pd = new ProgressDialog(this);

        btn_login.setOnClickListener(new View.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v)
            {
                username = et_username.getText().toString().trim();
                password = et_password.getText().toString().trim();
                AesCipher encrypted_username = AesCipher.encrypt(secretKey, username);
                AesCipher encrypted_password = AesCipher.encrypt(secretKey, password);
                if(username.equalsIgnoreCase("") || password.equalsIgnoreCase(""))
                {
                    Toast toast = Toast.makeText(getApplicationContext(),"Invalid username/password.",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,80);
                    toast.show();
                }
                else
                {
                    //Validate user...
                    validate_user(encrypted_username.toString(),encrypted_password.toString());
                }
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity(); // Close all activites
                System.exit(0);  // closing files, releasing resources
            }
        });

        tv_createaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, CreateAccount.class);
                startActivity(i);
            }
        });

        tv_forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                globalvars.set("OTPType", "ForgotPassword");
                Intent i = new Intent(Login.this, ForgotPassword.class);
                startActivity(i);
            }
        });

        globalvars.set("login_username", "");
        globalvars.set("login_attempt", attempt+"");

            if (isRooted()) {
            //before inflating the custom alert dialog layout, we will get the current activity viewgroup
            ViewGroup viewGroup = findViewById(android.R.id.content);

            //then we will inflate the custom alert dialog xml that we created
            View dialogView = LayoutInflater.from(Login.this).inflate(R.layout.my_dialog_rooted_error, viewGroup, false);
            TextView tv_dialog_message = dialogView.findViewById(R.id.tv_dialog_message);
            tv_dialog_message.setText("This app will not work in rooted devices.");
            //Now we need an AlertDialog.Builder object
            AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
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
                    finish();
                }
            });
        } else {
        checkSharedPref();
        }
    }


    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.cb_showpassword:
                if (checked)
                {
                    et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            else
                {
                    et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                break;
        }
    }

    private void checkSharedPref(){
        if(globalvars.get("username") != null)
        {
            Intent i = new Intent(Login.this, HomeActivity.class);
            startActivity(i);
        }
    }

    private static boolean isRooted() {
        return findBinary("su");
    }

    public static boolean findBinary(String binaryName) {
        boolean found = false;
        if (!found) {
            String[] places = { "/sbin/", "/system/bin/", "/system/xbin/",
                    "/data/local/xbin/", "/data/local/bin/",
                    "/system/sd/xbin/", "/system/bin/failsafe/", "/data/local/" };
            for (String where : places) {
                if (new File(where + binaryName).exists()) {
                    found = true;

                    break;
                }
            }
        }
        return found;
    }

    private void createdatabases() {
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS users(id INTEGER PRIMARY KEY," +
                "emp_id TEXT," +
                "username TEXT," +
                "password TEXT," +
                "emp_no TEXT," +
                "emp_pins TEXT," +
                "emp_name TEXT," +
                "company_code TEXT," +
                "bunit_code TEXT," +
                "dept_code TEXT," +
                "usertype TEXT)");
    }


    @Override
    public void onBackPressed() {
        // super.onBackPressed(); commented this line in order to disable back press
    }

    public void validate_user(final String user, final String pass)
    {

        pd.setMessage("Validating user...Please wait");
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
                JSONArray thedata;
                try
                {
                    thedata = new JSONArray(data);
                    String row = thedata.getString(0);
                    if(row.equalsIgnoreCase("Account blocked"))
                    {
                        pd.dismiss();
                        Toast toast1 = Toast.makeText(getApplicationContext(), "Your Account has been blocked.", Toast.LENGTH_SHORT);
                        toast1.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 80);
                        toast1.show();
                        globalvars.set("OTPType", "AccountBlocked");
                        Intent i = new Intent(Login.this, ForgotPassword.class);
                        startActivity(i);
                    }
                    else
                    {
                        if (row.equalsIgnoreCase("Success"))
                        {
                            pd.dismiss();
                            get_users_data(user, pass);
                        }
                        if(!globalvars.get("login_username").equalsIgnoreCase(user) && !row.equalsIgnoreCase("Account doesnt exist"))
                        {
                            attempt = 0;
                            globalvars.set("login_username", "");
                            globalvars.set("login_attempt", "");
                        }
                        if(row.equalsIgnoreCase("Incorrect password"))
                        {
                            globalvars.set("login_username", user);
                            globalvars.set("login_attempt", attempt+"");

                            if(globalvars.get("login_attempt").equalsIgnoreCase("3"))
                            {
                                update_rider_blocked_status(user);
                            }
                            else
                            {
                                pd.dismiss();
                                attempt++;
                                Toast toast = Toast.makeText(getApplicationContext(), "Incorrect password.", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 80);
                                toast.show();
                            }


                        }
                        if(row.equalsIgnoreCase("Account doesnt exist"))
                        {
                            Toast toast = Toast.makeText(getApplicationContext(), "Account doesnt exist.", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 80);
                            toast.show();
                            pd.dismiss();
                        }
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        });

//        String a1 = "vigor45";
//        AesCipher encrypted = AesCipher.encrypt(secretKey, a1);
//        String c1 = encrypted.toString();

//        String a = user;
//        String d = "zruaCFExZmspONEsW8p/Ng=="; //24char...
//        //String d = "VDFnOTk0eG8yVUFxRzgxTc67mghRMWZrKTjRLFvKfzY="; //44char...
//        AesCipher dencrypted = AesCipher.decrypt(secretKey, d);
//        String c = dencrypted.toString();

        mo.adddata("username",user);
        mo.adddata("password",pass);

        mo.execute(Globalvars.online_link + "validate_login_with_security");
    }

    public void update_rider_blocked_status(String user)
    {
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
                Toast toast1 = Toast.makeText(getApplicationContext(), "Your Account has been blocked.", Toast.LENGTH_SHORT);
                toast1.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 80);
                toast1.show();
                globalvars.set("OTPType", "AccountBlocked");
                Intent i = new Intent(Login.this, ForgotPassword.class);
                startActivity(i);
            }
        });

        mo.adddata("username",user);
        mo.execute(Globalvars.online_link + "update_rider_blocked_status");
    }

    public void get_users_data(String user, String pass)
    {
        final ProgressDialog pd1 = new ProgressDialog(this);
        pd1.setMessage("Validating user...Please wait");
        pd1.show();
        pd1.setCancelable(false);
        pd1.setCanceledOnTouchOutside(false);
        mo = new Ajax();
        mo.setCustomObjectListener(new Ajax.MyCustomObjectListener()
        {
            @Override
            public void onerror()
            {
                pd1.dismiss();
                Toast toast = Toast.makeText(getApplicationContext(),"Unable to connect. Please check your connection.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL,0,80);
                toast.show();
            }
            @Override
            public void onsuccess(String data)
            {
                JSONArray thedata;
                try
                {
                    thedata = new JSONArray(data);
                    String id, username, password, fname;
                    if(thedata.length() > 0)
                    {
                        for (int a = 0; a < thedata.length(); a++)
                        {
                            JSONArray row = thedata.getJSONArray(a);
                            u.set_id(row.getString(0));
                            u.set_username(row.getString(1));
                            u.set_password(row.getString(2));
                            u.set_fname(row.getString(3));
                            u.set_lname(row.getString(4));
                            u.set_image(row.getString(5));
                            u.set_r_id_num(row.getString(6));
                            //u.set_r_bunit_code(row.getString(7));
                        }
                        pd1.dismiss();
                        status = 2;
                        //db.createdatabases(mydatabase);
                        //Setting user preferences...
                        globalvars.set("id", u.get_id());
                        globalvars.set("username", u.get_username());
                        globalvars.set("password", u.get_password());
                        globalvars.set("fname", u.get_fname()+" "+u.get_lname());
                        globalvars.set("image", u.get_image());
                        globalvars.set("r_id_num",u.get_r_id_num());
                        //globalvars.set("bunit_code",u.get_r_bunit_code());
                        globalvars.set("category","Food");
                        globalvars.activity = activity;
                        globalvars.context = context;

                        //finish();
                        Toast toast = Toast.makeText(getApplicationContext(),"Welcome, "+globalvars.get("fname")+"!",Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,80);
                        toast.show();
                        Intent i = new Intent(Login.this, HomeActivity.class);
                        startActivity(i);
                    }
                    else
                    {
                        pd1.dismiss();
                    }

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        });

        mo.adddata("username",user);
        mo.adddata("password",pass);
//        mo.execute(Globalvars.online_link + "validateUsername");
        mo.execute(Globalvars.online_link + "validateloginwithencryption");
//        mo.execute(Globalvars.offline_link + "validate_login");
    }

}