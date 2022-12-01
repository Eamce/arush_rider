package com.example.raiderdelivery_v4.ui.transaction.food;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.raiderdelivery_v4.R;
import com.example.raiderdelivery_v4.ui.global.Ajax;
import com.example.raiderdelivery_v4.ui.global.Globalvars;
import com.example.raiderdelivery_v4.ui.global.Msgbox;
import com.example.raiderdelivery_v4.ui.login.Login;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

public class TransactionViewDiscountNotTele extends AppCompatActivity {

    Ajax mo;
    Globalvars globalvars;
    ArrayList<DownloadedDiscountData> itemlist;
    ArrayList<DownloadedDiscountData> filteredList;
    TransactionDiscountNotTeleAdapter adapter;
    ProgressDialog pd;
    DownloadedDiscountData itemrow;
    ListView lv_discount;
    Button btn_discount_submit;
    Msgbox msgbox;
    ProgressDialog pd1;
    String ticket;
    Handler handler;
    Runnable r;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_view_discount_not_tele);
        lv_discount = findViewById(R.id.lv_not_tele_discount);
        Intent intent = getIntent();
        ticket = intent.getExtras().getString("ticket_id");
        globalvars = new Globalvars(this, this);
        msgbox = new Msgbox(this);
        getDiscountType();

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
                    View dialogView = LayoutInflater.from(TransactionViewDiscountNotTele.this).inflate(R.layout.my_dialog_session_timedout, viewGroup, false);
                    TextView tv_dialog_message = dialogView.findViewById(R.id.tv_dialog_message);
                    tv_dialog_message.setText("Your session has timed out. Please login again.");
                    //Now we need an AlertDialog.Builder object
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(TransactionViewDiscountNotTele.this);
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
                            Intent intent = new Intent(TransactionViewDiscountNotTele.this, Login.class);
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

    public void getDiscountType() {
        globalvars.set("ImagePath", "naaysulod");
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
                JSONArray thedata = null;
                int pending = 0;
                int submit = 0;
                ArrayList<HashMap<String, String>> detailss = new ArrayList<HashMap<String, String>>();
                try {
                    thedata = new JSONArray(data);
                    String id, desc, name, id_num, rider_status, cancelled_status, submit_status, image_path;

                    if (thedata.length() > 0) {
                        for (int a = 0; a < thedata.length(); a++) {
                            JSONArray row = thedata.getJSONArray(a);
                            id = row.getString(0);
                            desc = row.getString(1);
                            name = row.getString(2);
                            id_num = row.getString(3);
                            rider_status = row.getString(4);
                            cancelled_status = row.getString(5);
                            submit_status = row.getString(6);
                            image_path = row.getString(7);
                            if(rider_status.equalsIgnoreCase("0"))
                            {
                                pending++;
                            }
                            if(submit_status.equalsIgnoreCase("1"))
                            {
                                submit++;
                            }
                            if(rider_status.equalsIgnoreCase("1") && cancelled_status.equalsIgnoreCase("0") && image_path.equalsIgnoreCase(""))
                            {
                                globalvars.set("ImagePath", "walaysulod");
                            }

                            itemrow = new DownloadedDiscountData(id, desc, name, id_num, rider_status, cancelled_status, submit_status, image_path);
                            itemlist.add(itemrow);
                        }
                        adapter = new TransactionDiscountNotTeleAdapter(TransactionViewDiscountNotTele.this, R.layout.transaction_view_discount_not_tele_content, itemlist, getIntent().getExtras().getString("ticket_id"));
                        lv_discount.setAdapter(adapter);
                        pd.dismiss();
                    } else {
                        pd.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    if(pending > 0)
                    {
                        globalvars.set("DiscountPendingStatus", "naa");
                    }
                    else
                    {
                        globalvars.set("DiscountPendingStatus", "wala");
                    }
                    if(submit > 0)
                    {
                        btn_discount_submit.setVisibility(View.GONE);
                    }

                }
            }
        });
        String a = getIntent().getExtras().getString("ticket_id");
        String b = getIntent().getExtras().getString("ticket_id");
        mo.adddata("ticket_id", getIntent().getExtras().getString("ticket_id"));
        mo.execute(Globalvars.online_link + "get_discount_type");
    }

}
