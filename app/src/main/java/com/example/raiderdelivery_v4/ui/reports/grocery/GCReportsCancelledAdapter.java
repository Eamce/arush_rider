package com.example.raiderdelivery_v4.ui.reports.grocery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filterable;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.raiderdelivery_v4.R;
import com.example.raiderdelivery_v4.ui.reports.food.DownloadedReportData;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class GCReportsCancelledAdapter extends ArrayAdapter<GCDownloadedReportData> implements Filterable {
    private static final String TAG = "GCReportsCancelledAdapter";
    public String langName;
    private Context mContext;
    private int mResource;
    private int lastPosition = -1;
    public ArrayList<GCDownloadedReportData> languageModelList;
    //Double grandtotal;

    static class ViewHolder {
        TextView holdname;
        TextView holdaddress;
        TextView holdcontactno;
        TextView holdorderfrom;
        TextView holdticketno;
        TextView holdtenderedamount;
        TextView holdchange;
        TextView holdcountrider;
        TextView holdorder;
        TextView holdcharge;
        TextView holdertotalamount;
        Button holdsubtotal;
        Button holdthru;
        Button holdmainriderstat;
    }

    public GCReportsCancelledAdapter(@NonNull Context context, int resource, @NonNull ArrayList<GCDownloadedReportData> objects) {
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
    public GCDownloadedReportData getItem(int position) {
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

        final String id = getItem(position).getId();
        String name = getItem(position).getName();
        String address = getItem(position).getAddress();
        String contactno = getItem(position).getContact_no();
        String order_from = getItem(position).getOrder_from();
        String order = getItem(position).getOrder();
        String charge = getItem(position).getCharge();
        String totalamount = getItem(position).getTotalamount();
        String discount = getItem(position).getDiscount();
        String ticketno = getItem(position).getTicket_no();
        String str_tendered_amount = getItem(position).getTendered_amount();
        String str_change = getItem(position).getChange();
        String str_count_rider = getItem(position).getCount_rider();
        String str_main_rider_stat = getItem(position).getMain_rider_stat();
        String str_cancelled_status = getItem(position).getCancelled();
        String str_picking_charge = getItem(position).getPicking_charge();

        int a = getCount();

        final View result;
        GCReportsCancelledAdapter.ViewHolder holder;

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, null, false);

        holder = new GCReportsCancelledAdapter.ViewHolder();
        holder.holdname = convertView.findViewById(R.id.tv_gc_reports_name);
        holder.holdaddress =  convertView.findViewById(R.id.tv_gc_reports_address);
        holder.holdcontactno = convertView.findViewById(R.id.tv_gc_reports_contactno);
        holder.holdorderfrom = convertView.findViewById(R.id.tv_gc_reports_thru);
        holder.holdorder =  convertView.findViewById(R.id.tv_gc_reports_order);
        holder.holdertotalamount =  convertView.findViewById(R.id.tv_gc_reports_amount);
        holder.holdsubtotal =  convertView.findViewById(R.id.btn_gc_reports_subtotal);
        holder.holdticketno = convertView.findViewById(R.id.tv_gc_reports_ticketno);
        holder.holdthru = convertView.findViewById(R.id.btn_gc_transaction_type);
        holder.holdtenderedamount = convertView.findViewById(R.id.tv_gc_reports_tendered_amount);
        holder.holdchange = convertView.findViewById(R.id.tv_gc_reports_change);
        holder.holdcountrider = convertView.findViewById(R.id.tv_gc_reports_count_rider);
        holder.holdmainriderstat = convertView.findViewById(R.id.btn_gc_reports_mainriderstat);

        convertView.setTag(holder);

        result = convertView;
        DecimalFormat df = new DecimalFormat("#,##0.00");
        df.setMaximumFractionDigits(2);
        Double dbl_tendered_amount = Double.parseDouble(str_tendered_amount);
        Double dbl_change = Double.parseDouble(str_change);
        Double dbl_count_rider = Double.parseDouble(str_count_rider);
        String total_amt = df.format(Double.parseDouble(totalamount));
        String total_charges = df.format(Double.parseDouble(charge) * dbl_count_rider);
        Double dbl_total_amt = Double.parseDouble(totalamount);
        Double dbl_total_charges = Double.parseDouble(charge);
        Double dbl_picking_charge = Double.parseDouble(str_picking_charge);
        Double dbl_subtotal = dbl_total_amt + Double.parseDouble(total_charges) + dbl_picking_charge;
        Double dbl_discount;
        String str_discount;

        if(discount.equalsIgnoreCase("0.00")){
            dbl_discount = 0.00;
            str_discount = df.format(dbl_discount);
        }else{
            dbl_discount = Double.parseDouble(discount);
            str_discount = df.format(dbl_discount);
            dbl_subtotal = dbl_subtotal - dbl_discount;
        }

        holder.holdname.setText(name);
        holder.holdaddress.setText(address);
        holder.holdcontactno.setText(contactno);
        holder.holdorderfrom.setText("Thru: " + order_from);
        holder.holdticketno.setText("Ticket No: " + ticketno);
        holder.holdtenderedamount.setText("Customer Tender: PHP " + df.format(dbl_tendered_amount));
        holder.holdchange.setText("Change: PHP " + df.format(dbl_change));
        holder.holdcountrider.setText("No of Rider: " + str_count_rider);
        holder.holdertotalamount.setText("(PHP " + df.format(dbl_total_amt) + " + " + "P " + df.format(dbl_picking_charge) + " + " + "PHP " + df.format(dbl_total_charges) + ") - PHP " + str_discount);
        holder.holdsubtotal.setText("TOTAL PHP " + df.format(dbl_subtotal));

        if (str_main_rider_stat.equals("1")){
            holder.holdmainriderstat.setBackgroundResource(R.drawable.ic_star_teal_24dp);
        }else if(str_main_rider_stat.equals("0")){
            holder.holdmainriderstat.setBackgroundResource(R.drawable.ic_star_half_teal_24dp);
        }else{

        }

        if (order_from.equals("1")){
            holder.holdthru.setBackgroundResource(R.drawable.ic_call_teal_24dp);
        }else if(order_from.equals("2")){
            holder.holdthru.setBackgroundResource(R.drawable.ic_phone_android_black_24dp);
        }else{
            holder.holdthru.setBackgroundResource(R.drawable.ic_web_black_24dp);
        }

        return convertView;
    }


    public void setData(ArrayList<GCDownloadedReportData> modelList) {
        this.languageModelList = modelList;
        notifyDataSetChanged();
    }
}
