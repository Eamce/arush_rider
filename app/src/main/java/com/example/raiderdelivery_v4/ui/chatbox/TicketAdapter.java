package com.example.raiderdelivery_v4.ui.chatbox;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.raiderdelivery_v4.R;

import java.util.ArrayList;

public class TicketAdapter extends ArrayAdapter<Tickets> {
    private Context mContext;
    private int mResource;
    public ArrayList<Tickets> ticket_lists;


    public TicketAdapter(@NonNull Context context, int resource, ArrayList<Tickets> objects) {
        super(context, resource, objects);
        this.mContext = context;
        mResource = resource;
        this.ticket_lists = objects;
    }

    @Override
    public int getCount() {
        return ticket_lists.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Nullable
    @Override
    public Tickets getItem(int position) {
        return super.getItem(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final String ticketid = getItem(position).getName();

        TicketAdapter.ViewHolder holder;

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, null, false);
        holder = new TicketAdapter.ViewHolder();
        holder.ticketname = convertView.findViewById(R.id.ticket);
        convertView.setTag(holder);
        holder.ticketname.setText(ticketid);

        return convertView;

    }


    static class ViewHolder {
        TextView ticketname;
    }



    public void  setData(ArrayList<Tickets> modelList) {
        this.ticket_lists = modelList;
        notifyDataSetChanged();
    }

}
