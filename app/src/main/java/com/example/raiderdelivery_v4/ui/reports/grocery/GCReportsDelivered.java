package com.example.raiderdelivery_v4.ui.reports.grocery;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
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
import com.example.raiderdelivery_v4.ui.reports.food.DownloadedReportData;
import com.example.raiderdelivery_v4.ui.reports.food.ReportsDelivered;
import com.example.raiderdelivery_v4.ui.reports.food.ReportsDeliveredAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class GCReportsDelivered extends AppCompatActivity {
    Globalvars globalvars;
    ArrayList<GCDownloadedReportData> itemlist;
    Ajax mo;
    GCReportsDeliveredAdapter adapter;
    Double dbl_grandtotal = 0.00, dbl_delivery_charge = 0.00, dbl_picking_charge = 0.00;
    TextView tv_grandtotal, tv_delivered_note, tv_deliveredreports_no_of_customer, tv_delivered_noresults;
    LinearLayout ll_delivered_noresults;
    ImageView iv_delivered_noresult;
    ListView lv_deliveredreports;
    int no_of_customer = 0;
    Boolean is_empty = false, is_empty2 = false, mobile_is_empty2 = false;
    GCDownloadedReportData itemrow;
    ProgressDialog pd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gc_reports_delivered);
        tv_grandtotal = findViewById(R.id.tv_gc_reports_delivered_total);
        tv_delivered_note = findViewById(R.id.tv_gc_deliveredreports_note);
        tv_deliveredreports_no_of_customer = findViewById(R.id.tv_gc_deliveredreports_no_of_customer);
        ll_delivered_noresults = findViewById(R.id.ll_gc_delivered_noresults);
        iv_delivered_noresult = findViewById(R.id.iv_gc_delivered_noresult);
        tv_delivered_noresults = findViewById(R.id.tv_gc_delivered_noresults);
        lv_deliveredreports = findViewById(R.id.lv_gc_deliveredreports);
        globalvars = new Globalvars(this,this);
        //this.setTitle("Delivered Report(" + globalvars.get("category") + ")");
        get_delivered_reports();
    }

    public void get_delivered_reports(){
        itemlist = new ArrayList<>();
        //final ListView listView = findViewById(R.id.lv_deliveredreports);
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
                    String id, ticket_no, fname, lname, fullname, barangay, town, address, order, charge, totalamount, discount, view_stat, ontransit, delivered, cancelled, remitted, contact_no, order_from, tendered_amount, change, main_rider_stat, count_rider, picking_charge;
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
                            picking_charge = row.getString(21);

                            if(cancelled.equals("0")) {
                                is_empty2 = true;
                                no_of_customer = no_of_customer + 1;
                                dbl_delivery_charge = dbl_delivery_charge + (Double.parseDouble(charge) * Double.parseDouble(count_rider));
                                dbl_picking_charge = dbl_picking_charge + Double.parseDouble(picking_charge);

                                if(discount.equalsIgnoreCase("0.00")){
                                    dbl_grandtotal = dbl_grandtotal + Double.parseDouble(totalamount);
                                }else{
                                    dbl_grandtotal = dbl_grandtotal + (Double.parseDouble(totalamount) - Double.parseDouble(discount));
                                }

                                itemrow = new GCDownloadedReportData(id, fullname, address, order, charge, totalamount, discount, view_stat, ontransit, delivered, cancelled, ticket_no, contact_no, order_from, tendered_amount, change, main_rider_stat, count_rider, picking_charge);
                                itemlist.add(itemrow);
                            }
                        }
                        adapter = new GCReportsDeliveredAdapter(GCReportsDelivered.this,R.layout.gc_reports_content, itemlist);
                        lv_deliveredreports.setAdapter(adapter);
                        pd.dismiss();
                    }
                    else
                    {
                        is_empty = true;
                        pd.dismiss();
                        hidewidgets();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    if(!is_empty2){
                        hidewidgets();
                    }else{
                        DecimalFormat df = new DecimalFormat("#,##0.00");
                        df.setMaximumFractionDigits(2);
                        tv_deliveredreports_no_of_customer.setText("Total no. of Customer: " + no_of_customer);
                        tv_delivered_note.setText("Del. Amt.+Picking Charge+Del. Charge(P " + df.format(dbl_grandtotal) + " + P " + df.format(dbl_picking_charge) + " + P " + df.format(dbl_delivery_charge) + ")");
                        tv_grandtotal.setText("PHP " + df.format(dbl_grandtotal + dbl_picking_charge + dbl_delivery_charge));
                    }
                }
            }
        });
        Intent intent = getIntent();
        String a = globalvars.get("r_id_num");
        String b = intent.getExtras().getString("selected_date");
        mo.adddata("r_id_num",globalvars.get("r_id_num"));
        mo.adddata("delevered_status","1");
        mo.adddata("selected_date",intent.getExtras().getString("selected_date"));
        //mo.adddata("bunit_code", globalvars.get("bunit_code"));
        //String a = globalvars.get("bunit_code");
        mo.execute(Globalvars.online_link + "gc_get_reports_delivered_items");
    }

    public void hidewidgets(){
        ll_delivered_noresults.setVisibility(View.VISIBLE);
        iv_delivered_noresult.setVisibility(View.VISIBLE);
        tv_delivered_noresults.setVisibility(View.VISIBLE);
        lv_deliveredreports.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed(); commented this line in order to disable back press
    }
}
