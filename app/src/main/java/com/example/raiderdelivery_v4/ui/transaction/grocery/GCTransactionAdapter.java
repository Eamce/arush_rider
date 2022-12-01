package com.example.raiderdelivery_v4.ui.transaction.grocery;

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
import com.example.raiderdelivery_v4.ui.transaction.food.DownloadedTransactionData;
import com.example.raiderdelivery_v4.ui.transaction.food.TransactionAdapter;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class GCTransactionAdapter extends ArrayAdapter<GCDownloadedTransactionData> implements Filterable {
    private static final String TAG = "TransactionAdapter";
    public String langName, str_ago;
    private Context mContext;
    private int mResource;
    private int lastPosition = -1;
    public ArrayList<GCDownloadedTransactionData> languageModelList;
    Date date1 = null, date2 = null;
    String str_change;


    static class ViewHolder {
        TextView holdname;
        TextView holdaddress;
        TextView holdcontactno;
        TextView holdthru;
        TextView holdticketno;
        TextView holdorder;
        TextView holdnopack;
        TextView holdcountrider;
        TextView holdtenderedamount;
        TextView holdchange;
        TextView holdcharge;
        TextView holdertotalamount;
        TextView holdercreated_at;
        TextView holdinstructions;
        TextView holdpaymentplatform;
        Button holddontransit;
        Button holddelivered;
        Button holdsubtotal;
        Button holdview;
        Button holdorderfrom;
        Button holdmainriderstat;
    }

    public GCTransactionAdapter(@NonNull Context context, int resource, @NonNull ArrayList<GCDownloadedTransactionData> objects) {
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
    public GCDownloadedTransactionData getItem(int position) {
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
        String cus_id = getItem(position).getCus_Id();
        String name = getItem(position).getName();
        String address = getItem(position).getAddress();
        String contact_no = getItem(position).getContact_no();
        String thru = getItem(position).getOrder_from();
        String ticket_no = getItem(position).getTicket_no();
        String order = getItem(position).getOrder();
        String num_pack = getItem(position).getNo_pack();
        String str_tendered_amount = getItem(position).getTendered_amount();
        String str_change_amount = getItem(position).getChange();
        String str_change_bu = getItem(position).getChange_bu();
        String charge = getItem(position).getCharge();
        String totalamount = getItem(position).getTotalamount();
        String discount = getItem(position).getDiscount();
        String view_stat = getItem(position).getView_stat();
        String str_ontransit = getItem(position).getOntransit();
        String str_delivered = getItem(position).getDelivered();
        String str_cancelled = getItem(position).getCancelled();
        String str_ticket_no = getItem(position).getTicket_no();
        String order_from = getItem(position).getOrder_from();
        String created_at = getItem(position).getCreate_at();
        String str_main_rider_stat = getItem(position).getMain_rider_stat();
        String count_rider = getItem(position).getCount_rider();
        String str_instructions = getItem(position).getInstructions();
        String str_submit_status = getItem(position).getSubmit_status();
        String str_picking_charge = getItem(position).getPicking_charge();
        String str_payment_platform = getItem(position).getPayment_platform();

        int a= getCount();

        final View result;
        GCTransactionAdapter.ViewHolder holder;

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, null, false);

        holder = new GCTransactionAdapter.ViewHolder();
        holder.holdname =  convertView.findViewById(R.id.tv_gc_name);
        holder.holdaddress =  convertView.findViewById(R.id.tv_gc_address);
        holder.holdcontactno = convertView.findViewById(R.id.tv_gc_contact_no);
        holder.holdthru = convertView.findViewById(R.id.tv_gc_thru);
        holder.holdticketno = convertView.findViewById(R.id.tv_gc_ticket_no);
        holder.holdorder =  convertView.findViewById(R.id.tv_gc_order);
        holder.holdnopack =  convertView.findViewById(R.id.tv_gc_num_pack);
        holder.holdcountrider = convertView.findViewById(R.id.tv_gc_count_rider);
        holder.holdtenderedamount = convertView.findViewById(R.id.tv_gc_tendered_amount);
        holder.holdchange = convertView.findViewById(R.id.tv_gc_change);
        holder.holdertotalamount =  convertView.findViewById(R.id.tv_gc_amount);
        holder.holddontransit =  convertView.findViewById(R.id.btn_gc_ontransit);
        holder.holddelivered =  convertView.findViewById(R.id.btn_gc_delivered);
        holder.holdsubtotal =  convertView.findViewById(R.id.btn_gc_subtotal);
        holder.holdview =  convertView.findViewById(R.id.btn_gc_view);
        holder.holdorderfrom = convertView.findViewById(R.id.btn_gc_transaction_type);
        holder.holdercreated_at = convertView.findViewById(R.id.tv_gc_created_at);
        holder.holdmainriderstat = convertView.findViewById(R.id.btn_gc_main_rider_stat);
        holder.holdinstructions = convertView.findViewById(R.id.tv_gc_instructions);
        holder.holdpaymentplatform = convertView.findViewById(R.id.tv_gc_payment_method);

        convertView.setTag(holder);

        holder = new GCTransactionAdapter.ViewHolder();
        holder.holdname =  convertView.findViewById(R.id.tv_gc_name);
        holder.holdaddress =  convertView.findViewById(R.id.tv_gc_address);
        holder.holdcontactno = convertView.findViewById(R.id.tv_gc_contact_no);
        holder.holdthru = convertView.findViewById(R.id.tv_gc_thru);
        holder.holdticketno = convertView.findViewById(R.id.tv_gc_ticket_no);
        holder.holdorder =  convertView.findViewById(R.id.tv_gc_order);
        holder.holdnopack =  convertView.findViewById(R.id.tv_gc_num_pack);
        holder.holdcountrider = convertView.findViewById(R.id.tv_gc_count_rider);
        holder.holdtenderedamount = convertView.findViewById(R.id.tv_gc_tendered_amount);
        holder.holdchange = convertView.findViewById(R.id.tv_gc_change);
        holder.holdertotalamount =  convertView.findViewById(R.id.tv_gc_amount);
        holder.holddontransit =  convertView.findViewById(R.id.btn_gc_ontransit);
        holder.holddelivered =  convertView.findViewById(R.id.btn_gc_delivered);
        holder.holdsubtotal =  convertView.findViewById(R.id.btn_gc_subtotal);
        holder.holdview =  convertView.findViewById(R.id.btn_gc_view);
        holder.holdorderfrom = convertView.findViewById(R.id.btn_gc_transaction_type);
        holder.holdercreated_at = convertView.findViewById(R.id.tv_gc_created_at);
        holder.holdmainriderstat = convertView.findViewById(R.id.btn_gc_main_rider_stat);
        holder.holdinstructions = convertView.findViewById(R.id.tv_gc_instructions);
        holder.holdpaymentplatform = convertView.findViewById(R.id.tv_gc_payment_method);

        result = convertView;
        DecimalFormat df = new DecimalFormat("#,##0.00");
        df.setMaximumFractionDigits(2);
        String total_amt = df.format(Double.parseDouble(totalamount));
        Double dbl_total_amt = Double.parseDouble(totalamount);
        Double dbl_count_rider = Double.parseDouble(count_rider);
        Double dbl_total_charges = Double.parseDouble(charge);
        Double dbl_picking_charge = Double.parseDouble(str_picking_charge);
        String str_total_charges = df.format(dbl_total_charges);
        Double dbl_subtotal;
        dbl_subtotal = dbl_total_amt + dbl_total_charges + dbl_picking_charge;
        Double dbl_tendered_amount = Double.parseDouble(str_tendered_amount);
        Double dbl_change_amount = Double.parseDouble(str_change_amount);
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

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
        Date currentTime = Calendar.getInstance().getTime();
        try {
            date1 = simpleDateFormat.parse(created_at);
            //date2 = simpleDateFormat.parse(String.valueOf(currentTime));
            str_ago = printDifference(date1, currentTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (dbl_change_amount > 0){
            str_change = df.format(dbl_change_amount) + str_change_bu;
        }else{
            str_change = df.format(dbl_change_amount);
        }

        holder.holdercreated_at.setText(str_ago);
        holder.holdname.setText(name);
        holder.holdaddress.setText(address);
        holder.holdcontactno.setText(contact_no);
        holder.holdthru.setText("Thru: " + thru);
        holder.holdticketno.setText("Ticket No: " + ticket_no);
        holder.holdorder.setText("Order: " + order);
        holder.holdnopack.setText("Container Type/Qty: " + num_pack);
        holder.holdcountrider.setText("No of Rider: " + count_rider);
        holder.holdtenderedamount.setText("Customer Tender: PHP " + df.format(dbl_tendered_amount));
        holder.holdchange.setText("Change: PHP " + str_change);
        //holder.holdchange.setText("Change: PHP " + dbl_change_amount);
        holder.holdertotalamount.setText("(P " + df.format(dbl_total_amt) + " + " + "P " + df.format(dbl_picking_charge) + " + " + "P " + df.format(dbl_total_charges) + ") - P " + str_discount);
        holder.holdsubtotal.setText("TOTAL PHP " + df.format(dbl_subtotal));
        holder.holddontransit.setTag(str_ticket_no);
        holder.holddelivered.setTag(str_ticket_no + "|" + str_submit_status);
        holder.holdname.setTag(name);
        holder.holdview.setTag(str_ticket_no + "|" + name + "|" + str_total_charges + "|" + cus_id + "|" + order_from + "|" + str_picking_charge);
        holder.holdinstructions.setText("Instructions: "+str_instructions);
        holder.holdpaymentplatform.setText("Payment Method: "+str_payment_platform);

        if (str_main_rider_stat.equals("1")){
            holder.holdmainriderstat.setBackgroundResource(R.drawable.ic_star_teal_24dp);
        }else{
            holder.holdmainriderstat.setBackgroundResource(R.drawable.ic_star_half_teal_24dp);
        }

        if (thru.equals("1")){
            holder.holdorderfrom.setBackgroundResource(R.drawable.ic_call_teal_24dp);
        }else if(thru.equals("2")){
            holder.holdorderfrom.setBackgroundResource(R.drawable.ic_phone_android_black_24dp);
        }else{
            holder.holdorderfrom.setBackgroundResource(R.drawable.ic_web_black_24dp);
        }

        if (str_ontransit.equals("1")) {
            holder.holddontransit.setBackgroundResource(R.drawable.ic_local_shipping_teal_24dp);
            holder.holddontransit.setEnabled(false);
        }else{
            holder.holddelivered.setEnabled(false);
        }
        if (str_delivered.equals("1") && str_cancelled.equals("0")) {
            holder.holddelivered.setBackgroundResource(R.drawable.ic_check_circle_teal_24dp);
            holder.holddelivered.setEnabled(false);
        }else if(str_delivered.equals("1") && str_cancelled.equals("1")) {
            holder.holddelivered.setBackgroundResource(R.drawable.ic_baseline_cancel_24);
            holder.holddelivered.setEnabled(false);
        }else{

        }
        if (view_stat.equals("1")) {
            holder.holdview.setBackgroundResource(R.drawable.ic_view_list_teal_24dp);
        }

        return convertView;
    }


    public void  setData(ArrayList<GCDownloadedTransactionData> modelList) {
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
