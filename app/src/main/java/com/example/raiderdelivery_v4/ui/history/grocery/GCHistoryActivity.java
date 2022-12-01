package com.example.raiderdelivery_v4.ui.history.grocery;

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
import com.example.raiderdelivery_v4.R;
import com.example.raiderdelivery_v4.ui.global.Ajax;
import com.example.raiderdelivery_v4.ui.global.Globalvars;
import com.example.raiderdelivery_v4.ui.history.food.DownloadedHistoryData;
import com.example.raiderdelivery_v4.ui.history.food.HistoryActivity;
import com.example.raiderdelivery_v4.ui.history.food.HistoryAdapter;
import com.example.raiderdelivery_v4.ui.transaction.food.TransactionViewCustomerDetails;
import com.example.raiderdelivery_v4.ui.transaction.food.TransactionViewItems;
import com.example.raiderdelivery_v4.ui.transaction.food.TransactionViewTimeFrame2;
import com.example.raiderdelivery_v4.ui.transaction.grocery.GCTransactionViewCustomerDetails;
import com.example.raiderdelivery_v4.ui.transaction.grocery.GCTransactionViewItems;
import com.example.raiderdelivery_v4.ui.transaction.grocery.GCTransactionViewTimeFrame;

import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;

public class GCHistoryActivity extends AppCompatActivity {
    ListView lv_history;
    TextView tv_note, tv_noresults;
    LinearLayout ll_noresults;
    ImageView iv_noresult;
    Ajax mo;
    ArrayList<GCDownloadedHistoryData> itemlist;
    ArrayList<GCDownloadedHistoryData> filteredList;
    GCHistoryAdapter adapter;
    ProgressDialog pd1;
    Globalvars globalvars;
    public static String activity_name = "GCHistoryActivity";
    int no_of_customer = 0;
    GCDownloadedHistoryData itemrow;
    ProgressDialog pd;
    Boolean web_is_empty = false, mobile_is_empty = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gc_history);
        lv_history = findViewById(R.id.lv_gc_history);
        tv_note = findViewById(R.id.tv_gc_history_no_of_customer);
        tv_noresults = findViewById(R.id.tv_gc_noresults);
        ll_noresults = findViewById(R.id.ll_gc_noresults);
        iv_noresult = findViewById(R.id.iv_gc_noresult);
        globalvars = new Globalvars((Context)this,(Activity)this);

        this.setTitle("History(Grocery)");
        get_history_orders();
    }
    public void get_history_orders(){
        itemlist = new ArrayList<>();
        final ListView listView = findViewById(R.id.lv_gc_history);
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
                    String id, cus_id, ticket_no, fname, lname, fullname, barangay, town, address, order, charge, totalamount;
                    String discount, view_stat, ontransit, delivered, cancelled, remitted, contact_no, num_pack, del_charge;
                    String landmark, created_at, order_from, tendered_amount, change, change_bu, count_rider, main_rider_stat , payment_platform;
                    String instructions, submit_status, picking_charge;
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
                            picking_charge = row.getString(28);
                            payment_platform = row.getString(29);

                            if(payment_platform.equalsIgnoreCase("null"))
                            {
                                payment_platform = "Cash on Delivery";
                            }

                            itemrow = new GCDownloadedHistoryData(id, cus_id, fullname, address, order, charge, totalamount, discount, view_stat,ontransit,delivered, cancelled, ticket_no, contact_no, num_pack, created_at, order_from, tendered_amount, change, change_bu, count_rider, main_rider_stat, picking_charge, payment_platform);
                            itemlist.add(itemrow);
                        }
                        adapter = new GCHistoryAdapter(GCHistoryActivity.this,R.layout.gc_history_content, itemlist);
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
        mo.execute(Globalvars.online_link + "gc_get_history_items");
    }

    public void hidewidgets(){
        ll_noresults.setVisibility(View.VISIBLE);
        iv_noresult.setVisibility(View.VISIBLE);
        tv_noresults.setVisibility(View.VISIBLE);
        lv_history.setVisibility(View.GONE);
    }

    public void HistoryOnClickView(View v){
        final Button n = (Button) v;
        final String array_string[] = n.getTag().toString().split("\\|");

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(GCHistoryActivity.this);
        builderSingle.setCancelable(false);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(GCHistoryActivity.this, android.R.layout.simple_list_item_1);
        arrayAdapter.add("View Customer Details");
        arrayAdapter.add("View Items");
        //arrayAdapter.add("View Time Frame");
        arrayAdapter.add("Cancel");


        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String strOption = arrayAdapter.getItem(which);
                if(strOption.equalsIgnoreCase("View Items"))
                {
                    Intent i = new Intent(GCHistoryActivity.this, GCTransactionViewItems.class);
                    i.putExtra("ticket_id",array_string[0]);
                    i.putExtra("customer_name",array_string[1]);
                    i.putExtra("delivery_charge",array_string[2]);
                    i.putExtra("order_from",array_string[4]);
                    startActivity(i);
                }
                else if(strOption.equalsIgnoreCase("View Time Frame"))
                {
                    Intent i = new Intent(GCHistoryActivity.this, GCTransactionViewTimeFrame.class);
                    i.putExtra("ticket_id",array_string[0]);
                    i.putExtra("customer_name",array_string[1]);
                    i.putExtra("activity",activity_name);
                    startActivity(i);
                }
                else if (strOption.equalsIgnoreCase("View Customer Details"))
                {
                    Intent i = new Intent(GCHistoryActivity.this, GCTransactionViewCustomerDetails.class);
                    i.putExtra("ticket_id", array_string[0]);
                    i.putExtra("activity",activity_name);
                    i.putExtra("cus_id", array_string[3]);
                    i.putExtra("order_from", array_string[4]);
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
                filteredList = new ArrayList<GCDownloadedHistoryData>();
                if (text.length() > 0)
                {
                    for (GCDownloadedHistoryData la : itemlist) {
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
