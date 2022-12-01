package com.example.raiderdelivery_v4.ui.transaction.food;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raiderdelivery_v4.ui.global.Ajax;
import com.example.raiderdelivery_v4.ui.global.Globalvars;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import com.example.raiderdelivery_v4.R;

import org.json.JSONArray;
import org.json.JSONException;

public class TransactionViewItemsAdapter extends BaseExpandableListAdapter {
    Globalvars globalvars;
    String[] values;
    ListAdapter listAdapter1;
    BottomSheetDialog bottomSheetDialog;
    BottomSheetBehavior bottomSheetBehavior;
    ListView lv_addons;
    View bottomSheetView;
    Ajax mo;
    private Context context;
    ProgressDialog pd1;
    private ArrayList<DownloadedTenantData> expandableListTitle;
    ArrayList<HashMap<String, String>> mCommentList =  new ArrayList<>();
    //public static String image_link = "http://172.16.43.134:8000/storage/";

    public TransactionViewItemsAdapter(Context context, ArrayList<DownloadedTenantData> expandableListTitle) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
          ArrayList<DownloadedItemsData> items = expandableListTitle.get(listPosition).getProductList();
        return items.get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        Activity activity = (Activity) context;
        globalvars = new Globalvars(context, activity);

        final DownloadedItemsData detailInfo = (DownloadedItemsData) getChild(listPosition, expandedListPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.transaction_view_items_content, null);
        }
        ImageView prod_image = convertView.findViewById(R.id.iv_item_image);
        TextView prod_name =  convertView.findViewById(R.id.tv_view_items_product_name);
        TextView prod_desc =  convertView.findViewById(R.id.tv_view_items_product_desc);
        TextView prod_price =  convertView.findViewById(R.id.tv_view_items_product_price);
        TextView prod_qty =  convertView.findViewById(R.id.tv_view_items_product_qty);
        TextView prod_addons =  convertView.findViewById(R.id.tv_view_items_product_addons);
        LinearLayout ll_addons = convertView.findViewById(R.id.ll_view_items_product_addons);
        Button prod_subtotal =  convertView.findViewById(R.id.btn_view_items_product_subtotal);
        Button prod_view_addons =  convertView.findViewById(R.id.btn_view_items_product_addons_view);

        bottomSheetView = convertView.inflate(context, R.layout.bottomsheetdialog_layout, null);
        bottomSheetDialog = new BottomSheetDialog(context);
        lv_addons = (ListView) bottomSheetView.findViewById(R.id.lv_addons);

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetBehavior = BottomSheetBehavior.from((View) bottomSheetView.getParent());
        //bottomSheetBehavior.setBottomSheetCallback(bottomSheetCallback);

        DecimalFormat df = new DecimalFormat("#,##0.00");
        df.setMaximumFractionDigits(2);

        Picasso.get()
                .load(Globalvars.online_product_image_link + detailInfo.getImage().trim())
                //.load(R.drawable.ic_broken_image_gray_24dp)
                .placeholder(R.drawable.ic_broken_image_gray_24dp)
                .error(R.drawable.ic_broken_image_gray_24dp)
                .resize(500, 500)
                .centerCrop()
                .into(prod_image);

        if((Double.parseDouble(detailInfo.getPrice().trim()) * Double.parseDouble(detailInfo.getQty().trim())) == Double.parseDouble(detailInfo.getSubtotal().trim()))
        {
            ll_addons.setVisibility(View.GONE);
        }
        String dbl_addons = df.format(Double.parseDouble(detailInfo.getSubtotal().trim()) - Double.parseDouble(detailInfo.getPrice().trim()) * Double.parseDouble(detailInfo.getQty().trim()));

        prod_name.setText(detailInfo.getName().trim());
        prod_desc.setText(detailInfo.getDesc().trim());
        prod_price.setText("Unit Price: PHP " + df.format(Double.parseDouble(detailInfo.getPrice().trim())));
        prod_qty.setText("Qty: " + detailInfo.getQty().trim());
        prod_addons.setText("Add-on: PHP " + dbl_addons);
        prod_subtotal.setText("TOTAL: PHP " + df.format(Double.parseDouble(detailInfo.getSubtotal().trim())));

        prod_view_addons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Message: ", detailInfo.getName().trim());
                globalvars.set("tco_id", detailInfo.getId().trim());
                get_addons_breakdown(detailInfo.getName().trim(), detailInfo.getQty().trim());
            }
        });

        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        ArrayList<DownloadedItemsData> productList = expandableListTitle.get(listPosition).getProductList();
        return productList.size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.expandableListTitle.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

//        ExpandableListView mExpandableListView = (ExpandableListView) parent;
//        mExpandableListView.expandGroup(listPosition);

        DownloadedTenantData listTitle = (DownloadedTenantData) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.transaction_view_item2_list_group, null);
        }
        TextView listTitleTextView =  convertView.findViewById(R.id.listTitle);
        TextView tv_subtotal = convertView.findViewById(R.id.tv_view_items_subtotal2);
        String arr_tenant[] = listTitle.getTenant().trim().split("\\|");

        listTitleTextView.setTextSize(16);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(arr_tenant[0]);
        tv_subtotal.setTextSize(16);
        tv_subtotal.setTypeface(null, Typeface.BOLD);
        tv_subtotal.setText(arr_tenant[1]);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }

    public void get_addons_breakdown(String product_name, final String qty) {
        pd1 = new ProgressDialog(context);
        pd1.setMessage("Please wait...");
        pd1.show();
        pd1.setCancelable(false);
        pd1.setCanceledOnTouchOutside(false);
        mo = new Ajax();
        mo.setCustomObjectListener(new Ajax.MyCustomObjectListener() {
            @Override
            public void onerror() {
                pd1.dismiss();
                Toast toast = Toast.makeText(context, "Unable to connect. Please check your connection.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 80);
                toast.show();
            }

            @Override
            public void onsuccess(String data) {
                DecimalFormat df = new DecimalFormat("#,##0.00");
                df.setMaximumFractionDigits(2);
                JSONArray thedata;
                HashMap<String, String> map = new HashMap<String, String>();

                try {
                    thedata = new JSONArray(data);
                    String price, description, total;
                    values = new String[thedata.length()];
                    if (thedata.length() > 0) {
                        for (int a = 0; a < thedata.length(); a++) {
                            JSONArray row = thedata.getJSONArray(a);
                            description = row.getString(1);
                            Double dbl_price = Double.parseDouble(row.getString(0));
                            Double dbl_qty = Double.parseDouble(qty);
                            Double dbl_total = dbl_price * dbl_qty;
                            price = df.format(dbl_price);
                            total = df.format(dbl_total);

                            values[a] = description + " -> PHP " + price + " x " + qty + " = PHP " + total;

                            mCommentList.add(map);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    pd1.dismiss();

                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    bottomSheetDialog.show();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                            android.R.layout.simple_list_item_1, android.R.id.text1, values);
                    lv_addons.setAdapter(adapter);
                }
            }
        });
        String a = globalvars.get("addons_ticket");
        String b = product_name;
        String c = globalvars.get("tco_id");
        mo.adddata("ticket", globalvars.get("addons_ticket"));
        mo.adddata("product_name", product_name);
        mo.adddata("tco_id", globalvars.get("tco_id"));
        mo.execute(Globalvars.online_link + "get_addons_breakdown");
    }

}


