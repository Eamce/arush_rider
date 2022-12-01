package com.example.raiderdelivery_v4.ui.chatbox;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.raiderdelivery_v4.R;
import com.example.raiderdelivery_v4.ui.global.Ajax;
import com.example.raiderdelivery_v4.ui.global.Globalvars;
import com.example.raiderdelivery_v4.ui.transaction.food.DownloadedTransactionData;

import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.HashMap;

public class ChatboxUsers extends AppCompatActivity {
    DownloadedChatboxUsers itemrow;
    ArrayList<DownloadedChatboxUsers> itemlist;
    ArrayList<DownloadedChatboxUsers> filteredList;
    ProgressDialog pd;
    Ajax mo;
    ChatboxUsersAdapter adapter;
    ListView lv_chatbox_users;
    Globalvars globalvars;
    String user_type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatbox_users);
        lv_chatbox_users = findViewById(R.id.lv_chatbox_users);
        globalvars = new Globalvars((Context)this,(Activity)this);
//        Intent i = getIntent();
//        String user_type = i.getExtras().getString("user_type");
//        globalvars.set("user_type", user_type);

          user_type = globalvars.get("user_type");
          GetUsers(user_type);
          Log.e("USER TYPE: ",user_type);

    }

    protected void onDestroy() {

        super.onDestroy();
    }


    private void GetUsers(final String user_type) {
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
                    String ticket_id, name, login_status;
                    if (thedata.length() > 0)
                    {
                        for (int a = 0; a < thedata.length(); a++)
                        {
                            JSONArray row = thedata.getJSONArray(a);
                            ticket_id = row.getString(0);
                            name = row.getString(1);
                            login_status = row.getString(2);
                            itemrow = new DownloadedChatboxUsers(ticket_id,name, login_status);
                            itemlist.add(itemrow);
                           // System.out.println("USER DATA: "+row.getString(a));
                        }
                        adapter = new ChatboxUsersAdapter(ChatboxUsers.this, R.layout.chatbox_users_content, itemlist);
                        lv_chatbox_users.setAdapter(adapter);
                        pd.dismiss();
                        lv_chatbox_users.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                final TextView emp_name = (TextView) view.findViewById(R.id.tv_chatbox_name);
                                String user_name = emp_name.getText().toString();
                                Intent in = new Intent(ChatboxUsers.this, ChatboxMessages.class);
                                in.putExtra("user_name", user_name);
                                in.putExtra("from", "chat");
                                startActivity(in);
                            }
                        });

//                        lv_chatbox_users.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//                            //Mo trigger if i long press ang listview....
//                            @Override
//                            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                                Log.e("Message: ","Long pressed");
//                                final TextView emp_name = (TextView) view.findViewById(R.id.tv_chatbox_name);
//                                String user_name = emp_name.getText().toString();
//                                Intent i = new Intent(ChatboxUsers.this, ChatboxMessages.class);
//                                i.putExtra("user_name", user_name);
//                                i.putExtra("from", "chat");
//                                startActivity(i);
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
        if(user_type.equalsIgnoreCase("Tenant"))
        {
            String a = globalvars.get("tenant_id");
            mo.adddata("tenant_id", globalvars.get("tenant_id"));
            mo.execute(Globalvars.online_link + "get_tenants_users");
        }
        else
        {
            Log.e("USER TYPE: ",user_type);
            mo.adddata("user_type", user_type);
            mo.adddata("rider_id", globalvars.get("id"));
            mo.execute(Globalvars.online_link + "get_chatbox_users");
        }
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
        //mSearch.getActionView().setMinimumWidth(500);
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
                filteredList = new ArrayList<DownloadedChatboxUsers>();
                if (text.length() > 0) {
                    for (DownloadedChatboxUsers la : itemlist) {
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

}
