package com.example.raiderdelivery_v4.ui.transaction.food;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.raiderdelivery_v4.R;
import com.example.raiderdelivery_v4.ui.global.Ajax;
import com.example.raiderdelivery_v4.ui.global.Globalvars;
import com.example.raiderdelivery_v4.ui.history.food.HistoryActivity;
import com.example.raiderdelivery_v4.ui.login.Login;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class TransactionViewCustomerDetails extends AppCompatActivity {
    Ajax mo;
    ProgressDialog pd1;
    CircleImageView civ_customer_profilepic;
    TextView tv_customer_acc_fullname;
    TextView et_customer_address1, et_customer_address1_landmark, et_customer_address2, et_customer_address2_landmark, et_customer_address3, et_customer_address3_landmark, et_customer_mobile1, et_customer_mobile2, et_customer_mobile3;
    Button btn_customer_details_cancel, btn_address1, btn_address2, btn_address3, btn_mobile1, btn_mobile2, btn_mobile3, btn_address1_landmark, btn_address2_landmark, btn_address3_landmark;
    ArrayList<DownloadedCustomerData> itemlist;
    String id, fullname, mobile_no, house_no, street, barangay, town, landmark, customer_id, order_from, activity, str_ticket_id;
    String[] mobile_no_arr, house_no_arr, street_arr, barangay_arr, town_arr, landmark_arr;
    Boolean web_is_empty = false, mobile_is_empty = false;
    int house_no_arr_length = 0, street_arr_length = 0, barangay_arr_length = 0, town_arr_length = 0, landmark_arr_length = 0;
    Handler handler;
    Runnable r;
    Globalvars globalvars;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_view_customer_detail);
        civ_customer_profilepic = findViewById(R.id.civ_customer_profilepic);
        tv_customer_acc_fullname = findViewById(R.id.tv_customer_acc_fullname);
        et_customer_address1 = findViewById(R.id.et_customer_address1);
        et_customer_address2 = findViewById(R.id.et_customer_address2);
        et_customer_address3 = findViewById(R.id.et_customer_address3);
        et_customer_address1_landmark = findViewById(R.id.et_customer_address1_landmark);
        et_customer_address2_landmark = findViewById(R.id.et_customer_address2_landmark);
        et_customer_address3_landmark = findViewById(R.id.et_customer_address3_landmark);
        et_customer_mobile1 = findViewById(R.id.et_customer_mobile1);
        et_customer_mobile2 = findViewById(R.id.et_customer_mobile2);
        et_customer_mobile3 = findViewById(R.id.et_customer_mobile3);
        btn_address1 = findViewById(R.id.btn_address1);
        btn_address2 = findViewById(R.id.btn_address2);
        btn_address3 = findViewById(R.id.btn_address3);
        btn_address1_landmark = findViewById(R.id.btn_address1_landmark);
        btn_address2_landmark = findViewById(R.id.btn_address2_landmark);
        btn_address3_landmark = findViewById(R.id.btn_address3_landmark);
        btn_mobile1 = findViewById(R.id.btn_mobile1);
        btn_mobile2 = findViewById(R.id.btn_mobile2);
        btn_mobile3 = findViewById(R.id.btn_mobile3);
        btn_customer_details_cancel = findViewById(R.id.btn_customer_details_cancel);
        globalvars = new Globalvars(this, this);

        Intent i = getIntent();
        customer_id = i.getExtras().getString("cus_id");
        order_from = i.getExtras().getString("order_from");
        activity = i.getExtras().getString("activity");
        str_ticket_id = i.getExtras().getString("ticket_id");

        getCustomerDetails();


        btn_customer_details_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity.equals("TransactionActivity")) {
                    Intent i = new Intent(TransactionViewCustomerDetails.this, TransactionActivity.class);
                    startActivity(i);
                } else {
                    Intent i = new Intent(TransactionViewCustomerDetails.this, HistoryActivity.class);
                    startActivity(i);
                }
            }
        });

        btn_address1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!et_customer_address1.getText().toString().equals("N/A")) {
                    copyToClipBoard(et_customer_address1.getText().toString());
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/@12.867031,121.766552,5z"));
                    startActivity(browserIntent);
                }
            }
        });

        btn_address2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!et_customer_address2.getText().toString().equals("N/A")) {
                    copyToClipBoard(et_customer_address2.getText().toString());
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/@12.867031,121.766552,5z"));
                    startActivity(browserIntent);
                }
            }
        });

        btn_address3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!et_customer_address3.getText().toString().equals("N/A")) {
                    copyToClipBoard(et_customer_address3.getText().toString());
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/@12.867031,121.766552,5z"));
                    startActivity(browserIntent);
                }
            }
        });

        btn_address1_landmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!et_customer_address1_landmark.getText().toString().equals("N/A")) {
                    copyToClipBoard(et_customer_address1_landmark.getText().toString());
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/@12.867031,121.766552,5z"));
                    startActivity(browserIntent);
                }
            }
        });

        btn_address2_landmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!et_customer_address2_landmark.getText().toString().equals("N/A")) {
                    copyToClipBoard(et_customer_address2_landmark.getText().toString());
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/@12.867031,121.766552,5z"));
                    startActivity(browserIntent);
                }
            }
        });

        btn_address3_landmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!et_customer_address3_landmark.getText().toString().equals("N/A")) {
                    copyToClipBoard(et_customer_address3_landmark.getText().toString());
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/@12.867031,121.766552,5z"));
                    startActivity(browserIntent);
                }
            }
        });

        btn_mobile1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!et_customer_mobile1.getText().toString().equals("N/A")) {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:" + et_customer_mobile1.getText().toString()));
                    startActivity(callIntent);
                }
            }
        });
        btn_mobile2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!et_customer_mobile2.getText().toString().equals("N/A")) {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:" + et_customer_mobile2.getText().toString()));
                    startActivity(callIntent);
                }
            }
        });
        btn_mobile3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!et_customer_mobile3.getText().toString().equals("N/A")) {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:" + et_customer_mobile3.getText().toString()));
                    startActivity(callIntent);
                }
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
                    View dialogView = LayoutInflater.from(TransactionViewCustomerDetails.this).inflate(R.layout.my_dialog_session_timedout, viewGroup, false);
                    TextView tv_dialog_message = dialogView.findViewById(R.id.tv_dialog_message);
                    tv_dialog_message.setText("Your session has timed out. Please login again.");
                    //Now we need an AlertDialog.Builder object
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(TransactionViewCustomerDetails.this);
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
                            Intent intent = new Intent(TransactionViewCustomerDetails.this, Login.class);
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

    // Copy EditCopy text to the ClipBoard
    private void copyToClipBoard(String text) {
        ClipboardManager clipMan = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", text);
        clipMan.setPrimaryClip(clip);
    }

    public void getCustomerDetails() {
        pd1 = new ProgressDialog(this);
        pd1.setMessage("Please wait...");
        pd1.show();
        pd1.setCancelable(false);
        pd1.setCanceledOnTouchOutside(false);
        itemlist = new ArrayList<>();
        mo = new Ajax();
        mo.setCustomObjectListener(new Ajax.MyCustomObjectListener() {
            @Override
            public void onerror() {
                pd1.dismiss();
                Toast toast = Toast.makeText(getApplicationContext(), "Unable to connect. Please check your connection.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 80);
                toast.show();
            }

            @Override
            public void onsuccess(String data) {
                JSONArray thedata;
                try {
                    thedata = new JSONArray(data);
                    int b = thedata.length();
                    if (thedata.length() > 0) {
                        for (int a = 0; a < thedata.length(); a++) {
                            JSONArray row = thedata.getJSONArray(a);
                            id = row.getString(0);
                            fullname = row.getString(1);
                            mobile_no = row.getString(2);
                            house_no = row.getString(3);
                            street = row.getString(4);
                            barangay = row.getString(5);
                            town = row.getString(6);
                            landmark = row.getString(7);
                        }
                    } else {
                        pd1.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    pd1.dismiss();
                    setCustomerDetails();
                    Tag_as_viewed();
                }
            }
        });
        mo.adddata("ticket_id", str_ticket_id);
        mo.execute(Globalvars.online_link + "get_customer_details");
    }

//    public void getCustomerDetailsFromMobile1() {
//        pd1 = new ProgressDialog(this);
//        pd1.setMessage("Please wait...");
//        pd1.show();
//        pd1.setCancelable(false);
//        pd1.setCanceledOnTouchOutside(false);
//        itemlist = new ArrayList<>();
//        mo = new Ajax();
//        mo.setCustomObjectListener(new Ajax.MyCustomObjectListener() {
//            @Override
//            public void onerror() {
//                pd1.dismiss();
//                Toast toast = Toast.makeText(getApplicationContext(), "Unable to connect. Please check your connection.", Toast.LENGTH_LONG);
//                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 80);
//                toast.show();
//            }
//
//            @Override
//            public void onsuccess(String data) {
//                JSONArray thedata;
//                try {
//                    thedata = new JSONArray(data);
//                    int b = thedata.length();
//                    if (thedata.length() > 0) {
//                        for (int a = 0; a < thedata.length(); a++) {
//                            JSONArray row = thedata.getJSONArray(a);
//                            id = row.getString(0);
//                            fullname = row.getString(1);
//                            mobile_no = row.getString(2);
//                            house_no = row.getString(3);
//                            street = row.getString(4);
//                            barangay = row.getString(5);
//                            town = row.getString(6);
//                            landmark = row.getString(7);
//                        }
//                    } else {
//                        //web_is_empty = true;
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                } finally {
//                    setCustomerDetails_from_mobile();
//                    //getCustomerDetailsFromMobile2();
//                }
//            }
//        });
//        mo.adddata("customer_id", customer_id);
//        mo.adddata("details_type", "signup");
//        mo.execute(Globalvars.offline_link + "get_customer_details_from_mobile");
//    }

//    public void getCustomerDetailsFromMobile2() {
//        mo = new Ajax();
//        mo.setCustomObjectListener(new Ajax.MyCustomObjectListener() {
//            @Override
//            public void onerror() {
//                pd1.dismiss();
//                Toast toast = Toast.makeText(getApplicationContext(), "Unable to connect. Please check your connection.", Toast.LENGTH_LONG);
//                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 80);
//                toast.show();
//            }
//
//            @Override
//            public void onsuccess(String data) {
//                JSONArray thedata;
//                try {
//                    thedata = new JSONArray(data);
//                    int b = thedata.length();
//                    if (thedata.length() > 0) {
//                        for (int a = 0; a < thedata.length(); a++) {
//                            JSONArray row = thedata.getJSONArray(a);
//                            id = row.getString(0);
//                            fullname = row.getString(1);
//                            mobile_no = row.getString(2);
//                            house_no = row.getString(3);
//                            street = row.getString(4);
//                            barangay = row.getString(5);
//                            town = row.getString(6);
//                            landmark = row.getString(7);
//                        }
//                        setCustomerDetails_from_mobile2();
//                        Tag_as_viewed();
//                    } else {
//                        if (web_is_empty) {
//                            setCustomerDetails_from_mobile();
//                        } else {
//                            setCustomerDetails_from_mobile2();
//                        }
//                        Tag_as_viewed();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                } finally {
//
//                }
//            }
//        });
//        mo.adddata("customer_id", customer_id);
//        mo.adddata("details_type", "checkout");
//        mo.execute(Globalvars.offline_link + "get_customer_details_from_mobile");
//    }

    public void setCustomerDetails() {

        address1();
        mobile_no1();

        tv_customer_acc_fullname.setText(fullname);
        disableEditText();
    }


    public void disableEditText() {
        et_customer_address1.setEnabled(false);
        et_customer_address2.setEnabled(false);
        et_customer_address3.setEnabled(false);
        et_customer_mobile1.setEnabled(false);
        et_customer_mobile2.setEnabled(false);
        et_customer_mobile3.setEnabled(false);
    }

    public void address1() {
        et_customer_address1.setText(validateNull(house_no + ", " + street) + ", " + barangay + ", " + town);
        et_customer_address1_landmark.setText(validateNull(landmark));
        et_customer_address2.setText("N/A");
        et_customer_address2_landmark.setText("N/A");
        et_customer_address3.setText("N/A");
        et_customer_address3_landmark.setText("N/A");
    }

    public void mobile_no1() {
        et_customer_mobile1.setText(validateNull(mobile_no));
        et_customer_mobile2.setText("N/A");
        et_customer_mobile3.setText("N/A");
    }

    public String validateNull(String text) {
        String return_text;
        if (text.isEmpty()) {
            return_text = "";
        } else {
            return_text = text;
        }
        return return_text;
    }

    public void Tag_as_viewed() {
        mo = new Ajax();
        mo.setCustomObjectListener(new Ajax.MyCustomObjectListener() {
            @Override
            public void onerror() {
                pd1.dismiss();
                Toast toast = Toast.makeText(getApplicationContext(), "Unable to connect. Please check your connection.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 80);
                toast.show();
            }

            @Override
            public void onsuccess(String data) {
                pd1.dismiss();
            }
        });

        mo.adddata("ticket_id", str_ticket_id);
        mo.execute(Globalvars.online_link + "update_viewed_status");
    }
}
