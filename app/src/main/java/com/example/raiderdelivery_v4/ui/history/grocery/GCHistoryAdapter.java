package com.example.raiderdelivery_v4.ui.history.grocery;

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
import com.example.raiderdelivery_v4.ui.history.food.DownloadedHistoryData;
import com.example.raiderdelivery_v4.ui.history.food.HistoryAdapter;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class GCHistoryAdapter extends ArrayAdapter<GCDownloadedHistoryData> implements Filterable {
    private static final String TAG = "HistoryAdapter";
    public String langName;
    private Context mContext;
    private int mResource;
    private int lastPosition = -1;
    public ArrayList<GCDownloadedHistoryData> languageModelList;


    static class ViewHolder {
        TextView holdname;
        TextView holdaddress;
        TextView holdcontactno;
        TextView holdcreatedat;
        TextView holdorderfrom;
        TextView holdticketno;
        TextView holdorder;
        TextView holdnumpack;
        TextView holdcharge;
        TextView holdertotalamount;
        TextView holdtenderedamount;
        TextView holdchange;
        TextView holdcountrider;
        TextView holdpaymentplatform;
        Button holdsubtotal;
        Button holdview;
        Button holdthru;
        Button holdmainriderstat;
        Button holddeliverystatus;
    }

    public GCHistoryAdapter(@NonNull Context context, int resource, @NonNull ArrayList<GCDownloadedHistoryData> objects) {
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
    public GCDownloadedHistoryData getItem(int position) {
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
        String cus_id = getItem(position).getCus_id();
        String name = getItem(position).getName();
        String address = getItem(position).getAddress();
        String contact_no = getItem(position).getContact_no();
        String order_from = getItem(position).getOrder_from();
        String order = getItem(position).getOrder();
        String charge = getItem(position).getCharge();
        String totalamount = getItem(position).getTotalamount();
        String discount = getItem(position).getDiscount();
        String view_status = getItem(position).getView_stat();
        String str_ticket_no = getItem(position).getTicket_no();
        String str_num_pack = getItem(position).getNo_pack();
        String str_ticketno = getItem(position).getTicket_no();
        String str_created_at = getItem(position).getCreated_at();
        String str_tendered_amount = getItem(position).getTendered_amount();
        String str_change = getItem(position).getChange();
        String str_change_bu = getItem(position).getChange_bu();
        String str_count_rider = getItem(position).getCount_rider();
        String str_main_rider_stat = getItem(position).getMain_rider_stat();
        String str_cancelled = getItem(position).getCancelled();
        String str_picking_charge = getItem(position).getPicking_charge();
        String str_payment_platform = getItem(position).getPayment_platform();

        int a = getCount();

        final View result;
        GCHistoryAdapter.ViewHolder holder;

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, null, false);

        holder = new GCHistoryAdapter.ViewHolder();
        holder.holdname =  convertView.findViewById(R.id.tv_gc_history_name);
        holder.holdaddress = convertView.findViewById(R.id.tv_gc_history_address);
        holder.holdcontactno = convertView.findViewById(R.id.tv_gc_history_contactno);
        holder.holdorderfrom = convertView.findViewById(R.id.tv_gc_history_thru);
        holder.holdticketno = convertView.findViewById(R.id.tv_gc_history_ticket_no);
        holder.holdorder =  convertView.findViewById(R.id.tv_gc_history_order);
        holder.holdnumpack = convertView.findViewById(R.id.tv_gc_history_num_pack);
        holder.holdertotalamount =  convertView.findViewById(R.id.tv_gc_history_amount);
        holder.holdsubtotal =  convertView.findViewById(R.id.btn_gc_history_subtotal);
        holder.holdview = convertView.findViewById(R.id.btn_gc_history_view);
        holder.holdthru = convertView.findViewById(R.id.btn_gc_history_type);
        holder.holdcreatedat = convertView.findViewById(R.id.tv_gc_history_created_at);
        holder.holdtenderedamount = convertView.findViewById(R.id.tv_gc_history_tendered_amount);
        holder.holdchange = convertView.findViewById(R.id.tv_gc_history_change);
        holder.holdcountrider = convertView.findViewById(R.id.tv_gc_history_count_rider);
        holder.holdmainriderstat = convertView.findViewById(R.id.btn_gc_history_mainriderstat);
        holder.holddeliverystatus = convertView.findViewById(R.id.btn_gc_del_status);
        holder.holdpaymentplatform = convertView.findViewById(R.id.tv_gc_history_payment_method);

        convertView.setTag(holder);

        result = convertView;
        DecimalFormat df = new DecimalFormat("#,##0.00");
        df.setMaximumFractionDigits(2);
        String total_amt = df.format(Double.parseDouble(totalamount));
        String total_charges = df.format(Double.parseDouble(charge));
        Double dbl_count_rider = Double.parseDouble(str_count_rider);
        Double dbl_total_amt = Double.parseDouble(totalamount);
        Double dbl_total_charges = Double.parseDouble(charge);
        Double dbl_picking_charge = Double.parseDouble(str_picking_charge);
        String str_total_charges = df.format(dbl_total_charges);
        Double dbl_subtotal = dbl_total_amt + dbl_total_charges + dbl_picking_charge;
        Double dbl_tendered_amount = Double.parseDouble(str_tendered_amount);
        Double dbl_change_amount = Double.parseDouble(str_change);
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

        if (dbl_change_amount > 0){
            str_change = df.format(dbl_change_amount) + str_change_bu;
        }else{
            str_change = df.format(dbl_change_amount);
        }

        holder.holdname.setText(name);
        holder.holdaddress.setText(address);
        holder.holdcontactno.setText(contact_no);
        holder.holdcreatedat.setText("Date: " + str_created_at);
        holder.holdorderfrom.setText("Thru: " + order_from);
        holder.holdticketno.setText("Ticket No: " + str_ticket_no);
        holder.holdorder.setText("Order: " + order);
        holder.holdnumpack.setText("Container Type/Qty: " + str_num_pack);
        holder.holdtenderedamount.setText("Customer Tender: PHP " + df.format(dbl_tendered_amount));
        holder.holdchange.setText("Change: PHP " + str_change);
        holder.holdcountrider.setText("No of Rider: " + str_count_rider);
        holder.holdertotalamount.setText("(P " + df.format(dbl_total_amt) + " + " + "P " + df.format(dbl_picking_charge) + " + " + "P " + df.format(dbl_total_charges) + ") - P " + str_discount);
        holder.holdsubtotal.setText("TOTAL PHP " + df.format(dbl_subtotal));
        holder.holdpaymentplatform.setText("Payment Method: " + str_payment_platform);

        holder.holdview.setTag(str_ticket_no + "|" + name + "|" + str_total_charges + "|" + cus_id + "|" + order_from);

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

        if(view_status.equals("1")){
            holder.holdview.setBackgroundResource(R.drawable.ic_view_list_teal_24dp);
        }

        if(str_cancelled.equals("1")){
            holder.holddeliverystatus.setBackgroundResource(R.drawable.ic_baseline_cancel_24);
        }

        return convertView;
    }


    public void  setData(ArrayList<GCDownloadedHistoryData> modelList) {
        this.languageModelList = modelList;
        notifyDataSetChanged();
    }
}
