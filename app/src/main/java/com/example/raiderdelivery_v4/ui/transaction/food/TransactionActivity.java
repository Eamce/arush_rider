package com.example.raiderdelivery_v4.ui.transaction.food;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.raiderdelivery_v4.ui.chatbox.ChatboxMessages;
import com.example.raiderdelivery_v4.ui.global.Ajax;
import com.example.raiderdelivery_v4.ui.global.Globalvars;
import com.example.raiderdelivery_v4.R;
import com.example.raiderdelivery_v4.ui.global.Msgbox;
import com.example.raiderdelivery_v4.ui.login.Login;

import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.HashMap;

public class TransactionActivity extends AppCompatActivity {
    Ajax mo;
    Globalvars globalvars;
    ArrayList<DownloadedTransactionData> itemlist;
    ArrayList<DownloadedTransactionData> filteredList;
    TransactionAdapter adapter;
    ProgressDialog pd1;
    ListView lv_trans_cus;
    TextView tv_transaction_no_of_customer, tv_noresults;
    LinearLayout ll_noresults;
    ImageView iv_noresults;
    public static String activity_name = "TransactionActivity";
    int no_of_customer = 0;
    DownloadedTransactionData itemrow;
    ProgressDialog pd;
    Boolean web_is_empty = false, mobile_is_empty = false;
    String str_ticket_id, activity;
    String tag_status = "";
    Msgbox msgbox;
    Context context = this;
    Handler handler;
    Runnable r;
    Button btn_deliver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction);
        lv_trans_cus = findViewById(R.id.lv_trans_customer);
        tv_transaction_no_of_customer = findViewById(R.id.tv_transaction_no_of_customer);
        tv_noresults = findViewById(R.id.tv_trans_noresults);
        ll_noresults = findViewById(R.id.ll_trans_noresults);
        iv_noresults = findViewById(R.id.iv_trans_noresult);
        globalvars = new Globalvars(this, this);
//        btn_deliver = (Button)findViewById(R.id.btn_delivered);
        this.setTitle("Transactions(Food/Electronics & Appliances   )");

        msgbox = new Msgbox(context);
//        Intent intent = getIntent();
//        str_ticket_id = intent.getExtras().getString("ticket_id");
//        activity = intent.getExtras().getString("activity");

        get_orders();

        handler = new Handler();
        r = new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                if(!isFinishing())
                {
                    //Logout user...
                    globalvars.logout();
                    //before inflating the custom alert dialog layout, we will get the current activity viewgroup
                    ViewGroup viewGroup = findViewById(android.R.id.content);
                    //then we will inflate the custom alert dialog xml that we created
                    View dialogView = LayoutInflater.from(TransactionActivity.this).inflate(R.layout.my_dialog_session_timedout, viewGroup, false);
                    TextView tv_dialog_message = dialogView.findViewById(R.id.tv_dialog_message);
                    tv_dialog_message.setText("Your session has timed out. Please login again.");
                    //Now we need an AlertDialog.Builder object
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(TransactionActivity.this);
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
                            Intent intent = new Intent(TransactionActivity.this, Login.class);
                            startActivity(intent);
                        }
                    });
                }
            }
        };
        startHandler();

        btn_deliver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
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

    public void get_orders() {

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
                ArrayList<HashMap<String, String>> detailss = new ArrayList<HashMap<String, String>>();
                try {
                    thedata = new JSONArray(data);
                    String id, cus_id, ticket_no, fname, lname, fullname, barangay, town, address, order, charge, totalamount, discount, view_stat, ontransit, delivered, cancelled,remitted, contact_no, num_pack, del_charge, landmark, order_from, created_at, tendered_amount, change, change_bu, main_rider_stat, count_rider, instructions, submit_status, payment_platform;
                    int a1 = thedata.length();
                    int b1 = thedata.length();
                    if (thedata.length() > 0) {
                        for (int a = 0; a < thedata.length(); a++) {
                            JSONArray row = thedata.getJSONArray(a);
                            id = row.getString(0);
                            cus_id = row.getString(1);
                            fname = row.getString(2);
                            lname = row.getString(3);
                            fullname = fname + " " + lname;
                            barangay = row.getString(4);
                            town = row.getString(5);
                            order = row.getString(6);
                            totalamount = row.getString(7);
                            discount = row.getString(8);
                            charge = row.getString(9);
                            view_stat = row.getString(10);
                            ontransit = row.getString(11);
                            delivered = row.getString(12);
                            cancelled = row.getString(13);
                            remitted = row.getString(14);
                            ticket_no = row.getString(15);
                            contact_no = row.getString(16);
                            num_pack = row.getString(17);
                            landmark = row.getString(18);
                            order_from = row.getString(19);
                            created_at = row.getString(20);
                            address = barangay + ", " + town + "(" + landmark + ")";
                            tendered_amount = row.getString(21);
                            change = row.getString(22);
                            change_bu = row.getString(23);
                            main_rider_stat = row.getString(24);
                            count_rider = row.getString(25);
                            instructions = row.getString(26);
                            submit_status = row.getString(27);
                            payment_platform = row.getString(28);
                            System.out.println("PAYMENT PLATFORM: " +payment_platform);
                            if(row.getString(27).equalsIgnoreCase("null"))
                            {
                                submit_status = "1";
                            }
                            else
                            {
                                submit_status = row.getString(27);
                            }

                            if(payment_platform.equalsIgnoreCase("null"))
                            {
                                payment_platform = "Cash on Delivery";
                            }

                            itemrow = new DownloadedTransactionData(id, cus_id, fullname, address, order, charge, totalamount, discount, view_stat, ontransit, delivered, cancelled, ticket_no, contact_no, num_pack, order_from, created_at, tendered_amount, change, change_bu, main_rider_stat, count_rider, instructions, submit_status, payment_platform);
                            itemlist.add(itemrow);
                        }
                        adapter = new TransactionAdapter(TransactionActivity.this, R.layout.transaction_content, itemlist);
                        lv_trans_cus.setAdapter(adapter);
                        pd.dismiss();
                        no_of_customer = thedata.length();

                    } else {
                        pd.dismiss();
                        hidewidgets();
                        web_is_empty = true;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    //get_items_from_mobile();
                    tv_transaction_no_of_customer.setText("Total no. of Customers: " + no_of_customer);
                }
            }
        });
        mo.adddata("r_id_num", globalvars.get("r_id_num"));
        //mo.adddata("bunit_code", globalvars.get("bunit_code"));
        mo.execute(Globalvars.online_link + "get_customer_orders");
    }

    public void Tag_as_cancelled(String id) {
        itemlist = new ArrayList<>();
        //final ListView listView = findViewById(R.id.lv_trans_customer);
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
                Toast toast = Toast.makeText(getApplicationContext(), "Unable to connect. Please check your connection.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 80);
                toast.show();
            }
            @Override
            public void onsuccess(String data) {
                refresh_transaction_listview("cancelled");
            }
        });

        mo.adddata("update_cancelled_status", id);
        mo.adddata("r_id_num", globalvars.get("r_id_num"));
        mo.execute(Globalvars.online_link + "update_cancelled_status");
    }

    public void Tag_as_delivered(String id, String payment_platform) {

        itemlist = new ArrayList<>();
        //final ListView listView = findViewById(R.id.lv_trans_customer);
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
                Toast toast = Toast.makeText(getApplicationContext(), "Unable to connect. Please check your connection.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 80);
                toast.show();
            }
            @Override
            public void onsuccess(String data) {
                //pd1.dismiss();
                refresh_transaction_listview("delivered");
            }
        });
        mo.adddata("update_delivered_status", id);
        mo.adddata("payment_platform", payment_platform);
        mo.adddata("r_id_num", globalvars.get("r_id_num"));
        mo.execute(Globalvars.online_link + "update_delivery_status");
    }

    public void refresh_transaction_listview(String column_name) {

        itemlist = new ArrayList<>();
        //final ListView listView = findViewById(R.id.lv_customer);
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
                lv_trans_cus.setAdapter(null);
                JSONArray thedata;
                ArrayList<HashMap<String, String>> detailss = new ArrayList<HashMap<String, String>>();
                try {
                    thedata = new JSONArray(data);
                    String id, cus_id, ticket_no, fname, lname, fullname, barangay, town, address, order, charge, totalamount, discount, view_stat, ontransit, delivered, cancelled, remitted, contact_no, num_pack, order_from, created_at, tendered_amount, change, change_bu, main_rider_stat, count_rider, instructions, submit_status, payment_platform;
                    if (thedata.length() > 0) {
                        for (int a = 0; a < thedata.length(); a++) {
                            JSONArray row = thedata.getJSONArray(a);
                            id = row.getString(0);
                            cus_id = row.getString(1);
                            fname = row.getString(2);
                            lname = row.getString(3);
                            fullname = fname + " " + lname;
                            barangay = row.getString(4);
                            town = row.getString(5);
                            address = barangay + ", " + town;
                            order = row.getString(6);
                            totalamount = row.getString(7);
                            discount = row.getString(8);
                            charge = row.getString(9);
                            view_stat = row.getString(10);
                            ontransit = row.getString(11);
                            delivered = row.getString(12);
                            cancelled = row.getString(13);
                            remitted = row.getString(14);
                            ticket_no = row.getString(15);
                            contact_no = row.getString(16);
                            num_pack = row.getString(17);
                            order_from = row.getString(19);
                            created_at = row.getString(20);
                            tendered_amount = row.getString(21);
                            change = row.getString(22);
                            change_bu = row.getString(23);
                            main_rider_stat = row.getString(24);
                            count_rider = row.getString(25);
                            instructions = row.getString(26);
                            payment_platform = row.getString(28);
                            if(row.getString(27).equalsIgnoreCase("null"))
                            {
                                submit_status = "1";
                            }
                            else
                            {
                                submit_status = row.getString(27);
                            }

//                            if(payment_platform.equalsIgnoreCase("null"))
//                            {
//                                payment_platform = "Cash on Delivery";
//                            }

                            itemrow = new DownloadedTransactionData(id, cus_id, fullname, address, order, charge, totalamount, discount, view_stat, ontransit, delivered, cancelled, ticket_no, contact_no, num_pack, order_from, created_at, tendered_amount, change, change_bu, main_rider_stat, count_rider, instructions, submit_status, payment_platform);
                            itemlist.add(itemrow);
                        }
                        no_of_customer = no_of_customer + thedata.length();
                        adapter = new TransactionAdapter(TransactionActivity.this, R.layout.transaction_content, itemlist);
                        lv_trans_cus.setAdapter(adapter);
                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    pd1.dismiss();
                    //refresh_transaction_listview2(column_name);
                    ViewGroup viewGroup = findViewById(android.R.id.content);

                    //then we will inflate the custom alert dialog xml that we created
                    View dialogView = LayoutInflater.from(TransactionActivity.this).inflate(R.layout.my_dialog, viewGroup, false);
                    TextView tv_dialog_message = dialogView.findViewById(R.id.tv_dialog_message);
                    tv_dialog_message.setText("Successfully tag as " + tag_status + ".");
                    //Now we need an AlertDialog.Builder object
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(TransactionActivity.this);
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
                    tv_transaction_no_of_customer.setText("Total no. of Customer: " + no_of_customer);
                }
            }
        });

        mo.adddata("r_id_num", globalvars.get("r_id_num"));
        //mo.adddata("bunit_code", globalvars.get("bunit_code"));
        mo.execute(Globalvars.online_link + "get_customer_orders");
    }

    public void hidewidgets() {
        ll_noresults.setVisibility(View.VISIBLE);
        iv_noresults.setVisibility(View.VISIBLE);
        tv_noresults.setVisibility(View.VISIBLE);
        lv_trans_cus.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        MenuItem mSearch = menu.findItem(R.id.action_search);
        SearchView mSearchView = (SearchView) mSearch.getActionView();
        mSearchView.setQueryHint("Search");

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //adapter.getFilter().filter(newText);
                filter(newText);
                return true;
            }

            private void filter(String text) {
                filteredList = new ArrayList<DownloadedTransactionData>();
                if (text.length() > 0) {
                    for (DownloadedTransactionData la : itemlist) {
                        if (la.getName().toLowerCase().trim().contains(text.toLowerCase().trim())) {
                            filteredList.add(la);
                        }
                    }
                } else {
                    filteredList.addAll(itemlist);
                }
                adapter.setData(filteredList);
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    public void OnClickDelivered(View v) {
        final Button n = (Button) v;
        final String array_string[] = n.getTag().toString().split("\\|");
        //final String id = n.getTag().toString();
        String a = array_string[0]; //Ticket No...
        String b = array_string[1]; //Submit Status...
        String c = array_string[2]; //Payment Platform...
//        Log.e("TICKET NO: ",array_string[0]);
//        Log.e("SUBMIT STATUS: ",array_string[1]);
//        Log.e("PAYMENT PLATFORM: ",array_string[2]);
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(TransactionActivity.this);
        builderSingle.setCancelable(false);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(TransactionActivity.this, android.R.layout.simple_list_item_1);
        arrayAdapter.add("Tag as Delivered");
        arrayAdapter.add("Tag as Cancelled");
        arrayAdapter.add("Cancel");


        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String strOption = arrayAdapter.getItem(which);
                if (strOption.equalsIgnoreCase("Tag as Delivered"))
                {
                    tag_status = "delivered";
//                    if (array_string[1].equalsIgnoreCase("1"))
//                    {
                          confirmDeliver(array_string[0], array_string[2]);
//                    }
//                    else
//                    {
//                        Toast toast = Toast.makeText(getApplicationContext(), "Please submit discount before tagging.", Toast.LENGTH_LONG);
//                        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 80);
//                        toast.show();
//                    }
                }
                else if (strOption.equalsIgnoreCase("Tag as Cancelled"))
                {
                    tag_status = "cancelled";
                    if (array_string[1].equalsIgnoreCase("1"))
                    {
                          confirmCancel(array_string[0]);
//                        Tag_as_cancelled(array_string[0]);
                    }
                    else
                    {
                        Toast toast = Toast.makeText(getApplicationContext(), "Please submit discount before tagging.", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 80);
                        toast.show();
                    }
                }
                else if (strOption.equalsIgnoreCase("Cancel"))
                {
                    //confirmViewLeveling(emp_tag[1],emp_name.getText().toString());
                }
                else
                {

                }
            }
        });
        builderSingle.show();
    }

    public void OnClickView(View v) {
        final Button n = (Button) v;
        final String array_string[] = n.getTag().toString().split("\\|");

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(TransactionActivity.this);
        //builderSingle.setIcon(R.drawable.ic_access_time_black_24dp);
        builderSingle.setCancelable(false);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(TransactionActivity.this, android.R.layout.simple_list_item_1); //simple_list_item_1...
        arrayAdapter.add("View Customer Details");
        arrayAdapter.add("View Items");
        arrayAdapter.add("View Time Frame");
        arrayAdapter.add("View Discount");
        arrayAdapter.add("Chat");
        arrayAdapter.add("Cancel");


        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String strOption = arrayAdapter.getItem(which);
                if (strOption.equalsIgnoreCase("View Items")) {
                    Intent i = new Intent(TransactionActivity.this, TransactionViewItems.class);
                    i.putExtra("ticket_id", array_string[0]);
                    i.putExtra("customer_name", array_string[1]);
                    i.putExtra("delivery_charge", array_string[2]);
                    i.putExtra("order_from",array_string[4]);
                    globalvars.set("addons_ticket", array_string[0]);
                    startActivity(i);
                } else if (strOption.equalsIgnoreCase("View Time Frame")) {
                    Intent i = new Intent(TransactionActivity.this, TransactionViewTimeFrame2.class);
                    i.putExtra("ticket_id", array_string[0]);
                    i.putExtra("customer_name", array_string[1]);
                    i.putExtra("activity", activity_name);
                    startActivity(i);
                } else if (strOption.equalsIgnoreCase("View Customer Details")) {
                    Intent i = new Intent(TransactionActivity.this, TransactionViewCustomerDetails.class);
                    i.putExtra("ticket_id", array_string[0]);
                    i.putExtra("activity", activity_name);
                    i.putExtra("cus_id", array_string[3]);
                    i.putExtra("order_from", array_string[4]);
                    startActivity(i);
                } else if (strOption.equalsIgnoreCase("View Discount")) {
                    if(array_string[4].equalsIgnoreCase("1")){
                        Intent i = new Intent(TransactionActivity.this, TransactionViewDiscountTele2.class);
                        i.putExtra("ticket_id", array_string[0]);
                        i.putExtra("activity", activity_name);
                        i.putExtra("cus_id", array_string[3]);
                        i.putExtra("order_from", array_string[4]);
                        //i.putExtra("discount", array_string[5]);
                        startActivity(i);
                        //finish();
                    }else{
                        Intent i = new Intent(TransactionActivity.this, TransactionViewDiscountNotTele.class);
                        i.putExtra("ticket_id", array_string[0]);
                        i.putExtra("activity", activity_name);
                        i.putExtra("cus_id", array_string[3]);
                        i.putExtra("order_from", array_string[4]);
                        //i.putExtra("discount", array_string[5]);
                        startActivity(i);
                    }

                } else if (strOption.equalsIgnoreCase("Cancel")) {
                    //action if cancelled...
                }else if (strOption.equalsIgnoreCase("Chat")) {
                    Intent i = new Intent(TransactionActivity.this, ChatboxMessages.class);
                    i.putExtra("ticket_id", array_string[0]);
//                    Log.e("TICKET ID: ",array_string[0]);
                    i.putExtra("user_name", array_string[5]);
                    i.putExtra("from", "transaction");
                    globalvars.set("user_type", "Customer");
                    startActivity(i);
                }else {

                }
            }
        });
        builderSingle.show();

    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed(); commented this line in order to disable back press
    }

    private void confirmCancel(final String id)
    {
        msgbox.showyesno("Confirmation", "Are you sure you want to tag as cancelled?");
        msgbox.setMsgboxListener(new Msgbox.MsgboxListener() {
            @Override
            public void onyes() {
                Tag_as_cancelled(id);
            }

            @Override
            public void onno() {
            }
        });
    }

    private void confirmDeliver(final String id, final String payment_platform)
    {
        msgbox.showyesno("Confirmation", "Are you sure you want to tag as delivered?");
        msgbox.setMsgboxListener(new Msgbox.MsgboxListener() {
            @Override
            public void onyes() {
                Tag_as_delivered(id, payment_platform);
            }

            @Override
            public void onno() {
            }
        });
    }

}
