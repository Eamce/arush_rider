package com.example.raiderdelivery_v4.ui.reports.food;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.raiderdelivery_v4.ui.account.CreateAccount;
import com.example.raiderdelivery_v4.ui.global.Globalvars;
import com.example.raiderdelivery_v4.R;
import com.example.raiderdelivery_v4.ui.reports.grocery.GCReportsCancelled;
import com.example.raiderdelivery_v4.ui.reports.grocery.GCReportsDelivered;
import com.example.raiderdelivery_v4.ui.reports.grocery.GCReportsUndelivered;

import java.util.Date;

public class ReportsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    Globalvars globalvars;
    Button btn_generate;
    RadioGroup rg_reports_category;
    DatePicker dp_date;
    Spinner spin_payment_method;
    String selected_date;
    int day, month, year;
    private static final String[] paths = {"All", "Online Payment", "Cash on Delivery"};
    String payment_method = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reports);
        globalvars = new Globalvars((Context)this,(Activity)this);
        this.setTitle("Reports");
        rg_reports_category = (RadioGroup)findViewById(R.id.rg_reports_category);
        spin_payment_method = findViewById(R.id.spin_payment_method);
        btn_generate = (Button)findViewById(R.id.btn_generate);
        dp_date = (DatePicker)findViewById(R.id.dp_reports);
        rg_reports_category.check(R.id.rb_delivered);
        dp_date.setMaxDate(new Date().getTime());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ReportsActivity.this,
                android.R.layout.simple_spinner_item,paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_payment_method.setAdapter(adapter);
        spin_payment_method.setOnItemSelectedListener(this);

        btn_generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // get selected radio button from radioGroup
                int selectedId = rg_reports_category.getCheckedRadioButtonId();
                // find the radiobutton by returned id
                RadioButton radioButton = (RadioButton) findViewById(selectedId);

                if("Delivered".equals(radioButton.getText()))
                {
                    if(globalvars.get("category").equalsIgnoreCase("Food"))
                    {
                        Intent i = new Intent(ReportsActivity.this, ReportsDelivered.class);
                        day = dp_date.getDayOfMonth();
                        month = dp_date.getMonth()+1;
                        year = dp_date.getYear();
                        selected_date = year+"-"+month+"-"+day;
                        i.putExtra("selected_date",selected_date);
                        i.putExtra("payment_method",payment_method);
                        startActivity(i);
                    }
                    else
                    {
                        Intent i = new Intent(ReportsActivity.this, GCReportsDelivered.class);
                        day = dp_date.getDayOfMonth();
                        month = dp_date.getMonth()+1;
                        year = dp_date.getYear();
                        selected_date = year+"-"+month+"-"+day;
                        i.putExtra("selected_date",selected_date);
                        startActivity(i);
                    }
//                    Intent i = new Intent(ReportsActivity.this, ReportsDelivered.class);
//                    day = dp_date.getDayOfMonth();
//                    month = dp_date.getMonth()+1;
//                    year = dp_date.getYear();
//                    selected_date = year+"-"+month+"-"+day;
//                    i.putExtra("selected_date",selected_date);
//                    startActivity(i);
                }
                else if("Cancelled".equals(radioButton.getText()))
                {
                    if(globalvars.get("category").equalsIgnoreCase("Food"))
                    {
                        Intent i = new Intent(ReportsActivity.this, ReportsCancelled.class);
                        day = dp_date.getDayOfMonth();
                        month = dp_date.getMonth()+1;
                        year = dp_date.getYear();
                        selected_date = year+"-"+month+"-"+day;
                        i.putExtra("selected_date",selected_date);
                        i.putExtra("payment_method",payment_method);
                        startActivity(i);
                    }
                    else
                    {
                        Intent i = new Intent(ReportsActivity.this, GCReportsCancelled.class);
                        day = dp_date.getDayOfMonth();
                        month = dp_date.getMonth()+1;
                        year = dp_date.getYear();
                        selected_date = year+"-"+month+"-"+day;
                        i.putExtra("selected_date",selected_date);
                        startActivity(i);
                    }
//                    Intent i = new Intent(ReportsActivity.this, ReportsCancelled.class);
//                    day = dp_date.getDayOfMonth();
//                    month = dp_date.getMonth()+1;
//                    year = dp_date.getYear();
//                    selected_date = year+"-"+month+"-"+day;
//                    i.putExtra("selected_date",selected_date);
//                    startActivity(i);
                }
                else
                {
                    if(globalvars.get("category").equalsIgnoreCase("Food"))
                    {
                        Intent i = new Intent(ReportsActivity.this, ReportsUndelivered.class);
                        day = dp_date.getDayOfMonth();
                        month = dp_date.getMonth()+1;
                        year = dp_date.getYear();
                        selected_date = year+"-"+month+"-"+day;
                        i.putExtra("selected_date",selected_date);
                        startActivity(i);
                    }
                    else
                    {
                        Intent i = new Intent(ReportsActivity.this, GCReportsUndelivered.class);
                        day = dp_date.getDayOfMonth();
                        month = dp_date.getMonth()+1;
                        year = dp_date.getYear();
                        selected_date = year+"-"+month+"-"+day;
                        i.putExtra("selected_date",selected_date);
                        startActivity(i);
                    }
//                    Intent i = new Intent(ReportsActivity.this, ReportsUndelivered.class);
//                    day = dp_date.getDayOfMonth();
//                    month = dp_date.getMonth()+1;
//                    year = dp_date.getYear();
//                    selected_date = year+"-"+month+"-"+day;
//                    i.putExtra("selected_date",selected_date);
//                    startActivity(i);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed(); commented this line in order to disable back press
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        payment_method = paths[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
