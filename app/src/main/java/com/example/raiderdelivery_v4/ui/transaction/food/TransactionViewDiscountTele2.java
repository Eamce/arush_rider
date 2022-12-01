package com.example.raiderdelivery_v4.ui.transaction.food;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class TransactionViewDiscountTele2 extends AppCompatActivity {
    Ajax mo;
    Globalvars globalvars;
    ArrayList<DownloadedDiscountData> itemlist;
    ArrayList<DownloadedDiscountData> filteredList;
    TransactionDiscountTeleAdapter adapter;
    ProgressDialog pd;
    DownloadedDiscountData itemrow;
    ListView lv_discount;
    Button btn_discount_submit, btn_discount_cancel;
    LinearLayout ll_discount_submit, ll_discount_listview, ll_discounts_noresults;
    ImageView iv_discounts_noresult;
    TextView tv_discounts_noresults;
    Msgbox msgbox;
    ProgressDialog pd1;
    String ticket, discount;
    Handler handler;
    Runnable r;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_view_discount_tele2);
        lv_discount = findViewById(R.id.lv_discount);
        btn_discount_submit = findViewById(R.id.btn_discount_submit);
        ll_discount_submit = findViewById(R.id.ll_discount_submit);
        ll_discount_listview = findViewById(R.id.ll_discount_listview);
        ll_discounts_noresults = findViewById(R.id.ll_discounts_noresults);
        iv_discounts_noresult = findViewById(R.id.iv_discounts_noresult);
        tv_discounts_noresults = findViewById(R.id.tv_discounts_noresults);
        btn_discount_cancel = findViewById(R.id.btn_discount_cancel);

        Intent intent = getIntent();
        ticket = intent.getExtras().getString("ticket_id");
        discount = intent.getExtras().getString("discount");
        globalvars = new Globalvars(this, this);
        msgbox = new Msgbox(this);
        getDiscountType();

        btn_discount_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TransactionViewDiscountTele2.this, TransactionActivity.class);
                startActivity(i);
            }
        });

        btn_discount_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmSubmit();
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
                View dialogView = LayoutInflater.from(TransactionViewDiscountTele2.this).inflate(R.layout.my_dialog_session_timedout, viewGroup, false);
                TextView tv_dialog_message = dialogView.findViewById(R.id.tv_dialog_message);
                tv_dialog_message.setText("Your session has timed out. Please login again.");
                //Now we need an AlertDialog.Builder object
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(TransactionViewDiscountTele2.this);
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
                        Intent intent = new Intent(TransactionViewDiscountTele2.this, Login.class);
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

    public void OnClickCamera(View v) {
        final Button n = (Button) v;
        final String array_string[] = n.getTag().toString().split("\\|");
        Log.e("Message: ", "Camera clicked!");
        Log.e("0: ", array_string[0]);
        Log.e("1: ", array_string[1]);
        Intent i = new Intent(TransactionViewDiscountTele2.this, TransactionViewDiscountTele.class);
        i.putExtra("ticket_id", ticket);
        i.putExtra("ID#", array_string[0]);
        i.putExtra("Discount_type", array_string[1]);
        startActivity(i);
    }

    public void OnClickConfirm(View v) {
        String str_clicked = "CONFIRM";
        final Button n = (Button) v;
        final String array_string[] = n.getTag().toString().split("\\|");
        confirmUpload(array_string[0], array_string[1], str_clicked);
    }

    public void OnClickCancel(View v) {
        String str_clicked = "CANCEL";
        final Button n = (Button) v;
        final String array_string[] = n.getTag().toString().split("\\|");
        confirmUpload(array_string[0], array_string[1], str_clicked);
    }

    private void confirmUpload(final String str_discount_id, final String desc, final String str_clicked)
    {
        msgbox.showyesno("Confirmation", "Are you sure you want to " + str_clicked + "?");
        msgbox.setMsgboxListener(new Msgbox.MsgboxListener() {
            @Override
            public void onyes() {
                if(str_clicked.equalsIgnoreCase("CONFIRM")){
                    UpdateComfirmedStatus(str_discount_id, desc);
                }else{
                    UpdateCancelledStatus(str_discount_id, desc);
                }
            }

            @Override
            public void onno() {
            }
        });
    }

    private void confirmSubmit()
    {
        msgbox.showyesno("Confirmation", "Are you sure you want to SUBMIT?");
        msgbox.setMsgboxListener(new Msgbox.MsgboxListener() {
            @Override
            public void onyes() {
                if(globalvars.get("DiscountPendingStatus").equalsIgnoreCase("wala")) {
                    if(globalvars.get("ImagePath").equalsIgnoreCase("walaysulod"))
                    {
                        Toast toast = Toast.makeText(getBaseContext(), "Please upload image before you submit.", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 80);
                        toast.show();
                    }
                    else
                    {
                        SubmitDiscount();
                    }
                }
                else
                {
                    Toast toast = Toast.makeText(getBaseContext(), "Please confirm/cancel all discounts.", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 80);
                    toast.show();
                }
            }

            @Override
            public void onno() {
            }
        });
    }

    public void SubmitDiscount() {

        pd1 = new ProgressDialog(this);
        pd1.setMessage("Please wait...");
        pd1.show();
        pd1.setCancelable(false);
        pd1.setCanceledOnTouchOutside(false);
        mo = new Ajax();
        mo.setCustomObjectListener(new Ajax.MyCustomObjectListener() {
            @Override
            public void onerror() {
                pd1.dismiss();
                Toast toast = Toast.makeText(getBaseContext(), "Unable to connect. Please check your connection.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 80);
                toast.show();
            }

            @Override
            public void onsuccess(String data) {

                pd1.dismiss();
                //refresh_transaction_listview2(column_name);
                ViewGroup viewGroup = findViewById(android.R.id.content);

                //then we will inflate the custom alert dialog xml that we created
                View dialogView = LayoutInflater.from(TransactionViewDiscountTele2.this).inflate(R.layout.my_dialog, viewGroup, false);
                TextView tv_dialog_message = dialogView.findViewById(R.id.tv_dialog_message);
                tv_dialog_message.setText("Discounts has been successfully updated.");
                //Now we need an AlertDialog.Builder object
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(TransactionViewDiscountTele2.this);
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
                        Intent i = new Intent(TransactionViewDiscountTele2.this, TransactionActivity.class);
                        //i.putExtra("ticket_id", ticket);
                        startActivity(i);
                    }
                });

            }
        });

        mo.adddata("ticket_id", ticket);
        //mo.adddata("discount", discount);
        mo.execute(Globalvars.online_link + "submit_discount");
    }

    public void UpdateCancelledStatus(String str_discount_id, String desc) {

        pd1 = new ProgressDialog(this);
        pd1.setMessage("Please wait...");
        pd1.show();
        pd1.setCancelable(false);
        pd1.setCanceledOnTouchOutside(false);
        mo = new Ajax();
        mo.setCustomObjectListener(new Ajax.MyCustomObjectListener() {
            @Override
            public void onerror() {
                pd1.dismiss();
                Toast toast = Toast.makeText(getBaseContext(), "Unable to connect. Please check your connection.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 80);
                toast.show();
            }

            @Override
            public void onsuccess(String data) {
                //refresh_transaction_listview("cancelled");

                pd1.dismiss();
                getDiscountType();
                //refresh_transaction_listview2(column_name);
                ViewGroup viewGroup = findViewById(android.R.id.content);

                //then we will inflate the custom alert dialog xml that we created
                View dialogView = LayoutInflater.from(TransactionViewDiscountTele2.this).inflate(R.layout.my_dialog, viewGroup, false);
                TextView tv_dialog_message = dialogView.findViewById(R.id.tv_dialog_message);
                tv_dialog_message.setText("Successfully tag as cancelled.");
                //Now we need an AlertDialog.Builder object
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(TransactionViewDiscountTele2.this);
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
                    }
                });

            }
        });

        String a = ticket;
        String b = desc;
        String c = str_discount_id;

        mo.adddata("ticket_id", ticket);
        mo.adddata("discount_desc", desc);
        mo.adddata("customer_discount_id", str_discount_id);
        mo.execute(Globalvars.online_link + "update_discount_cancelled_status");
    }

    public void UpdateComfirmedStatus(String str_discount_id, String desc) {

        pd1 = new ProgressDialog(this);
        pd1.setMessage("Please wait...");
        pd1.show();
        pd1.setCancelable(false);
        pd1.setCanceledOnTouchOutside(false);
        mo = new Ajax();
        mo.setCustomObjectListener(new Ajax.MyCustomObjectListener() {
            @Override
            public void onerror() {
                pd1.dismiss();
                Toast toast = Toast.makeText(getBaseContext(), "Unable to connect. Please check your connection.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 80);
                toast.show();
            }

            @Override
            public void onsuccess(String data) {
                //refresh_transaction_listview("cancelled");

                pd1.dismiss();
                getDiscountType();

                ViewGroup viewGroup = findViewById(android.R.id.content);

                //then we will inflate the custom alert dialog xml that we created
                View dialogView = LayoutInflater.from(TransactionViewDiscountTele2.this).inflate(R.layout.my_dialog, viewGroup, false);
                TextView tv_dialog_message = dialogView.findViewById(R.id.tv_dialog_message);
                tv_dialog_message.setText("Successfully tag as confirmed.");
                //Now we need an AlertDialog.Builder object
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(TransactionViewDiscountTele2.this);
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
                    }
                });

            }
        });

        mo.adddata("ticket_id", ticket);
        mo.adddata("discount_desc", desc);
        mo.adddata("customer_discount_id", str_discount_id);
        mo.execute(Globalvars.online_link + "update_confirmed_status");
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
                        adapter = new TransactionDiscountTeleAdapter(TransactionViewDiscountTele2.this, R.layout.transaction_view_discount_tele2_content, itemlist, getIntent().getExtras().getString("ticket_id"));
                        lv_discount.setAdapter(adapter);
                        pd.dismiss();
                    } else {
                        hidewidgets();
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
                        btn_discount_cancel.setVisibility(View.VISIBLE);
                    }

                }
            }
        });
        String a = getIntent().getExtras().getString("ticket_id");
        String b = getIntent().getExtras().getString("ticket_id");
        mo.adddata("ticket_id", getIntent().getExtras().getString("ticket_id"));
        mo.execute(Globalvars.online_link + "get_discount_type");
    }

    public void hidewidgets() {
        ll_discounts_noresults.setVisibility(View.VISIBLE);
        iv_discounts_noresult.setVisibility(View.VISIBLE);
        tv_discounts_noresults.setVisibility(View.VISIBLE);
        btn_discount_cancel.setVisibility(View.VISIBLE);
        ll_discount_submit.setVisibility(View.VISIBLE);
        btn_discount_submit.setVisibility(View.GONE);
        lv_discount.setVisibility(View.GONE);
        btn_discount_submit.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed(); commented this line in order to disable back press
    }
}
