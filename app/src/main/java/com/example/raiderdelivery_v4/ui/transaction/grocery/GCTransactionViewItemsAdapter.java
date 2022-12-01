package com.example.raiderdelivery_v4.ui.transaction.grocery;

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
import com.example.raiderdelivery_v4.R;
import com.example.raiderdelivery_v4.ui.global.Ajax;
import com.example.raiderdelivery_v4.ui.global.Globalvars;
import com.example.raiderdelivery_v4.ui.transaction.grocery.GCDownloadedItemsData;
import com.example.raiderdelivery_v4.ui.transaction.food.DownloadedTenantData;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class GCTransactionViewItemsAdapter extends BaseExpandableListAdapter {
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
    private ArrayList<GCDownloadedStoreData> expandableListTitle;
    ArrayList<HashMap<String, String>> mCommentList =  new ArrayList<>();
    //public static String image_link = "http://172.16.43.134:8000/storage/";

    public GCTransactionViewItemsAdapter(Context context, ArrayList<GCDownloadedStoreData> expandableListTitle) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        ArrayList<GCDownloadedItemsData> items = expandableListTitle.get(listPosition).getProductList();
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

        final GCDownloadedItemsData detailInfo = (GCDownloadedItemsData) getChild(listPosition, expandedListPosition);
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

        Log.e("Image",Globalvars.gc_online_product_image_link + detailInfo.getImage().trim());

        Picasso.get()
                .load(Globalvars.gc_online_product_image_link + detailInfo.getImage().trim())
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

        prod_desc.setVisibility(View.GONE);

        prod_name.setText(detailInfo.getName().trim());
        //prod_desc.setText(detailInfo.getDesc().trim());
        prod_price.setText("Unit Price: PHP " + df.format(Double.parseDouble(detailInfo.getPrice().trim())));
        prod_qty.setText("Qty: " + detailInfo.getQty().trim());
        prod_addons.setText("Add-on: PHP " + dbl_addons);
        prod_subtotal.setText("TOTAL: PHP " + df.format(Double.parseDouble(detailInfo.getSubtotal().trim())));

        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        ArrayList<GCDownloadedItemsData> productList = expandableListTitle.get(listPosition).getProductList();
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

        GCDownloadedStoreData listTitle = (GCDownloadedStoreData) getGroup(listPosition);
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


}
