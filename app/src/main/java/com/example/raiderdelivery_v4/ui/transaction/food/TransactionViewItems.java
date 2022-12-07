package com.example.raiderdelivery_v4.ui.transaction.food;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class TransactionViewItems extends AppCompatActivity {
    Ajax mo, mo1;
    ArrayList<DownloadedTenantData> itemlist;
    LinkedHashMap<String, DownloadedTenantData> expandableListDetail = new LinkedHashMap<String, DownloadedTenantData>();
    ExpandableListView lv_transaction_view_items;
    TransactionViewItemsAdapter adapter;
    Globalvars globalvars;
    Double dbl_grandtotal = 0.00, dbl_delivery_charge = 0.00, dbl_subtotal = 0.00, dbl_total_by_tenant = 0.00;
    TextView tv_grandtotal, tv_transaction_view_items_note, tv_transaction_view_items_no_of_items;
    ProgressDialog pd;
    int no_of_items;
    String str_total_by_tenant, order_from;
    Double dbl_fnl_total_per_tenant = 0.00;
    Handler handler;
    Runnable r;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_view_items);
        lv_transaction_view_items = findViewById(R.id.elv_trans_view_items2);
        tv_grandtotal = findViewById(R.id.tv_transaction_view_items_grandtotal2);
        tv_transaction_view_items_note = findViewById(R.id.tv_transaction_view_items_note2);
        tv_transaction_view_items_no_of_items = findViewById(R.id.tv_transaction_view_items_no_of_items2);
        globalvars = new Globalvars((Context) this, (Activity) this);
        Intent intent = getIntent();
        this.setTitle(intent.getExtras().getString("customer_name"));
        order_from = intent.getExtras().getString("order_from");

            get_items_breakdown();

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
                    View dialogView = LayoutInflater.from(TransactionViewItems.this).inflate(R.layout.my_dialog_session_timedout, viewGroup, false);
                    TextView tv_dialog_message = dialogView.findViewById(R.id.tv_dialog_message);
                    tv_dialog_message.setText("Your session has timed out. Please login again.");
                    //Now we need an AlertDialog.Builder object
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(TransactionViewItems.this);
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
                            Intent intent = new Intent(TransactionViewItems.this, Login.class);
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

    public void get_items_breakdown() {
        itemlist = new ArrayList<DownloadedTenantData>();
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
                ArrayList<HashMap<String, ArrayList<DownloadedItemsData>>> detailss = new ArrayList<HashMap<String, ArrayList<DownloadedItemsData>>>();
                try {
                    thedata = new JSONArray(data);
                    String id, prod_image, prod_name, prod_desc, prod_price, prod_qty, prod_subtotal, prod_delivery_charge, prod_tenant, prod_bu, prod_total_by_tenant, prod_discounted_amount;
                    if (thedata.length() > 0) {
                        for (int a = 0; a < thedata.length(); a++) {
                            JSONArray row = thedata.getJSONArray(a);
                            id = row.getString(0);
                            prod_image = row.getString(1);
                            prod_name = row.getString(2);
                            prod_desc = row.getString(3);
                            prod_price = row.getString(4);
                            prod_qty = row.getString(5);
                            prod_subtotal = row.getString(6);
                            prod_delivery_charge = row.getString(7);
                            prod_tenant = row.getString(8);
                            prod_bu = row.getString(9);
                            prod_tenant = prod_bu + "-" + prod_tenant;
                            prod_total_by_tenant = row.getString(10);
                            prod_discounted_amount = row.getString(11);
//                            if(prod_discounted_amount.equalsIgnoreCase("0.00"))
//                            {
//                                dbl_fnl_total_per_tenant = dbl_fnl_total_per_tenant + Double.parseDouble(prod_total_by_tenant);
//                            }
//                            else
//                            {
//                                dbl_fnl_total_per_tenant = dbl_fnl_total_per_tenant + Double.parseDouble(prod_discounted_amount);
//                            }
                            dbl_delivery_charge = Double.parseDouble(prod_delivery_charge);
                            //dbl_grandtotal = dbl_grandtotal + Double.parseDouble(fnl_total_per_tenant);
                            //get_items_breakdown_total_by_tenant(prod_tenant);
                            addProduct(id, prod_tenant, prod_image, prod_name, prod_desc, prod_price, prod_qty, prod_subtotal, prod_total_by_tenant, prod_discounted_amount);
                        }
                        adapter = new TransactionViewItemsAdapter(TransactionViewItems.this, itemlist);
                        lv_transaction_view_items.setAdapter(adapter);
                        no_of_items = thedata.length();
                        pd.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    DecimalFormat df = new DecimalFormat("#,##0.00");
                    df.setMaximumFractionDigits(2);
                    Intent intent = getIntent();
                    tv_transaction_view_items_no_of_items.setText("Total no. of Items: " + no_of_items);
                    tv_transaction_view_items_note.setText("Note: Additional delivery charge: PHP " + intent.getExtras().getString("delivery_charge"));
                    tv_grandtotal.setText("PHP " + df.format(dbl_grandtotal));
                    //Tag_as_viewed();
                }
            }
        });

        String a = globalvars.get("r_id_num");
        String b = getIntent().getExtras().getString("ticket_id");
        Log.e("R_ID_NUM :  ",globalvars.get("r_id_num"));
        Log.e("TICKET_ID: ",b);
        mo.adddata("r_id_num", globalvars.get("r_id_num"));
        mo.adddata("ticket_id", getIntent().getExtras().getString("ticket_id"));
        //mo.adddata("bunit_code", globalvars.get("bunit_code"));
        mo.execute(Globalvars.online_link + "get_items_breakdown");
    }

    public int addProduct(String id, String tenant, String prod_image, String prod_name, String prod_desc, String prod_price, String prod_qty, String prod_subtotal, String prod_total_by_tenant, String prod_discounted_amount) {
        String fnl_total_per_tenant, fnl_discount;
        Double dbl_fnl_discount;
        int groupPosition = 0;

        DecimalFormat df = new DecimalFormat("#,##0.00");
        df.setMaximumFractionDigits(2);

        if(prod_discounted_amount.equalsIgnoreCase(""))
        {
            fnl_total_per_tenant = prod_total_by_tenant;
            fnl_discount = "0.00";
        }
        else
        {
            fnl_total_per_tenant = prod_discounted_amount;
            dbl_fnl_discount = Double.parseDouble(prod_total_by_tenant) - Double.parseDouble(prod_discounted_amount);
            fnl_discount = dbl_fnl_discount.toString();
        }

        //check the hash map if the group already exists
        DownloadedTenantData headerInfo = expandableListDetail.get(tenant);
        //add the group if doesn't exists
        if (headerInfo == null) {
            //String total = get_items_breakdown_total_by_tenant(tenant);
            headerInfo = new DownloadedTenantData();
            headerInfo.setTenant(tenant + "| (SUBTOT \u20B1" + df.format(Double.parseDouble(fnl_discount)) + " [DISC: \u20B1" + df.format(Double.parseDouble(fnl_total_per_tenant)) + "])");
            expandableListDetail.put(tenant, headerInfo);
            itemlist.add(headerInfo);
            dbl_grandtotal = dbl_grandtotal + Double.parseDouble(fnl_discount);
        }

        //get the children for the group
        ArrayList<DownloadedItemsData> productList = headerInfo.getProductList();
        //size of the children list
        int listSize = productList.size();
        //add to the counter
        listSize++;

        //create a new child and add that to the group
        DownloadedItemsData detailInfo = new DownloadedItemsData();
        detailInfo.setSequence(String.valueOf(listSize));
        detailInfo.setId(id);
        detailInfo.setImage(prod_image);
        detailInfo.setName(prod_name);
        detailInfo.setDesc(prod_desc);
        detailInfo.setPrice(prod_price);
        detailInfo.setQty(prod_qty);
        detailInfo.setSubtotal(prod_subtotal);
        productList.add(detailInfo);
        headerInfo.setProductList(productList);

        //find the group position inside the list
        groupPosition = itemlist.indexOf(headerInfo);
        return groupPosition;
    }

//    public String get_items_breakdown_total_by_tenant(String tenant)
//    {
//        final String[] sample = new String[1];
//        mo1 = new Ajax();
//        mo1.setCustomObjectListener(new Ajax.MyCustomObjectListener() {
//            @Override
//            public void onerror() {
//                pd.dismiss();
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
//                    int length = thedata.length();
//                    if (thedata.length() > 0) {
//                        for (int a = 0; a < thedata.length(); a++) {
//                            JSONArray row = thedata.getJSONArray(a);
//                            sample[0] = row.getString(0);
//                            globalvars.set("total_by_tenant",str_total_by_tenant);
//                        }
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                } finally {
//                }
//            }
//        });
//        mo1.adddata("tenant",tenant);
//        mo1.adddata("r_id_num",globalvars.get("r_id_num"));
//        mo1.adddata("ticket_id",getIntent().getExtras().getString("ticket_id"));
//        mo1.execute("http://172.16.43.234/e-commerce_rider/index.php/get_items_breakdown_total_by_tenant");
//        return sample[0];
//    }

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
}
