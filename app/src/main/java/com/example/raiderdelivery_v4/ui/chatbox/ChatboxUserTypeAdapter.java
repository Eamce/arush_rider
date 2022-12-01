package com.example.raiderdelivery_v4.ui.chatbox;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.raiderdelivery_v4.R;

import java.util.ArrayList;
import java.util.Date;

public class ChatboxUserTypeAdapter extends ArrayAdapter<DownloadedChatboxUserType> implements Filterable {
    private static final String TAG = "ChatboxUserTypeAdapter";
    public String langName, str_ago;
    private Context mContext;
    private int mResource;
    private int lastPosition = -1;
    public ArrayList<DownloadedChatboxUserType> languageModelList;
    Date date1 = null, date2 = null;
    String str_change;


    static class ViewHolder {
        TextView holdusertype;
    }

    public ChatboxUserTypeAdapter(@NonNull Context context, int resource, @NonNull ArrayList<DownloadedChatboxUserType> objects) {
        super(context, resource, objects);
        this.mContext = context;
        mResource = resource;
        this.languageModelList = objects;
    }

    @Override
    public int getCount() {
        return languageModelList.size();
    }

    @Override
    public DownloadedChatboxUserType getItem(int position) {
        return languageModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //setupImageLoader();

        final String str_usertype = getItem(position).getType();

        int a= getCount();

        final View result;
        ChatboxUserTypeAdapter.ViewHolder holder;

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, null, false);

        holder = new ChatboxUserTypeAdapter.ViewHolder();
        holder.holdusertype =  convertView.findViewById(R.id.tv_chatbox_usertype);


        convertView.setTag(holder);

        holder = new ChatboxUserTypeAdapter.ViewHolder();
        holder.holdusertype =  convertView.findViewById(R.id.tv_chatbox_usertype);

        result = convertView;

        holder.holdusertype.setText(str_usertype);

        return convertView;
    }


    public void  setData(ArrayList<DownloadedChatboxUserType> modelList) {
        this.languageModelList = modelList;
        notifyDataSetChanged();
    }

 }
