package com.example.raiderdelivery_v4.ui.transaction.food;

public class DownloadedDiscountData {
    String id;
    String discount_type;
    String name;
    String id_num;
    String rider_status;
    String cancelled_status;
    String submit_status;
    String image_path;

    public DownloadedDiscountData(String id, String discount_type, String name, String id_num, String rider_status, String cancelled_status, String submit_status, String image_path) {
        this.id = id;
        this.discount_type = discount_type;
        this.name = name;
        this.id_num = id_num;
        this.rider_status = rider_status;
        this.cancelled_status = cancelled_status;
        this.submit_status = submit_status;
        this.image_path = image_path;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public String getSubmit_status() {
        return submit_status;
    }

    public void setSubmit_status(String submit_status) {
        this.submit_status = submit_status;
    }

    public String getCancelled_status() {
        return cancelled_status;
    }

    public void setCancelled_status(String cancelled_status) {
        this.cancelled_status = cancelled_status;
    }

    public String getRider_status() {
        return rider_status;
    }

    public void setRider_status(String rider_status) {
        this.rider_status = rider_status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDiscount_type() {
        return discount_type;
    }

    public void setDiscount_type(String discount_type) {
        this.discount_type = discount_type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId_num() {
        return id_num;
    }

    public void setId_num(String id_num) {
        this.id_num = id_num;
    }
}
