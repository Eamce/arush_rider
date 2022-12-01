package com.example.raiderdelivery_v4.ui.chatbox;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.raiderdelivery_v4.R;
import com.example.raiderdelivery_v4.ui.global.Globalvars;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatboxAdapter extends ArrayAdapter<DownloadedChatMessages> implements Filterable {
    private static final String TAG = "ChatboxAdapter";
    private Context mContext;
    private int mResource;
    private int lastPosition = -1;
    public ArrayList<DownloadedChatMessages> languageModelList;
    private Globalvars globalvar;
    SimpleDateFormat simpleDateFormatIn = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
    SimpleDateFormat simpleDateFormatOut = new SimpleDateFormat("EEE, MMM d, yyyy hh:mm aaa");
    SimpleDateFormat simpleDateFormatOut2 = new SimpleDateFormat("MMM d, yyyy hh:mm aaa");
    Date date1, seen_date;
    String date2, str_seen;

    static class ViewHolder {
        TextView holdname;
        TextView holddatetime;
        TextView holdmessage;
        TextView holdid;
        TextView holdremarks;
        TextView holdremovestatus;
        TextView holdseenat;
        LinearLayout ll_message;
        LinearLayout ll_seen_at;
    }

    public ChatboxAdapter(@NonNull Context context, int resource, @NonNull ArrayList<DownloadedChatMessages> objects, Globalvars globalvars) {
        super(context, resource, objects);
        this.mContext = context;
        mResource = resource;
        this.languageModelList = objects;
        globalvar = globalvars;
    }

    @Override
    public int getCount() {
        return languageModelList.size();
    }

    @Override
    public DownloadedChatMessages getItem(int position) {
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

        String id = getItem(position).getId();
        String Contact_type_from = getItem(position).getContact_type_from();
        String From_id = getItem(position).getFrom_id();
        String Contact_type_to = getItem(position).getContact_type_to();
        String To_id = getItem(position).getTo_id();
        String Body = getItem(position).getBody();
        String Attachment = getItem(position).getAttachment();
        String remove_status = getItem(position).getRemove_status();
        String Seen = getItem(position).getSeen();
        String Seen_at = getItem(position).getSeen_at();
        String Created_at = getItem(position).getCreated_at();
        String Updated_at = getItem(position).getUpdated_at();

        int a = getCount();

        final View result;
        ChatboxAdapter.ViewHolder holder;

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, null, false);

        holder = new ChatboxAdapter.ViewHolder();
        //holder.holdname =  convertView.findViewById(R.id.tv_chatbox_name);
        holder.holddatetime =  convertView.findViewById(R.id.tv_chatbox_date);
        holder.holdmessage = convertView.findViewById(R.id.tv_chatbox_message);
        holder.holdid = convertView.findViewById(R.id.tv_chatbox_id);
        holder.holdremarks = convertView.findViewById(R.id.tv_chatbox_remarks);
        holder.holdremovestatus = convertView.findViewById(R.id.tv_chatbox_remove_status);
        holder.holdseenat = convertView.findViewById(R.id.tv_chatbox_seen_at);
        holder.ll_message = convertView.findViewById(R.id.ll_message);
        holder.ll_seen_at = convertView.findViewById(R.id.ll_seen_at);

        convertView.setTag(holder);

        //holder = new ChatboxAdapter.ViewHolder();
        //holder.holdname =  convertView.findViewById(R.id.tv_chatbox_name);
        holder.holddatetime =  convertView.findViewById(R.id.tv_chatbox_date);
        holder.holdmessage = convertView.findViewById(R.id.tv_chatbox_message);
        holder.holdid = convertView.findViewById(R.id.tv_chatbox_id);
        holder.holdremarks = convertView.findViewById(R.id.tv_chatbox_remarks);
        holder.holdremovestatus = convertView.findViewById(R.id.tv_chatbox_remove_status);
        holder.holdseenat = convertView.findViewById(R.id.tv_chatbox_seen_at);
        holder.ll_message = convertView.findViewById(R.id.ll_message);
        holder.ll_seen_at = convertView.findViewById(R.id.ll_seen_at);


        if(Contact_type_from.equalsIgnoreCase("RIDER") && globalvar.get("id").equalsIgnoreCase(From_id))
        {
            holder.holdremarks.setText("1");
            holder.holdmessage.setTextColor(Color.parseColor("#ffffff"));
            holder.ll_message.setGravity(Gravity.RIGHT);
            holder.holdmessage.setBackground(mContext.getResources().getDrawable(R.drawable.edittext_border2));
            //holder.holdmessage.setBackgroundColor(Color.parseColor("#3b8068"));

            if(Seen.equalsIgnoreCase("1"))
            {
                try {
                    seen_date = simpleDateFormatIn.parse(Seen_at);
                    str_seen = simpleDateFormatOut2.format(seen_date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                holder.holdseenat.setVisibility(View.VISIBLE);
                holder.holdseenat.setText("Seen " + str_seen);
            }
        }
        else
        {
            holder.holdremarks.setText("0");
        }

        try {
            date1 = simpleDateFormatIn.parse(Created_at);
            date2 = simpleDateFormatOut.format(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.holddatetime.setText(date2);
        if (remove_status.equalsIgnoreCase("1"))
        {
            holder.holdmessage.setText("Message has been removed.");
        }
        else
        {
            holder.holdmessage.setText(Body);
        }

        holder.holdid.setText(id);
        holder.holdremovestatus.setText(remove_status);

        return convertView;
    }


    public void  setData(ArrayList<DownloadedChatMessages> modelList) {
        this.languageModelList = modelList;
        notifyDataSetChanged();
    }

    //1 minute = 60 seconds
    //1 hour = 60 x 60 = 3600
    //1 day = 3600 x 24 = 86400
    public String printDifference(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();
        String diff = "";

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;


        //"%d days, %d hours, %d minutes, %d seconds%n
        if(elapsedDays == 0 && elapsedHours == 0 && elapsedMinutes == 0) {
            diff =  elapsedSeconds + " sec ago";
        }else if(elapsedDays == 0 && elapsedHours == 0 && elapsedMinutes > 0){
            diff = elapsedMinutes + " min ago";
        }else if(elapsedDays == 0 && elapsedHours > 0 && elapsedMinutes > 0){
            diff = elapsedHours + " hour(s) ago";
        }else{
            diff =  elapsedDays + " day(s) ago";
        }
        return diff;
    }
}
