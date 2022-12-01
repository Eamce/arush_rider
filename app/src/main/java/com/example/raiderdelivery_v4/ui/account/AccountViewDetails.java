package com.example.raiderdelivery_v4.ui.account;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.raiderdelivery_v4.ui.global.Ajax;
import com.example.raiderdelivery_v4.ui.global.Globalvars;
import com.example.raiderdelivery_v4.R;
import com.example.raiderdelivery_v4.ui.login.Login;

import org.json.JSONArray;
import org.json.JSONException;

public class AccountViewDetails extends AppCompatActivity {
    ProgressDialog pd;
    Ajax mo;
    String id, name, birthday, gender, address, contactno, license, other_details, zone, motorcycle, plate_no, motorcycle_other_details, bcode, username, create_at;
    EditText et_account_view_details_id, et_account_view_details_name, et_account_view_details_birthday, et_account_view_details_gender, et_account_view_details_address, et_account_view_details_contact, et_account_view_details_license, et_account_view_details_other_details, et_account_view_details_zone, et_account_view_details_motorcycle, et_account_view_details_plateno, et_account_view_details_motorcycle_other_details, et_account_view_details_bcode, et_account_view_details_username, et_account_view_details_create_at;
    Globalvars globalvars;
    Button btn_close;
    Handler handler;
    Runnable r;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_view_details);

        et_account_view_details_id = findViewById(R.id.et_account_view_details_id);
        et_account_view_details_name = findViewById(R.id.et_account_view_details_name);
        et_account_view_details_birthday = findViewById(R.id.et_account_view_details_birthday);
        et_account_view_details_gender = findViewById(R.id.et_account_view_details_gender);
        et_account_view_details_address = findViewById(R.id.et_account_view_details_address);
        et_account_view_details_contact = findViewById(R.id.et_account_view_details_contact);
        et_account_view_details_license = findViewById(R.id.et_account_view_details_license);
        et_account_view_details_other_details = findViewById(R.id.et_account_view_details_other_details);
        et_account_view_details_zone = findViewById(R.id.et_account_view_details_zone);
        et_account_view_details_motorcycle = findViewById(R.id.et_account_view_details_motorcycle);
        et_account_view_details_plateno = findViewById(R.id.et_account_view_details_plateno);
        et_account_view_details_motorcycle_other_details = findViewById(R.id.et_account_view_details_motorcycle_other_details);
        et_account_view_details_bcode = findViewById(R.id.et_account_view_details_bcode);
        et_account_view_details_username = findViewById(R.id.et_account_view_details_username);
        et_account_view_details_create_at = findViewById(R.id.et_account_view_details_create_at);
        btn_close = findViewById(R.id.btn_account_view_details_close);

        et_account_view_details_id.setEnabled(false);
        et_account_view_details_name.setEnabled(false);
        et_account_view_details_birthday.setEnabled(false);
        et_account_view_details_gender.setEnabled(false);
        et_account_view_details_address.setEnabled(false);
        et_account_view_details_contact.setEnabled(false);
        et_account_view_details_license.setEnabled(false);
        et_account_view_details_other_details.setEnabled(false);
        et_account_view_details_zone.setEnabled(false);
        et_account_view_details_motorcycle.setEnabled(false);
        et_account_view_details_plateno.setEnabled(false);
        et_account_view_details_motorcycle_other_details.setEnabled(false);
        et_account_view_details_bcode.setEnabled(false);
        et_account_view_details_username.setEnabled(false);
        et_account_view_details_create_at.setEnabled(false);

        globalvars = new Globalvars((Context) this, (Activity) this);

        get_rider_details();

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AccountViewDetails.this, AccountActivity.class);
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
                    View dialogView = LayoutInflater.from(AccountViewDetails.this).inflate(R.layout.my_dialog_session_timedout, viewGroup, false);
                    TextView tv_dialog_message = dialogView.findViewById(R.id.tv_dialog_message);
                    tv_dialog_message.setText("Your session has timed out. Please login again.");
                    //Now we need an AlertDialog.Builder object
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(AccountViewDetails.this);
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
                            Intent intent = new Intent(AccountViewDetails.this, Login.class);
                            startActivity(intent);
                        }
                    });
                }
            }
        };
        startHandler();
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

    public void get_rider_details() {

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
                    if (thedata.length() > 0) {
                        for (int a = 0; a < thedata.length(); a++) {
                            JSONArray row = thedata.getJSONArray(a);
                            id = row.getString(0);
                            name = row.getString(1) + " " + row.getString(2);
                            birthday = row.getString(3);
                            gender = row.getString(4);
                            address = row.getString(5);
                            contactno = row.getString(6);
                            license = row.getString(7);
                            other_details = row.getString(8);
                            //zone = row.getString(9);
                            motorcycle = row.getString(9) + " " + row.getString(10) + " (" + row.getString(11) + ")";
                            plate_no = row.getString(12);
                            motorcycle_other_details = row.getString(13);
                            //bcode = row.getString(15);
                            username = row.getString(14);
                            create_at = row.getString(15);

                        }

                        et_account_view_details_id.setText(id);
                        et_account_view_details_name.setText(name);
                        et_account_view_details_birthday.setText(birthday);
                        et_account_view_details_gender.setText(gender);
                        et_account_view_details_address.setText(address);
                        et_account_view_details_contact.setText(contactno);
                        et_account_view_details_license.setText(license);
                        et_account_view_details_other_details.setText(other_details);
                        et_account_view_details_zone.setText(zone);
                        et_account_view_details_motorcycle.setText(motorcycle);
                        et_account_view_details_plateno.setText(plate_no);
                        et_account_view_details_motorcycle_other_details.setText(motorcycle_other_details);
                        et_account_view_details_bcode.setText(bcode);
                        et_account_view_details_username.setText(username);
                        et_account_view_details_create_at.setText(create_at);


                        if (!et_account_view_details_id.getText().toString().equals("")) {
                            et_account_view_details_id.setText(id);
                        } else {
                            et_account_view_details_id.setText("N/A");
                        }

                        if (!et_account_view_details_name.getText().toString().equals("")) {
                            et_account_view_details_name.setText(name);
                        } else {
                            et_account_view_details_name.setText("N/A");
                        }

                        //et_account_view_details_name.setText(name);

                        if (!et_account_view_details_birthday.getText().toString().equals("")) {
                            et_account_view_details_birthday.setText(birthday);
                        } else {
                            et_account_view_details_birthday.setText("N/A");
                        }

                        //et_account_view_details_birthday.setText(birthday);

                        if (!et_account_view_details_gender.getText().toString().equals("")) {
                            et_account_view_details_gender.setText(gender);
                        } else {
                            et_account_view_details_gender.setText("N/A");
                        }

                        //et_account_view_details_gender.setText(gender);

                        if (!et_account_view_details_address.getText().toString().equals("")) {
                            et_account_view_details_address.setText(address);
                        } else {
                            et_account_view_details_address.setText("N/A");
                        }

                        //et_account_view_details_address.setText(address);

                        if (!et_account_view_details_contact.getText().toString().equals("")) {
                            et_account_view_details_contact.setText(contactno);
                        } else {
                            et_account_view_details_contact.setText("N/A");
                        }

                        //et_account_view_details_contact.setText(contactno);

                        if (!et_account_view_details_license.getText().toString().equals("")) {
                            et_account_view_details_license.setText(license);
                        } else {
                            et_account_view_details_license.setText("N/A");
                        }

                        //et_account_view_details_license.setText(license);

                        if (!et_account_view_details_other_details.getText().toString().equals("")) {
                            et_account_view_details_other_details.setText(other_details);
                        } else {
                            et_account_view_details_other_details.setText("N/A");
                        }

                        //et_account_view_details_other_details.setText(other_details);

                        if (!et_account_view_details_zone.getText().toString().equals("")) {
                            et_account_view_details_zone.setText(zone);
                        } else {
                            et_account_view_details_zone.setText("N/A");
                        }

                        //et_account_view_details_zone.setText(zone);

                        if (!et_account_view_details_motorcycle.getText().toString().equals("")) {
                            et_account_view_details_motorcycle.setText(motorcycle);
                        } else {
                            et_account_view_details_motorcycle.setText("N/A");
                        }

                        //et_account_view_details_motorcycle.setText(motorcycle);

                        if (!et_account_view_details_plateno.getText().toString().equals("")) {
                            et_account_view_details_plateno.setText(plate_no);
                        } else {
                            et_account_view_details_plateno.setText("N/A");
                        }

                        //et_account_view_details_plateno.setText(plate_no);

                        if (!et_account_view_details_motorcycle_other_details.getText().toString().equals("")) {
                            et_account_view_details_motorcycle_other_details.setText(motorcycle_other_details);
                        } else {
                            et_account_view_details_motorcycle_other_details.setText("N/A");
                        }

                        //et_account_view_details_motorcycle_other_details.setText(motorcycle_other_details);

                        if (!et_account_view_details_bcode.getText().toString().equals("")) {
                            et_account_view_details_bcode.setText(bcode);
                        } else {
                            et_account_view_details_bcode.setText("N/A");
                        }

                        //et_account_view_details_bcode.setText(bcode);

                        if (!et_account_view_details_username.getText().toString().equals("")) {
                            et_account_view_details_username.setText(username);
                        } else {
                            et_account_view_details_username.setText("N/A");
                        }

                        //et_account_view_details_username.setText(username);

                        if (!et_account_view_details_create_at.getText().toString().equals("")) {
                            et_account_view_details_create_at.setText(create_at);
                        } else {
                            et_account_view_details_create_at.setText("N/A");
                        }

                        //et_account_view_details_create_at.setText(create_at);

                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), "No data found!", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 80);
                        toast.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    pd.dismiss();
                }
            }
        });

        mo.adddata("r_id_num", globalvars.get("r_id_num"));
        mo.execute(Globalvars.online_link + "view_rider_details");
    }
}
