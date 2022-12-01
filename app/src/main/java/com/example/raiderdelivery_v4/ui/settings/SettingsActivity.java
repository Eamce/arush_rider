package com.example.raiderdelivery_v4.ui.settings;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.raiderdelivery_v4.ui.global.Globalvars;
import com.example.raiderdelivery_v4.R;

public class SettingsActivity extends AppCompatActivity {
    RadioGroup rg_category;
    RadioButton rb_cat_food, rb_cat_grocery;
    Button btn_update;
    Globalvars globalvars;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        globalvars = new Globalvars((Context)this,(Activity)this);
        rg_category = (RadioGroup)findViewById(R.id.rg_category);
        rb_cat_grocery = (RadioButton)findViewById(R.id.rb_grocery);
        rb_cat_food = (RadioButton)findViewById(R.id.rb_food);
        btn_update = (Button)findViewById(R.id.btn_update);

        if(globalvars.get("category").equals("Food")){
            rg_category.check(R.id.rb_food);
        }else{
            rg_category.check(R.id.rb_grocery);
        }

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // get selected radio button from radioGroup
                int selectedId = rg_category.getCheckedRadioButtonId();
                // find the radiobutton by returned id
                RadioButton radioButton = (RadioButton) findViewById(selectedId);

                if(globalvars.get("category").equals(radioButton.getText())){
                    Toast toast = Toast.makeText(SettingsActivity.this,"Category has no changes!",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,80);
                    toast.show();
                }else{
                    globalvars.set("category",radioButton.getText().toString());
                    Toast toast = Toast.makeText(SettingsActivity.this,"Category has been successfully updated!",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,80);
                    toast.show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed(); commented this line in order to disable back press
    }
}
