package com.example.raiderdelivery_v4.ui.transaction.food;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.raiderdelivery_v4.R;
import com.example.raiderdelivery_v4.ui.global.Ajax;
import com.example.raiderdelivery_v4.ui.global.Globalvars;
import com.example.raiderdelivery_v4.ui.global.Msgbox;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class TransactionDiscountTeleAdapter extends ArrayAdapter<DownloadedDiscountData> implements Filterable {
    private static final String TAG = "TransactionDiscountTeleAdapter";
    public String langName, str_ago;
    private Context mContext;
    Msgbox msgbox;
    private int mResource;
    private int lastPosition = -1;
    public ArrayList<DownloadedDiscountData> languageModelList;
    public String ticket;
    Date date1 = null, date2 = null;
    String str_change;
    ProgressDialog pd1;
    Ajax mo;
    ViewHolder holder;

    static class ViewHolder {
        TextView holdid;
        TextView holddesc;
        TextView holdname;
        TextView holdno;
        TextView holdstatus;
        Button holdcamera;
        Button holdcomfirm;
        Button holdcancel;
        ImageView holdimage;
    }

    public TransactionDiscountTeleAdapter(@NonNull Context context, int resource, @NonNull ArrayList<DownloadedDiscountData> objects, String ticket_id) {
        super(context, resource, objects);
        this.mContext = context;
        mResource = resource;
        this.languageModelList = objects;
        ticket = ticket_id;
    }

    @Override
    public int getCount() {
        return languageModelList.size();
    }

    @Override
    public DownloadedDiscountData getItem(int position) {
        return languageModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //setupImageLoader();

        final String str_discount_type = getItem(position).getDiscount_type();
        final String str_discount_id = getItem(position).getId();
        final String str_discount_name = getItem(position).getName();
        final String str_discount_no = getItem(position).getId_num();
        final String str_rider_status = getItem(position).getRider_status();
        final String str_cancelled_status = getItem(position).getCancelled_status();
        final String str_submit_status = getItem(position).getSubmit_status();
        final String str_image_path = getItem(position).getImage_path();

        int a= getCount();
        msgbox = new Msgbox(mContext);
        final View result;

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, null, false);

        holder = new ViewHolder();
        holder.holddesc =  convertView.findViewById(R.id.tv_discount_type);
        holder.holdname =  convertView.findViewById(R.id.tv_discount_name);
        holder.holdno =  convertView.findViewById(R.id.tv_discount_no);
        holder.holdstatus =  convertView.findViewById(R.id.tv_status);
        holder.holdcamera =  convertView.findViewById(R.id.btn_discount_camera);
        holder.holdcomfirm = convertView.findViewById(R.id.btn_discount_confirm);
        holder.holdcancel = convertView.findViewById(R.id.btn_discount_cancel);
        holder.holdimage = convertView.findViewById(R.id.iv_discount_image);

        convertView.setTag(holder);

        holder = new ViewHolder();
        holder.holddesc =  convertView.findViewById(R.id.tv_discount_type);
        holder.holdname =  convertView.findViewById(R.id.tv_discount_name);
        holder.holdno =  convertView.findViewById(R.id.tv_discount_no);
        holder.holdstatus =  convertView.findViewById(R.id.tv_status);
        holder.holdcamera =  convertView.findViewById(R.id.btn_discount_camera);
        holder.holdcomfirm = convertView.findViewById(R.id.btn_discount_confirm);
        holder.holdcancel = convertView.findViewById(R.id.btn_discount_cancel);
        holder.holdimage = convertView.findViewById(R.id.iv_discount_image);

        result = convertView;

        holder.holddesc.setText("Discount Type: " + str_discount_type);
        holder.holdname.setText(str_discount_name);
        holder.holdno.setText("ID # : " + str_discount_no);
        if(str_rider_status.equalsIgnoreCase("1") && str_cancelled_status.equalsIgnoreCase("0"))
        {
            holder.holdstatus.setText("Status: Confirmed");
        }
        else if(str_rider_status.equalsIgnoreCase("1") && str_cancelled_status.equalsIgnoreCase("1"))
        {
            holder.holdstatus.setText("Status: Cancelled");
        }
        else
        {
            holder.holdstatus.setText("Status: Pending");
        }

        holder.holdcomfirm.setTag(str_discount_id + "|" + str_discount_type);
        holder.holdcamera.setTag(str_discount_id + "|" + str_discount_type);
        holder.holdcancel.setTag(str_discount_id + "|" + str_discount_type);

        //Hide Confirm or Cancel Button if Confirm or Cancel Button clicked...
        if(str_rider_status.equalsIgnoreCase("1") && str_cancelled_status.equalsIgnoreCase("0") && str_submit_status.equalsIgnoreCase("0"))
        {
            holder.holdcomfirm.setVisibility(View.GONE);
        }
        else if(str_rider_status.equalsIgnoreCase("1") && str_cancelled_status.equalsIgnoreCase("1") && str_submit_status.equalsIgnoreCase("0"))
        {
            holder.holdcancel.setVisibility(View.GONE);
        }
        else if((str_rider_status.equalsIgnoreCase("1") && str_cancelled_status.equalsIgnoreCase("1")) || str_submit_status.equalsIgnoreCase("1"))
        {
            holder.holdcomfirm.setVisibility(View.GONE);
            holder.holdcancel.setVisibility(View.GONE);
            holder.holdcamera.setVisibility(View.GONE);
        }
        else if((str_rider_status.equalsIgnoreCase("1") && str_cancelled_status.equalsIgnoreCase("0")) || str_submit_status.equalsIgnoreCase("1"))
        {
            holder.holdcomfirm.setVisibility(View.GONE);
            holder.holdcancel.setVisibility(View.GONE);
            holder.holdcamera.setVisibility(View.GONE);
        }

        Picasso.get()
                .load(Globalvars.online_discount_image_link  + str_image_path)
                //.load(R.drawable.ic_broken_image_gray_24dp)
                .placeholder(R.drawable.ic_broken_image_gray_24dp)
                .error(R.drawable.ic_broken_image_gray_24dp)
                .resize(500, 500)
                .centerCrop()
                .into(holder.holdimage);

        return convertView;
    }

    public void  setData(ArrayList<DownloadedDiscountData> modelList) {
        this.languageModelList = modelList;
        notifyDataSetChanged();
    }
}
