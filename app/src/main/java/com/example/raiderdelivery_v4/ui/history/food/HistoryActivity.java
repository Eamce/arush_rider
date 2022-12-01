package com.example.raiderdelivery_v4.ui.history.food;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.example.raiderdelivery_v4.ui.transaction.food.TransactionActivity;
import com.example.raiderdelivery_v4.ui.transaction.food.TransactionViewCustomerDetails;
import com.example.raiderdelivery_v4.ui.transaction.food.TransactionViewItems;
import com.example.raiderdelivery_v4.ui.transaction.food.TransactionViewTimeFrame2;

import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    ListView lv_history;
    TextView tv_note, tv_noresults;
    LinearLayout ll_noresults;
    ImageView iv_noresult;
    Ajax mo;
    ArrayList<DownloadedHistoryData> itemlist;
    ArrayList<DownloadedHistoryData> filteredList;
    HistoryAdapter adapter;
    ProgressDialog pd1;
    Globalvars globalvars;
    public static String activity_name = "HistoryActivity";
    int no_of_customer = 0;
    DownloadedHistoryData itemrow;
    ProgressDialog pd;
    Boolean web_is_empty = false, mobile_is_empty = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);
        lv_history = findViewById(R.id.lv_history);
        tv_note = findViewById(R.id.tv_history_no_of_customer);
        tv_noresults = findViewById(R.id.tv_noresults);
        ll_noresults = findViewById(R.id.ll_noresults);
        iv_noresult = findViewById(R.id.iv_noresult);
        globalvars = new Globalvars((Context)this,(Activity)this);

        this.setTitle("History(Food/Electronics & Appliances)");
        get_history_orders();
    }
    public void get_history_orders(){
        itemlist = new ArrayList<>();
        final ListView listView = findViewById(R.id.lv_history);
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
                    String id, cus_id, ticket_no, fname, lname, fullname, barangay, town, address, order, charge, totalamount, discount, view_stat, ontransit, delivered, cancelled, remitted, contact_no, num_pack, del_charge, landmark, created_at, order_from, tendered_amount, change, change_bu, count_rider, main_rider_stat, payment_platform;
                    if (thedata.length() > 0)
                    {
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
                            address = barangay + ", " + town + "("+landmark+")";
                            created_at = row.getString(19);
                            order_from = row.getString(20);
                            tendered_amount = row.getString(21);
                            change = row.getString(22);
                            change_bu = row.getString(23);
                            count_rider = row.getString(24);
                            main_rider_stat = row.getString(25);
                            payment_platform = row.getString(26);

                            if(payment_platform.equalsIgnoreCase("null"))
                            {
                                payment_platform = "Cash on Delivery";
                            }

                            itemrow = new DownloadedHistoryData(id, cus_id, fullname, address, order, charge, totalamount, discount, view_stat,ontransit,delivered, cancelled, ticket_no, contact_no, num_pack, created_at, order_from, tendered_amount, change, change_bu, count_rider, main_rider_stat, payment_platform);
                            itemlist.add(itemrow);
                        }
                        adapter = new HistoryAdapter(HistoryActivity.this,R.layout.history_content, itemlist);
                        listView.setAdapter(adapter);
                        no_of_customer = thedata.length();
                        pd.dismiss();
                    }
                    else
                    {
                        pd.dismiss();
                        hidewidgets();
                        //web_is_empty = true;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    //get_history_from_mobile();
                    tv_note.setText("Total no. of customers: " + no_of_customer);
                }
            }
        });
        mo.adddata("r_id_num",globalvars.get("r_id_num"));
        mo.execute(Globalvars.online_link + "get_history_items");
    }

//    public void get_history_from_mobile(){
////        mo = new Ajax();
////        mo.setCustomObjectListener(new Ajax.MyCustomObjectListener() {
////            @Override
////            public void onerror() {
////                pd.dismiss();
////                Toast toast = Toast.makeText(getApplicationContext(), "Unable to connect. Please check your connection.", Toast.LENGTH_LONG);
////                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 80);
////                toast.show();
////            }
////
////            @Override
////            public void onsuccess(String data) {
////                JSONArray thedata;
////                try {
////                    thedata = new JSONArray(data);
////                    String id, cus_id, ticket_no, fname, lname, fullname, barangay, town, address, order, charge, totalamount, view_stat, ontransit, delivered, cancelled, remitted, contact_no, num_pack, del_charge, landmark, created_at, order_from, tendered_amount, change, count_rider, main_rider_stat;
////                    if (thedata.length() > 0)
////                    {
////                        for (int a = 0; a < thedata.length(); a++) {
////                            JSONArray row = thedata.getJSONArray(a);
////                            id = row.getString(0);
////                            cus_id = row.getString(1);
////                            fname = row.getString(2);
////                            lname = row.getString(3);
////                            fullname = fname + " " + lname;
////                            barangay = row.getString(4);
////                            town = row.getString(5);
////                            order = row.getString(6);
////                            totalamount = row.getString(7);
////                            charge = row.getString(8);
////                            view_stat = row.getString(9);
////                            ontransit = row.getString(10);
////                            delivered = row.getString(11);
////                            cancelled = row.getString(12);
////                            remitted = row.getString(13);
////                            ticket_no = row.getString(14);
////                            contact_no = row.getString(15);
////                            num_pack = row.getString(16);
////                            landmark = row.getString(17);
////                            address = barangay + ", " + town + "("+landmark+")";
////                            created_at = row.getString(18);
////                            order_from = row.getString(19);
////                            tendered_amount = row.getString(20);
////                            change = row.getString(21);
////                            count_rider = row.getString(22);
////                            main_rider_stat = row.getString(23);
////
////                            itemrow = new DownloadedHistoryData(id, cus_id, fullname, address, order, charge, totalamount, view_stat,ontransit,delivered, cancelled, ticket_no, contact_no, num_pack, created_at, order_from, tendered_amount, change, count_rider, main_rider_stat);
////                            itemlist.add(itemrow);
////                        }
////                        adapter = new HistoryAdapter(HistoryActivity.this,R.layout.history_content, itemlist);
////                        lv_history.setAdapter(adapter);
////                        no_of_customer = no_of_customer + thedata.length();
////                        pd.dismiss();
////                    }
////                    else
////                    {
////                        pd.dismiss();
////                        if (web_is_empty) {
////                            hidewidgets();
////                        }else {
////                            adapter = new HistoryAdapter(HistoryActivity.this, R.layout.history_content, itemlist);
////                            lv_history.setAdapter(adapter);
////                        }
////                    }
////                } catch (JSONException e) {
////                    e.printStackTrace();
////                } finally {
////                    tv_note.setText("Total no. of customers: " + no_of_customer);
////                }
////            }
////        });
////        mo.adddata("r_id_num",globalvars.get("r_id_num"));
////        mo.adddata("bunit_code", globalvars.get("bunit_code"));
////        mo.execute(Globalvars.offline_link + "get_history_items_from_mobile");
////    }

    public void hidewidgets(){
        ll_noresults.setVisibility(View.VISIBLE);
        iv_noresult.setVisibility(View.VISIBLE);
        tv_noresults.setVisibility(View.VISIBLE);
        lv_history.setVisibility(View.GONE);
    }

    public void HistoryOnClickView(View v){
        final Button n = (Button) v;
        final String array_string[] = n.getTag().toString().split("\\|");

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(HistoryActivity.this);
        builderSingle.setCancelable(false);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(HistoryActivity.this, android.R.layout.simple_list_item_1);
        arrayAdapter.add("View Customer Details");
        arrayAdapter.add("View Items");
        arrayAdapter.add("View Time Frame");
        //arrayAdapter.add("Chat");
        arrayAdapter.add("Cancel");


        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String strOption = arrayAdapter.getItem(which);
                if(strOption.equalsIgnoreCase("View Items"))
                {
                    Intent i = new Intent(HistoryActivity.this, TransactionViewItems.class);
                    i.putExtra("ticket_id",array_string[0]);
                    i.putExtra("customer_name",array_string[1]);
                    i.putExtra("delivery_charge",array_string[2]);
                    i.putExtra("order_from",array_string[4]);
                    startActivity(i);
                }
                else if(strOption.equalsIgnoreCase("View Time Frame"))
                {
                    Intent i = new Intent(HistoryActivity.this, TransactionViewTimeFrame2.class);
                    i.putExtra("ticket_id",array_string[0]);
                    i.putExtra("customer_name",array_string[1]);
                    i.putExtra("activity",activity_name);
                    startActivity(i);
                }
                else if (strOption.equalsIgnoreCase("View Customer Details"))
                {
                    Intent i = new Intent(HistoryActivity.this, TransactionViewCustomerDetails.class);
                    i.putExtra("ticket_id", array_string[0]);
                    i.putExtra("activity",activity_name);
                    i.putExtra("cus_id", array_string[3]);
                    i.putExtra("order_from", array_string[4]);
                    startActivity(i);
                }
                else if (strOption.equalsIgnoreCase("Chat")) {
                    Intent i = new Intent(HistoryActivity.this, ChatboxMessages.class);
                    i.putExtra("ticket_id", array_string[0]);
                    i.putExtra("user_name", array_string[1]);
                    i.putExtra("from", "history");
                    globalvars.set("user_type", "Customer");
                    startActivity(i);
                }
                else if(strOption.equalsIgnoreCase("Cancel"))
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

    @Override
    public void onBackPressed() {
        // super.onBackPressed(); commented this line in order to disable back press
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
                filteredList = new ArrayList<DownloadedHistoryData>();
                if (text.length() > 0)
                {
                    for (DownloadedHistoryData la : itemlist) {
                        if (la.getName().toLowerCase().trim().contains(text.toLowerCase().trim())) {
                            filteredList.add(la);
                        }
                    }
                }else{
                    filteredList.addAll(itemlist);
                }
                adapter.setData(filteredList);
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}
