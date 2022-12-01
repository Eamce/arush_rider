package com.example.raiderdelivery_v4.ui.chatbox;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.raiderdelivery_v4.R;
import com.example.raiderdelivery_v4.ui.global.Ajax;
import com.example.raiderdelivery_v4.ui.global.Globalvars;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.HashMap;

public class ChatboxTenants extends AppCompatActivity {
    ListView lv_tenants;
    DownloadedChatboxTenants itemrow;
    ArrayList<DownloadedChatboxTenants> itemlist;
    ProgressDialog pd;
    Ajax mo;
    ChatboxTenantsAdapter adapter;
    Globalvars globalvars;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatbox_tenants);
        lv_tenants = findViewById(R.id.lv_chatbox_tenants);
        globalvars = new Globalvars((Context)this,(Activity)this);

        get_tenants();
    }
    protected void onDestroy() {
        super.onDestroy();
    }
    private void get_tenants() {

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
                    String id, tenant_name;

                    if (thedata.length() > 0) {
                        for (int a = 0; a < thedata.length(); a++) {
                            JSONArray row = thedata.getJSONArray(a);
                            id = row.getString(0);
                            tenant_name = row.getString(1);

                            itemrow = new DownloadedChatboxTenants(id, tenant_name);
                            itemlist.add(itemrow);
                        }
                        adapter = new ChatboxTenantsAdapter(ChatboxTenants.this, R.layout.chatbox_tenants_content, itemlist);
                        lv_tenants.setAdapter(adapter);
                        pd.dismiss();

                        lv_tenants.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                            //Mo trigger if i long press ang listview....
                            @Override
                            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                                final TextView user_id = (TextView) view.findViewById(R.id.tv_chatbox_id);
                                String str_user_id = user_id.getText().toString();
                                globalvars.set("tenant_id", str_user_id);
                                //Log.e("Tenant Name:" , str_user_id);

                                Intent i = new Intent(ChatboxTenants.this, ChatboxUsers.class);
                                //i.putExtra("user_type", user_type);
                                startActivity(i);

                                return false;
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {

                }
            }
        });
        mo.execute(Globalvars.online_link + "get_tenants");
    }
}
