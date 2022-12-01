package com.example.raiderdelivery_v4.ui.transaction.grocery;

import com.example.raiderdelivery_v4.ui.transaction.food.DownloadedItemsData;

import java.util.ArrayList;

public class GCDownloadedStoreData {
    private String tenant;
    private ArrayList<GCDownloadedItemsData> list = new ArrayList<GCDownloadedItemsData>();


    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    public ArrayList<GCDownloadedItemsData> getProductList() {
        return list;
    }

    public void setProductList(ArrayList<GCDownloadedItemsData> productList) {
        this.list = productList;
    }
}
