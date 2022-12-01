package com.example.raiderdelivery_v4.ui.chatbox;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.raiderdelivery_v4.R;
import com.example.raiderdelivery_v4.ui.global.Ajax;
import com.example.raiderdelivery_v4.ui.global.Globalvars;
import com.pusher.client.PusherOptions;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.HashMap;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.PusherEvent;
import com.pusher.client.channel.SubscriptionEventListener;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;

public class ChatboxMessages extends AppCompatActivity {
    EditText et_message;
    Button btn_send;
    ProgressDialog pd1;
    ListView lv_chatbox;
    Ajax mo;
    DownloadedChatMessages itemrow;
    ArrayList<DownloadedChatMessages> itemlist;
    ChatboxAdapter adapter;
    Globalvars globalvars;
    String user_name, from, ticket_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatbox_messages);

        globalvars = new Globalvars((Context)this,(Activity)this);
        et_message = findViewById(R.id.et_message);
        btn_send = findViewById(R.id.btn_send);
        lv_chatbox = findViewById(R.id.lv_chatbox);

        Intent i = getIntent();
        user_name = i.getExtras().getString("user_name");
        ticket_id = i.getExtras().getString("ticket_id");
        from = i.getExtras().getString("from");

        this.setTitle(user_name);

        execute_pusher();
        load_messages();

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!et_message.getText().toString().isEmpty())
                {
                    send_message(et_message.getText().toString());
                    //checkConnection();
                }
            }
        });

    }

    private void execute_pusher() {
        PusherOptions options = new PusherOptions();
        options.setCluster("ap1");

        Pusher pusher = new Pusher("b78f6990d12c34f243d2", options);

        pusher.connect(new ConnectionEventListener() {
            @Override
            public void onConnectionStateChange(ConnectionStateChange change) {
                Log.i("Pusher", "State changed from " + change.getPreviousState() +
                        " to " + change.getCurrentState());
            }

            @Override
            public void onError(String message, String code, Exception e) {
                Log.i("Pusher", "There was a problem connecting! " +
                        "\ncode: " + code +
                        "\nmessage: " + message +
                        "\nException: " + e
                );
            }
        }, ConnectionState.ALL);

        Channel channel = pusher.subscribe("RIDER-" + globalvars.get("id"));

        channel.bind("send-message", new SubscriptionEventListener() {
            @Override
            public void onEvent(PusherEvent event) {
                Log.i("Pusher", "Received event with data: " + event.toString());
                load_messages();
            }
        });
    }


    protected void onDestroy() {
        super.onDestroy();
    }


    public void load_messages() {
        itemlist = new ArrayList<>();
//        pd1 = new ProgressDialog(this);
//        pd1.setMessage("Please wait...");
//        pd1.show();
//        pd1.setCancelable(false);
//        pd1.setCanceledOnTouchOutside(false);
        mo = new Ajax();
        mo.setCustomObjectListener(new Ajax.MyCustomObjectListener() {
            @Override
            public void onerror() {
//                pd1.dismiss();
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
                    String id, contact_type_from, from_id, contact_type_to, to_id, body, attachment, remove_status, seen, seen_at, updated_at, created_at;

                    if (thedata.length() > 0) {
                        for (int a = 0; a < thedata.length(); a++) {
                            JSONArray row = thedata.getJSONArray(a);
                            id = row.getString(0);
                            contact_type_from = row.getString(1);
                            from_id = row.getString(2);
                            contact_type_to = row.getString(3);
                            to_id = row.getString(4);
                            body = row.getString(5);
                            attachment = row.getString(6);
                            remove_status = row.getString(7);
                            seen = row.getString(8);
                            seen_at = row.getString(9);
                            created_at = row.getString(10);
                            updated_at = row.getString(11);

                            itemrow = new DownloadedChatMessages(id, contact_type_from, from_id, contact_type_to, to_id, body, attachment, remove_status, seen, seen_at, created_at, updated_at);
                            itemlist.add(itemrow);
                        }
                        adapter = new ChatboxAdapter(ChatboxMessages.this, R.layout.chatbox_csr_content, itemlist, globalvars);
                        lv_chatbox.setAdapter(adapter);
                        scrollMyListViewToBottom();
                        et_message.setText("");

                        lv_chatbox.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                            //Mo trigger if i long press ang listview....
                            @Override
                            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                                final TextView emp_name = (TextView) view.findViewById(R.id.tv_chatbox_message);
                                final TextView str_id = (TextView) view.findViewById(R.id.tv_chatbox_id);
                                final TextView str_remarks = (TextView) view.findViewById(R.id.tv_chatbox_remarks);
                                final TextView str_remove_status = (TextView) view.findViewById(R.id.tv_chatbox_remove_status);
                                //final String emp_tag[] = emp_name.getTag().toString().split("\\|");

                                if(str_remarks.getText().toString().equalsIgnoreCase("1"))
                                {
                                    if (str_remove_status.getText().toString().equalsIgnoreCase("0"))
                                    {
                                        AlertDialog.Builder builderSingle = new AlertDialog.Builder(ChatboxMessages.this);

                                        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ChatboxMessages.this, android.R.layout.simple_list_item_1);
                                        arrayAdapter.add("Remove Message");

                                        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });

                                        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                String strOption = arrayAdapter.getItem(which);
                                                if (strOption.equalsIgnoreCase("Remove Message")) {
                                                    Log.e("Message :", emp_name.getText().toString());
                                                    Log.e("ID :", str_id.getText().toString());
                                                    Log.e("Remarks :", str_remarks.getText().toString());
                                                    RemoveMessage(str_id.getText().toString());

                                                }
                                            }
                                        });
                                        builderSingle.show();
                                    }
                                }

                                return false;
                            }
                        });
                    }
                    else
                    {
                        //pd1.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {

                }
            }
        });

        String str_id = globalvars.get("id");
        String str_user_type = globalvars.get("user_type");
        String str_user_name = user_name;

        mo.adddata("rider_id", globalvars.get("id"));
        mo.adddata("user_type", globalvars.get("user_type"));

        if(from.equalsIgnoreCase("chat"))
        {
            mo.adddata("user_name", user_name);
            mo.execute(Globalvars.online_link + "load_messages");
        }
        else
        {
            mo.adddata("user_name", ticket_id);
            mo.execute(Globalvars.online_link + "load_messages_from_transaction");
        }
    }

    public void RemoveMessage(String id)
    {
        pd1 = new ProgressDialog(this);
        pd1.setMessage("Please wait...");
        pd1.show();
        pd1.setCancelable(false);
        pd1.setCanceledOnTouchOutside(false);
        mo = new Ajax();
        mo.setCustomObjectListener(new Ajax.MyCustomObjectListener() {
            @Override
            public void onerror() {
                pd1.dismiss();
                Toast toast = Toast.makeText(getApplicationContext(), "Unable to connect. Please check your connection.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 80);
                toast.show();
            }

            @Override
            public void onsuccess(String data) {
                load_messages();
                pd1.dismiss();
            }
        });

        mo.adddata("message_id", id);
        mo.execute(Globalvars.online_link + "remove_message");
    }

    public void checkConnection(){
        PusherOptions options = new PusherOptions().setCluster("ap1");
        com.pusher.client.Pusher pusher = new com.pusher.client.Pusher("b78f6990d12c34f243d2", options);

        pusher.connect(new ConnectionEventListener() {
        @Override
        public void onConnectionStateChange(ConnectionStateChange change) {
            System.out.println("State changed to " + change.getCurrentState() +
                    " from " + change.getPreviousState());
        }

        @Override
        public void onError(String message, String code, Exception e) {
            System.out.println("There was a problem connecting!");
        }
    }, ConnectionState.ALL);
    }

    public void scrollMyListViewToBottom() {
        lv_chatbox.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                lv_chatbox.setSelection(adapter.getCount() - 1);
            }
        });
    }

    public void send_message(String message) {
        itemlist = new ArrayList<>();
//        pd1 = new ProgressDialog(this);
//        pd1.setMessage("Please wait...");
//        pd1.show();
//        pd1.setCancelable(false);
//        pd1.setCanceledOnTouchOutside(false);
        mo = new Ajax();
        mo.setCustomObjectListener(new Ajax.MyCustomObjectListener() {
            @Override
            public void onerror() {
//                pd1.dismiss();
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
                    String id, contact_type_from, from_id, contact_type_to, to_id, body, attachment, remove_status, seen, seen_at, updated_at, created_at;

                    if (thedata.length() > 0) {
                        for (int a = 0; a < thedata.length(); a++) {
                            JSONArray row = thedata.getJSONArray(a);
                            id = row.getString(0);
                            contact_type_from = row.getString(1);
                            from_id = row.getString(2);
                            contact_type_to = row.getString(3);
                            to_id = row.getString(4);
                            body = row.getString(5);
                            attachment = row.getString(6);
                            remove_status = row.getString(7);
                            seen = row.getString(8);
                            seen_at = row.getString(9);
                            created_at = row.getString(10);
                            updated_at = row.getString(11);

                            itemrow = new DownloadedChatMessages(id, contact_type_from, from_id, contact_type_to, to_id, body, attachment, remove_status, seen, seen_at, created_at, updated_at);
                            itemlist.add(itemrow);
                        }
                        adapter = new ChatboxAdapter(ChatboxMessages.this, R.layout.chatbox_csr_content, itemlist, globalvars);
                        lv_chatbox.setAdapter(adapter);
                        scrollMyListViewToBottom();
                        et_message.setText("");
//                        pd1.dismiss();
                    }
                    else
                    {
//                        pd1.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {

                }
            }
        });


        String a = globalvars.get("id");
//        String b = message;
        String c = globalvars.get("user_type");
        String d = user_name;
        String e = ticket_id;

        mo.adddata("rider_id", globalvars.get("id"));
        mo.adddata("message", message);
        mo.adddata("user_type", globalvars.get("user_type"));

        if(from.equalsIgnoreCase("chat"))
        {
            mo.adddata("receiver_name", user_name);
            mo.execute(Globalvars.online_link + "sendmessage");
//            mo.adddata("receiver_name", ticket_id);
//            mo.execute(Globalvars.online_link + "sendmessage_from_transaction");
        }
        else
        {
            mo.adddata("ticket_id", ticket_id);
            mo.execute(Globalvars.online_link + "sendmessage_from_transaction");
        }
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed(); commented this line in order to disable back press
    }
}

