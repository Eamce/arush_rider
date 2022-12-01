package com.example.raiderdelivery_v4.ui.reports.food;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.raiderdelivery_v4.ui.global.Ajax;
import com.example.raiderdelivery_v4.ui.global.Globalvars;
import com.example.raiderdelivery_v4.R;
import org.json.JSONArray;
import org.json.JSONException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class ReportsUndelivered extends AppCompatActivity {
    Globalvars globalvars;
    ArrayList<DownloadedReportData> itemlist;
    Ajax mo;
    ReportsUndeliveredAdapter adapter;
    Double dbl_grandtotal = 0.00, dbl_delivery_charge = 0.00;
    TextView tv_grandtotal, tv_undelivered_note, tv_undeliveredreports_no_of_customer, tv_undelivered_noresults;
    LinearLayout ll_undelivered_noresults;
    ImageView iv_undelivered_noresult;
    ListView lv_undeliveredreports;
    DatePicker dp_date;
    int no_of_customer = 0;
    DownloadedReportData itemrow;
    ProgressDialog pd;
    Boolean web_is_empty = false, mobile_is_empty = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reports_undelivered);
        tv_grandtotal = (TextView)findViewById(R.id.tv_reports_undelivered_total);
        tv_undelivered_note = findViewById(R.id.tv_undeliveredreports_note);
        tv_undeliveredreports_no_of_customer = findViewById(R.id.tv_undeliveredreports_no_of_customer);
        ll_undelivered_noresults = findViewById(R.id.ll_undelivered_noresults);
        iv_undelivered_noresult = findViewById(R.id.iv_undelivered_noresult);
        lv_undeliveredreports = findViewById(R.id.lv_undeliveredreports);
        tv_undelivered_noresults = findViewById(R.id.tv_undelivered_noresults);
        globalvars = new Globalvars((Context)this,(Activity)this);
        //this.setTitle("Undelivered Report(" + globalvars.get("category") + ")");
        get_undelivered_reports();
    }

    public void get_undelivered_reports(){
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
                    String id, ticket_no, fname, lname, fullname, barangay, town, address, order, charge, totalamount, discount, view_stat;
                    String ontransit, delivered, cancelled, remitted, contact_no, order_from, tendered_amount, change, main_rider_stat, count_rider, return_change_to_tenant;
                    if (thedata.length() > 0)
                    {
                        for (int a = 0; a < thedata.length(); a++) {
                            JSONArray row = thedata.getJSONArray(a);
                            id = row.getString(0);
                            fname = row.getString(1);
                            lname = row.getString(2);
                            fullname = fname + " " + lname;
                            barangay = row.getString(3);
                            town = row.getString(4);
                            address = barangay + ", " + town;
                            order = row.getString(5);
                            totalamount = row.getString(6);
                            discount = row.getString(7);
                            charge = row.getString(8);
                            view_stat = row.getString(9);
                            ontransit = row.getString(10);
                            delivered = row.getString(11);
                            cancelled = row.getString(12);
                            remitted = row.getString(13);
                            ticket_no = row.getString(14);
                            contact_no = row.getString(15);
                            order_from = row.getString(16);
                            tendered_amount = row.getString(17);
                            change = row.getString(18);
                            main_rider_stat = row.getString(19);
                            count_rider = row.getString(20);
                            //return_change_to_tenant = row.getString(19);

                            dbl_delivery_charge = dbl_delivery_charge + Double.parseDouble(charge)  * Double.parseDouble(count_rider);

                            if(discount.equalsIgnoreCase("0.00")){
                                dbl_grandtotal = dbl_grandtotal + Double.parseDouble(totalamount);
                            }else{
                                dbl_grandtotal = dbl_grandtotal + (Double.parseDouble(totalamount) - Double.parseDouble(discount));
                            }

                            itemrow = new DownloadedReportData(id,fullname, address, order, charge, totalamount, discount, view_stat,ontransit,delivered, cancelled, ticket_no, contact_no, order_from, tendered_amount, change, main_rider_stat, count_rider);
                            itemlist.add(itemrow);
                        }

                        adapter = new ReportsUndeliveredAdapter(ReportsUndelivered.this,R.layout.reports_content, itemlist);
                        lv_undeliveredreports.setAdapter(adapter);
                        pd.dismiss();
                        no_of_customer = no_of_customer + thedata.length();
                    }
                    else
                    {
                        hidewidgets();
                        pd.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    DecimalFormat df = new DecimalFormat("#,##0.00");
                    df.setMaximumFractionDigits(2);
                    tv_undeliveredreports_no_of_customer.setText("Total no. of Customer: " + no_of_customer);
                    tv_undelivered_note.setText("Order Amt. + Del. Charge(P " + df.format(dbl_grandtotal) +" + P "+df.format(dbl_delivery_charge)+")");
                    tv_grandtotal.setText("PHP " + df.format(dbl_grandtotal+dbl_delivery_charge));
                }
            }
        });
        Intent intent = getIntent();
        mo.adddata("r_id_num",globalvars.get("r_id_num"));
        mo.adddata("delevered_status","0");
        mo.adddata("selected_date",intent.getExtras().getString("selected_date"));
        //mo.adddata("bunit_code", globalvars.get("bunit_code"));
        mo.execute(Globalvars.online_link + "get_reports_undelivered_items");
    }

//    public void get_undelivered_reports_from_mobile(){
//        mo = new Ajax();
//        mo.setCustomObjectListener(new Ajax.MyCustomObjectListener() {
//            @Override
//            public void onerror() {
//                pd.dismiss();
//
//                Toast toast = Toast.makeText(getApplicationContext(), "Unable to connect. Please check your connection.", Toast.LENGTH_LONG);
//                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 80);
//                toast.show();
//            }
//
//            @Override
//            public void onsuccess(String data) {
//                JSONArray thedata;
//                ArrayList<HashMap<String, String>> detailss = new ArrayList<HashMap<String, String>>();
//                try {
//                    thedata = new JSONArray(data);
//                    String id, ticket_no, fname, lname, fullname, barangay, town, address, order, charge, totalamount, view_stat;
//                    String ontransit, delivered, cancelled, remitted, contact_no, order_from, tendered_amount, change, main_rider_stat, count_rider, return_change_to_tenant;
//                    if (thedata.length() > 0)
//                    {
//                        for (int a = 0; a < thedata.length(); a++) {
//                            JSONArray row = thedata.getJSONArray(a);
//                            id = row.getString(0);
//                            fname = row.getString(1);
//                            lname = row.getString(2);
//                            fullname = fname + " " + lname;
//                            barangay = row.getString(3);
//                            town = row.getString(4);
//                            address = barangay + ", " + town;
//                            order = row.getString(5);
//                            totalamount = row.getString(6);
//                            charge = row.getString(7);
//                            view_stat = row.getString(8);
//                            ontransit = row.getString(9);
//                            delivered = row.getString(10);
//                            cancelled = row.getString(11);
//                            remitted = row.getString(12);
//                            ticket_no = row.getString(13);
//                            contact_no = row.getString(14);
//                            order_from = row.getString(15);
//                            tendered_amount = row.getString(16);
//                            change = row.getString(17);
//                            main_rider_stat = row.getString(18);
//                            count_rider = row.getString(19);
//                            //return_change_to_tenant = row.getString(19);
//
//                            dbl_delivery_charge = dbl_delivery_charge + (Double.parseDouble(charge) * Double.parseDouble(count_rider));
//                            dbl_grandtotal = dbl_grandtotal + Double.parseDouble(totalamount);
//
//                            itemrow = new DownloadedReportData(id,fullname, address, order, charge, totalamount, view_stat,ontransit,delivered, cancelled, ticket_no, contact_no, order_from, tendered_amount, change, main_rider_stat, count_rider);
//                            itemlist.add(itemrow);
//                        }
//                        adapter = new ReportsUndeliveredAdapter(ReportsUndelivered.this,R.layout.reports_content, itemlist);
//                        lv_undeliveredreports.setAdapter(adapter);
//                        pd.dismiss();
//                        no_of_customer = no_of_customer + thedata.length();
//                    }
//                    else
//                    {
//                        if (web_is_empty) {
//                            hidewidgets();
//                        }else{
//                          adapter = new ReportsUndeliveredAdapter(ReportsUndelivered.this,R.layout.reports_content, itemlist);
//                            lv_undeliveredreports.setAdapter(adapter);
//                        }
//                        pd.dismiss();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                } finally {
//                    DecimalFormat df = new DecimalFormat("#,##0.00");
//                    df.setMaximumFractionDigits(2);
//                    tv_undeliveredreports_no_of_customer.setText("Total no. of Customer: " + no_of_customer);
//                    tv_undelivered_note.setText("Order Amt. + Del. Charge(P " + df.format(dbl_grandtotal) +" + P "+df.format(dbl_delivery_charge)+")");
//                    tv_grandtotal.setText("PHP " + df.format(dbl_grandtotal+dbl_delivery_charge));
//                }
//            }
//        });
//        Intent intent = getIntent();
//        //String sample1 = globalvars.get("r_id_num");
//        //String sample2 = intent.getExtras().getString("selected_date");
//        mo.adddata("r_id_num",globalvars.get("r_id_num"));
//        mo.adddata("delevered_status","0");
//        mo.adddata("selected_date",intent.getExtras().getString("selected_date"));
//        //mo.adddata("bunit_code", globalvars.get("bunit_code"));
//        mo.execute(Globalvars.offline_link + "get_reports_undelivered_items_from_mobile");
//    }

    public void hidewidgets(){
        ll_undelivered_noresults.setVisibility(View.VISIBLE);
        iv_undelivered_noresult.setVisibility(View.VISIBLE);
        tv_undelivered_noresults.setVisibility(View.VISIBLE);
        lv_undeliveredreports.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed(); commented this line in order to disable back press
    }
}
