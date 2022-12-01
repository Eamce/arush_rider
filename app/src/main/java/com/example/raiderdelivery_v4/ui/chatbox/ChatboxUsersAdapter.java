package com.example.raiderdelivery_v4.ui.chatbox;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.raiderdelivery_v4.R;
import com.example.raiderdelivery_v4.ui.global.Globalvars;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class ChatboxUsersAdapter extends ArrayAdapter<DownloadedChatboxUsers> implements Filterable {
    private static final String TAG = "ChatboxUsersAdapter";
    private Context mContext;
    private int mResource;
    private int lastPosition = -1;
    public ArrayList<DownloadedChatboxUsers> languageModelList;


    static class ViewHolder {
        TextView holdname;
        ImageView holduserimage;
        ImageView holdonlinestatus;
    }

    public ChatboxUsersAdapter(@NonNull Context context, int resource, @NonNull ArrayList<DownloadedChatboxUsers> objects) {
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
    public DownloadedChatboxUsers getItem(int position) {
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

        final String str_name = getItem(position).getName();
        final String str_login_status = getItem(position).getLogin_status();

        int a= getCount();

        final View result;
        ChatboxUsersAdapter.ViewHolder holder;

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, null, false);

        holder = new ChatboxUsersAdapter.ViewHolder();
        holder.holdname =  convertView.findViewById(R.id.tv_chatbox_name);
        holder.holduserimage =  convertView.findViewById(R.id.iv_user_image);
        holder.holdonlinestatus =  convertView.findViewById(R.id.iv_online_status);


        convertView.setTag(holder);

        holder = new ChatboxUsersAdapter.ViewHolder();
        holder.holdname =  convertView.findViewById(R.id.tv_chatbox_name);
        holder.holduserimage =  convertView.findViewById(R.id.iv_user_image);
        holder.holdonlinestatus =  convertView.findViewById(R.id.iv_online_status);

        result = convertView;

        holder.holdname.setText(str_name);
        if(str_login_status.equalsIgnoreCase("0"))
        {
            holder.holdonlinestatus.setVisibility(View.GONE);
        }

        Picasso.get()
                .load(R.drawable.ic_round_face_200)
                //.load(R.drawable.ic_broken_image_gray_24dp)
                .placeholder(R.drawable.ic_round_face_200)
                .error(R.drawable.ic_round_face_200)
                .resize(500, 500)
                .centerCrop()
                .into(holder.holduserimage);


        return convertView;
    }


    public void  setData(ArrayList<DownloadedChatboxUsers> modelList) {
        this.languageModelList = modelList;
        notifyDataSetChanged();
    }

}
