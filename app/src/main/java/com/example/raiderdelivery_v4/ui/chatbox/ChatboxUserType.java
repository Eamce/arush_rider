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

public class ChatboxUserType extends AppCompatActivity {
    ListView lv_chatbox_usertype;
    DownloadedChatboxUserType itemrow;
    ArrayList<DownloadedChatboxUserType> itemlist;
    ProgressDialog pd;
    Ajax mo;
    ChatboxUserTypeAdapter adapter;
    Globalvars globalvars;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatbox_user_type);
        lv_chatbox_usertype = findViewById(R.id.lv_chatbox_usertype);
        globalvars = new Globalvars((Context)this,(Activity)this);

        get_usertype();
    }

    protected void onDestroy() {

        super.onDestroy();
    }

    private void get_usertype() {
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
                    String type;

                    if (thedata.length() > 0) {
                        for (int a = 0; a < thedata.length(); a++) {
                            JSONArray row = thedata.getJSONArray(a);
                            type = row.getString(0);

                            itemrow = new DownloadedChatboxUserType(type);
                            itemlist.add(itemrow);
                        }
                        adapter = new ChatboxUserTypeAdapter(ChatboxUserType.this, R.layout.chatbox_user_type_content, itemlist);
                        lv_chatbox_usertype.setAdapter(adapter);
                        pd.dismiss();

                        lv_chatbox_usertype.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                final TextView emp_name = (TextView) view.findViewById(R.id.tv_chatbox_usertype);
                                String user_type = emp_name.getText().toString();
                                globalvars.set("user_type", user_type);

                                if(user_type.equalsIgnoreCase("Tenant"))
                                {
                                    Intent intent = new Intent(ChatboxUserType.this, ChatboxTenants.class);
                                    //i.putExtra("user_type", user_type);
                                    startActivity(intent);
                                }
                                else
                                {
                                    Intent intent = new Intent(ChatboxUserType.this, ChatboxUsers.class);
                                    //i.putExtra("user_type", user_type);
                                    startActivity(intent);
                                }

                            }
                        });




//                        lv_chatbox_usertype.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//                            //Mo trigger if i long press ang listview....
//                            @Override
//                            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                                final TextView emp_name = (TextView) view.findViewById(R.id.tv_chatbox_usertype);
//                                String user_type = emp_name.getText().toString();
//                                globalvars.set("user_type", user_type);
//
//                                if(user_type.equalsIgnoreCase("Tenant"))
//                                {
//                                    Intent i = new Intent(ChatboxUserType.this, ChatboxTenants.class);
//                                    //i.putExtra("user_type", user_type);
//                                    startActivity(i);
//                                }
//                                else
//                                {
//                                    Intent i = new Intent(ChatboxUserType.this, ChatboxUsers.class);
//                                    //i.putExtra("user_type", user_type);
//                                    startActivity(i);
//                                }
//                                return false;
//                            }
//                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {

                }
            }
        });
        mo.execute(Globalvars.online_link + "get_chatbox_usertype");
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed(); commented this line in order to disable back press
    }
}
