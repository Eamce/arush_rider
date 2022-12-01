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

public class ChatboxTenantsAdapter extends ArrayAdapter<DownloadedChatboxTenants> implements Filterable {
    private static final String TAG = "ChatboxTenantsAdapter";
    public String langName, str_ago;
    private Context mContext;
    private int mResource;
    public ArrayList<DownloadedChatboxTenants> languageModelList;



    static class ViewHolder {
        TextView holdtenantname;
        TextView holdtenantid;
    }

    public ChatboxTenantsAdapter(@NonNull Context context, int resource, @NonNull ArrayList<DownloadedChatboxTenants> objects) {
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
    public DownloadedChatboxTenants getItem(int position) {
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

        final String str_id = getItem(position).getId();
        final String str_tenant_name = getItem(position).getTenant_name();

        int a= getCount();

        final View result;
        ChatboxTenantsAdapter.ViewHolder holder;

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, null, false);

        holder = new ChatboxTenantsAdapter.ViewHolder();
        holder.holdtenantname =  convertView.findViewById(R.id.tv_chatbox_tenants);


        convertView.setTag(holder);

        holder = new ChatboxTenantsAdapter.ViewHolder();
        holder.holdtenantname =  convertView.findViewById(R.id.tv_chatbox_tenants);
        holder.holdtenantid =  convertView.findViewById(R.id.tv_chatbox_id);

        result = convertView;

        holder.holdtenantname.setText(str_tenant_name);
        holder.holdtenantid.setText(str_id);

        return convertView;
    }


    public void  setData(ArrayList<DownloadedChatboxTenants> modelList) {
        this.languageModelList = modelList;
        notifyDataSetChanged();
    }

}
