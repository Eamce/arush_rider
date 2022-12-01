package com.example.raiderdelivery_v4.ui.transaction.food;

import android.app.ProgressDialog;
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
import com.example.raiderdelivery_v4.ui.global.Ajax;
import com.example.raiderdelivery_v4.ui.global.Globalvars;
import com.example.raiderdelivery_v4.ui.global.Msgbox;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.Date;

public class TransactionDiscountNotTeleAdapter extends ArrayAdapter<DownloadedDiscountData> implements Filterable {
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
    TransactionDiscountNotTeleAdapter.ViewHolder holder;

    static class ViewHolder {
        TextView holdid;
        TextView holddesc;
        TextView holdname;
        TextView holdno;
        TextView holdstatus;
        ImageView holdimage;
    }

    public TransactionDiscountNotTeleAdapter(@NonNull Context context, int resource, @NonNull ArrayList<DownloadedDiscountData> objects, String ticket_id) {
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

        holder = new TransactionDiscountNotTeleAdapter.ViewHolder();
        holder.holddesc =  convertView.findViewById(R.id.tv_discount_type_not_tele);
        holder.holdname =  convertView.findViewById(R.id.tv_discount_name_not_tele);
        holder.holdno =  convertView.findViewById(R.id.tv_discount_no_not_tele);
        holder.holdimage = convertView.findViewById(R.id.iv_discount_image);

        convertView.setTag(holder);

        holder = new TransactionDiscountNotTeleAdapter.ViewHolder();
        holder.holddesc =  convertView.findViewById(R.id.tv_discount_type_not_tele);
        holder.holdname =  convertView.findViewById(R.id.tv_discount_name_not_tele);
        holder.holdno =  convertView.findViewById(R.id.tv_discount_no_not_tele);
        holder.holdimage = convertView.findViewById(R.id.iv_discount_image);

        result = convertView;

        holder.holddesc.setText("Discount Type: " + str_discount_type);
        holder.holdname.setText(str_discount_name);
        holder.holdno.setText("ID # : " + str_discount_no);

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

    public void setData(ArrayList<DownloadedDiscountData> modelList) {
        this.languageModelList = modelList;
        notifyDataSetChanged();
    }
}
