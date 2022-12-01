package com.example.raiderdelivery_v4.ui.chatbox;


import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.raiderdelivery_v4.R;

import java.util.ArrayList;

public class ChatboxTicketsList extends AppCompatActivity {

    ArrayList<Tickets> tickets;
    ListView ticketList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbox_tickets_list);

       ticketList =  findViewById(R.id.ticketLists);

       ticketList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

           }
       });
    }

    public void  getTickets(){

    }
}