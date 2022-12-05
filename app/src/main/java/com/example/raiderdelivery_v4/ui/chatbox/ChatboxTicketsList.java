package com.example.raiderdelivery_v4.ui.chatbox;


import androidx.appcompat.app.AppCompatActivity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raiderdelivery_v4.R;
import com.example.raiderdelivery_v4.ui.global.Ajax;
import com.example.raiderdelivery_v4.ui.global.Globalvars;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatboxTicketsList extends AppCompatActivity {

    Tickets tickets;
    ArrayList<Tickets> ticket_list;
    ListView ticketList;
    String id, name;
    TicketAdapter adapter;
    ProgressDialog pd;
    Globalvars globalvars;
    Ajax mo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbox_tickets_list);
        globalvars = new Globalvars(this, this);
        Intent i = getIntent();
        id = i.getExtras().getString("id");
        name = i.getExtras().getString("user_name");
       ticketList =  findViewById(R.id.ticketLists);
       getTickets(id, name);
    }

    public void  getTickets(String id, final String username){
            ticket_list = new ArrayList<>();
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
                    String tcktname,tcktid;
                    {
                        for (int a = 0; a < thedata.length(); a++)
                        {
                            JSONArray row = thedata.getJSONArray(a);
                            tcktid = row.getString(0);
                            tcktname = row.getString(1);
                            tickets = new Tickets(tcktid,tcktname);
                            ticket_list.add(tickets);
                            // System.out.println("USER DATA: "+row.getString(a));
                        }
                        adapter = new TicketAdapter(ChatboxTicketsList.this, R.layout.ticket_layout, ticket_list);
                        ticketList.setAdapter(adapter);
                        pd.dismiss();
                        ticketList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Intent in = new Intent(ChatboxTicketsList.this, ChatboxMessages.class);
                                in.putExtra("user_name", username);
                                in.putExtra("from", "chat");
                                in.putExtra("ticket", adapter.getItem(i).getName());
                                in.putExtra("tcktid", adapter.getItem(i).getId());
                                Log.e("TICKET ID: ", adapter.getItem(i).getId());
                                globalvars.set("user_type", "Customer");
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

        mo.adddata("id", id);
        mo.execute(Globalvars.online_link + "getTickets");
    }
}