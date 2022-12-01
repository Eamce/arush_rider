package com.example.raiderdelivery_v4.ui.transaction.food;

import java.util.ArrayList;

public class DownloadedTenantData {
    private String tenant;
    private ArrayList<DownloadedItemsData> list = new ArrayList<DownloadedItemsData>();

//    public DownloadedTenantData(String prod_tenant) {
//        this.tenant = prod_tenant;
//    }

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    public ArrayList<DownloadedItemsData> getProductList() {
        return list;
    }

    public void setProductList(ArrayList<DownloadedItemsData> productList) {
        this.list = productList;
    }
}