package com.example.raiderdelivery_v4.ui.transaction.grocery;

public class GCDownloadedCustomerData {
    private String cus_id;
    private String fullname;
    private String mobile_no;
    private String address;
    private String landmark;

    public GCDownloadedCustomerData(String cus_id, String fullname, String mobile_no, String address, String landmark){
        this.cus_id = cus_id;
        this.fullname = fullname;
        this.mobile_no = mobile_no;
        this.address = address;
        this.landmark = landmark;
    }

    public String getCus_id(){
        return this.cus_id;
    }
    public void setCus_id(String cus_id){
        this.cus_id = cus_id;
    }
    public String getFullname(){
        return this.fullname;
    }
    public void setFullname(String fullname){
        this.fullname = fullname;
    }
    public String getMobile_no(){
        return this.mobile_no;
    }
    public void setMobile_no(String mobile_no){
        this.mobile_no = mobile_no;
    }
    public String getAddress(){
        return this.address;
    }
    public void setAddress(String address){
        this.address = address;
    }
    public String getLandmark(){
        return this.landmark;
    }
    public void setLandmark(String landmark){
        this.landmark = landmark;
    }
}
