package com.example.raiderdelivery_v4.ui.transaction.food;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.raiderdelivery_v4.R;
import com.example.raiderdelivery_v4.ui.global.Ajax;
import com.example.raiderdelivery_v4.ui.global.Globalvars;
import com.example.raiderdelivery_v4.ui.history.food.HistoryActivity;
import com.example.raiderdelivery_v4.ui.login.Login;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TransactionViewTimeFrame2 extends AppCompatActivity {
    LinearLayout ll_main_container;
    Ajax mo;
    ArrayList itemlist;
    Globalvars globalvars;
    String id, order_submitted, tag_as_food_preparation ,tag_as_for_pickup,trans_at, delivered_at, tenant_id, bu, tenant_name, tenant_logo;
    ProgressDialog pd;
    String str_ticket_id, activity;
    Button btn_close;
    Handler handler;
    Runnable r;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_view_timeframe);
        ll_main_container = findViewById(R.id.ll_timeframe_main_container);
        btn_close = findViewById(R.id.btn_close);
        globalvars = new Globalvars((Context) this, (Activity) this);
        Intent intent = getIntent();
        str_ticket_id = intent.getExtras().getString("ticket_id");
        activity = intent.getExtras().getString("activity");
        this.setTitle(intent.getExtras().getString("customer_name") + "(Customer)");
        CreateLayout();

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity.equals("TransactionActivity")) {
                    Intent i = new Intent(TransactionViewTimeFrame2.this, TransactionActivity.class);
                    startActivity(i);
                } else {
                    Intent i = new Intent(TransactionViewTimeFrame2.this, HistoryActivity.class);
                    startActivity(i);
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
                    View dialogView = LayoutInflater.from(TransactionViewTimeFrame2.this).inflate(R.layout.my_dialog_session_timedout, viewGroup, false);
                    TextView tv_dialog_message = dialogView.findViewById(R.id.tv_dialog_message);
                    tv_dialog_message.setText("Your session has timed out. Please login again.");
                    //Now we need an AlertDialog.Builder object
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(TransactionViewTimeFrame2.this);
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
                            Intent intent = new Intent(TransactionViewTimeFrame2.this, Login.class);
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

    public void CreateLayout(){

        itemlist = new ArrayList<>();
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
                String order_submitted_to_tag_as_food_preparation = "";
                String food_preparation_to_tag_as_tag_as_for_pickup = "";
                String tag_as_for_pickup_to_trans_at = "";
                String tag_as_trans_at_to_delivered_at = "";
                String tag_as_delivered_at = "";

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
                //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("M-dd-yyyy hh:mm:ss");

                try {
                    thedata = new JSONArray(data);
                    if (thedata.length() > 0) {
                        for (int a = 0; a < thedata.length(); a++) {
                            JSONArray row = thedata.getJSONArray(a);
                            id = row.getString(0);
                            order_submitted = row.getString(1);
                            tag_as_food_preparation = row.getString(2);
                            tag_as_for_pickup = row.getString(3);
                            trans_at = row.getString(4);
                            delivered_at = row.getString(5);
                            tenant_id = row.getString(6);
                            bu = row.getString(7);
                            tenant_name = row.getString(8);
                            tenant_logo = row.getString(9);

                            LinearLayout ll1 = new LinearLayout(TransactionViewTimeFrame2.this);
                            ll1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                            ll1.setOrientation(LinearLayout.VERTICAL);

                            //Add new created Linear Layout to our Linear Layout Container...
                            ll_main_container.addView(ll1);

                            TextView tv0 = new TextView(TransactionViewTimeFrame2.this);
                            ImageView iv0 = new ImageView(TransactionViewTimeFrame2.this);

                            TextView tv1 = new TextView(TransactionViewTimeFrame2.this);
                            EditText et1 = new EditText(TransactionViewTimeFrame2.this);

                            TextView tv2 = new TextView(TransactionViewTimeFrame2.this);
                            TextView tv22 = new TextView(TransactionViewTimeFrame2.this);
                            EditText et2 = new EditText(TransactionViewTimeFrame2.this);

                            TextView tv3 = new TextView(TransactionViewTimeFrame2.this);
                            TextView tv33 = new TextView(TransactionViewTimeFrame2.this);
                            EditText et3 = new EditText(TransactionViewTimeFrame2.this);

                            TextView tv4 = new TextView(TransactionViewTimeFrame2.this);
                            TextView tv44 = new TextView(TransactionViewTimeFrame2.this);
                            EditText et4 = new EditText(TransactionViewTimeFrame2.this);

                            TextView tv5 = new TextView(TransactionViewTimeFrame2.this);
                            TextView tv55 = new TextView(TransactionViewTimeFrame2.this);
                            EditText et5 = new EditText(TransactionViewTimeFrame2.this);

                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            );
                            params.setMargins(0, 50, 0, 10);

                            //Creaing Linear Layout for tenant image and text...
                            LinearLayout ll2 = new LinearLayout(TransactionViewTimeFrame2.this);
                            ll2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
                            ll2.setOrientation(LinearLayout.HORIZONTAL);
                            ll2.setLayoutParams(params);

                            ll1.addView(ll2);

                            //Creaing Linear Layout for tenant image and text...
                            LinearLayout ll3 = new LinearLayout(TransactionViewTimeFrame2.this);
                            ll3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                            ll3.setOrientation(LinearLayout.VERTICAL);

                            ll1.addView(ll3);

                            //Add new created TextView and ImageView to our newly created Linear Layout...
                            ll2.addView(iv0);
                            iv0.setScaleType(ImageView.ScaleType.FIT_XY);

                            Picasso.get()
                                    .load(Globalvars.online_tenant_image_link + tenant_logo)
                                    .resize(50, 50)
                                    .centerCrop()
                                    .into(iv0);

                            ll2.addView(tv0);
                            tv0.setText(bu + "-" + tenant_name);
                            tv0.setTextSize(18);
                            tv0.setPadding(5,0,0,0);

                            et1.setEnabled(false);
                            et2.setEnabled(false);
                            et3.setEnabled(false);
                            et4.setEnabled(false);
                            et5.setEnabled(false);

                            ll3.addView(tv1);
                            tv1.setText(R.string.app_submitted);
                            tv1.setTextColor(getResources().getColor(R.color.black));
                            ll3.addView(et1);
                            et1.setText(order_submitted);
                            et1.setTextColor(getResources().getColor(R.color.teal));
                            et1.setTextSize(17);

                            ll3.addView(tv2);
                            ll3.addView(tv22);
                            tv2.setText(R.string.food_prep);
                            tv2.setTextColor(getResources().getColor(R.color.black));
                            tv22.setText(R.string.food_prep_details);
                            tv22.setTextColor(getResources().getColor(R.color.orange));
                            ll3.addView(et2);
                            et2.setText(tag_as_food_preparation);
                            et2.setTextColor(getResources().getColor(R.color.teal));
                            et2.setTextSize(17);

                            ll3.addView(tv3);
                            ll3.addView(tv33);
                            tv3.setText(R.string.pickup);
                            tv3.setTextColor(getResources().getColor(R.color.black));
                            tv33.setText(R.string.pickup_details);
                            tv33.setTextColor(getResources().getColor(R.color.orange));
                            ll3.addView(et3);
                            et3.setText(tag_as_for_pickup);
                            et3.setTextColor(getResources().getColor(R.color.teal));
                            et3.setTextSize(17);

                            ll3.addView(tv4);
                            ll3.addView(tv44);
                            tv4.setText(R.string.intransit);
                            tv4.setTextColor(getResources().getColor(R.color.black));
                            tv44.setText(R.string.intransit_details);
                            tv44.setTextColor(getResources().getColor(R.color.orange));
                            ll3.addView(et4);
                            et4.setText(trans_at);
                            et4.setTextColor(getResources().getColor(R.color.teal));
                            et4.setTextSize(17);

                            ll3.addView(tv5);
                            ll3.addView(tv55);
                            tv5.setText(R.string.delivered);
                            tv5.setTextColor(getResources().getColor(R.color.black));
                            tv55.setText(R.string.delivered_details);
                            tv55.setTextColor(getResources().getColor(R.color.orange));
                            ll3.addView(et5);
                            et5.setText(delivered_at);
                            et5.setTextColor(getResources().getColor(R.color.teal));
                            et5.setTextSize(17);

                            try {
                                Date date1 = null, date2 = null, date3 = null, date4 = null, date5 = null;

                                if(!order_submitted.equals("")) {
                                    date1 = simpleDateFormat.parse(order_submitted);
                                    et1.setText(order_submitted);
                                    if(!tag_as_food_preparation.equals("")) {
                                        date2 = simpleDateFormat.parse(tag_as_food_preparation);
                                        order_submitted_to_tag_as_food_preparation = printDifference(date1, date2);
                                        et2.setText(tag_as_food_preparation + order_submitted_to_tag_as_food_preparation);
                                        if(!tag_as_for_pickup.equals("")) {
                                            date3 = simpleDateFormat.parse(tag_as_for_pickup);
                                            tag_as_for_pickup_to_trans_at = printDifference(date2, date3);
                                            et3.setText(tag_as_for_pickup + tag_as_for_pickup_to_trans_at);
                                            if(!trans_at.equals("")) {
                                                date4 = simpleDateFormat.parse(trans_at);
                                                tag_as_trans_at_to_delivered_at = printDifference(date3, date4);
                                                et4.setText(trans_at + tag_as_trans_at_to_delivered_at);
                                                if(!delivered_at.equals("")) {
                                                    date5 = simpleDateFormat.parse(delivered_at);
                                                    tag_as_delivered_at = printDifference(date4, date5);
                                                    et5.setText(trans_at + tag_as_delivered_at);
                                                }else{
                                                    et5.setText("N/A");
                                                }
                                            }else{
                                                et4.setText("N/A");
                                                et5.setText("N/A");
                                            }
                                        }else{
                                            et3.setText("N/A");
                                            et4.setText("N/A");
                                            et5.setText("N/A");
                                        }
                                    }else{
                                        et2.setText("N/A");
                                        et3.setText("N/A");
                                        et4.setText("N/A");
                                        et5.setText("N/A");
                                    }
                                }else{
                                    et1.setText("N/A");
                                    et2.setText("N/A");
                                    et3.setText("N/A");
                                    et4.setText("N/A");
                                    et5.setText("N/A");
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }
                        Tag_as_viewed();
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
        String a = globalvars.get("r_id_num");
        String b = str_ticket_id;
        mo.adddata("r_id_num", globalvars.get("r_id_num"));
        mo.adddata("ticket_id", str_ticket_id);
        mo.execute(Globalvars.online_link + "get_tenant_timeframe2");

    }

    public String printDifference(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();
        String diff = "";

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;


        //"%d days, %d hours, %d minutes, %d seconds%n
        if(elapsedDays == 0 && elapsedHours == 0 && elapsedMinutes == 0) {
            diff =  " / " + elapsedSeconds + "sec ";
        }else if(elapsedDays == 0 && elapsedHours == 0 && elapsedMinutes > 0){
            diff = " / " + elapsedMinutes + "min & " + elapsedSeconds + "sec ";
        }else if(elapsedDays == 0 && elapsedHours > 0 && elapsedMinutes > 0){
            diff = " / " + elapsedHours + "h & " + elapsedMinutes + "min & " + elapsedSeconds + "sec ";
        }else{
            diff =  " / " + elapsedDays + "d & " + elapsedHours + "h & " + elapsedMinutes + "min & " + elapsedSeconds + "sec ";
        }
        return diff;
    }

    public void Tag_as_viewed() {
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

            }
        });

        mo.adddata("ticket_id", getIntent().getExtras().getString("ticket_id"));
        mo.execute(Globalvars.online_link + "update_viewed_status");
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed(); commented this line in order to disable back press
    }


}
