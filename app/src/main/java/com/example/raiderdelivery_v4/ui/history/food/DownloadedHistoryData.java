package com.example.raiderdelivery_v4.ui.history.food;

public class DownloadedHistoryData {
    private String id;
    private String cus_id;
    private String name;
    private String address;
    private String order;
    private String charge;
    private String totalamount;
    private String discount;
    private String view_stat;
    private String ontransit;
    private String delivered;
    private String cancelled;
    private String ticket_no;
    private String contact_no;
    private String no_pack;
    private String created_at;
    private String order_from;
    private String tendered_amount;
    private String change;
    private String change_bu;
    private String count_rider;
    private String main_rider_stat;
    private String payment_platform;

    public DownloadedHistoryData(String id, String cus_id, String name, String address, String order, String charge, String totalamount, String discount, String view_stat, String ontransit, String delivered, String cancelled, String ticket_no, String contact_no, String no_pack, String created_at, String order_from, String tendered_amount, String change, String change_bu, String count_rider, String main_rider_stat, String payment_platform) {
        this.id = id;
        this.cus_id = cus_id;
        this.name = name;
        this.address = address;
        this.order = order;
        this.charge = charge;
        this.totalamount = totalamount;
        this.discount = discount;
        this.view_stat = view_stat;
        this.ontransit = ontransit;
        this.delivered = delivered;
        this.cancelled = cancelled;
        this.ticket_no = ticket_no;
        this.contact_no = contact_no;
        this.no_pack = no_pack;
        this.created_at = created_at;
        this.order_from = order_from;
        this.tendered_amount = tendered_amount;
        this.change = change;
        this.change_bu = change_bu;
        this.count_rider = count_rider;
        this.main_rider_stat = main_rider_stat;
        this.payment_platform = payment_platform;
    }

    public String getPayment_platform() {
        return payment_platform;
    }

    public void setPayment_platform(String payment_platform) {
        this.payment_platform = payment_platform;
    }

    public String getCancelled() {
        return cancelled;
    }

    public void setCancelled(String cancelled) {
        this.cancelled = cancelled;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCus_id() {
        return cus_id;
    }

    public void setCus_id(String cus_id) {
        this.cus_id = cus_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }

    public String getTotalamount() {
        return totalamount;
    }

    public void setTotalamount(String totalamount) {
        this.totalamount = totalamount;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getView_stat() {
        return view_stat;
    }

    public void setView_stat(String view_stat) {
        this.view_stat = view_stat;
    }

    public String getOntransit() {
        return ontransit;
    }

    public void setOntransit(String ontransit) {
        this.ontransit = ontransit;
    }

    public String getDelivered() {
        return delivered;
    }

    public void setDelivered(String delivered) {
        this.delivered = delivered;
    }

    public String getTicket_no() {
        return ticket_no;
    }

    public void setTicket_no(String ticket_no) {
        this.ticket_no = ticket_no;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public String getNo_pack() {
        return no_pack;
    }

    public void setNo_pack(String no_pack) {
        this.no_pack = no_pack;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getOrder_from() {
        return order_from;
    }

    public void setOrder_from(String order_from) {
        this.order_from = order_from;
    }

    public String getTendered_amount() {
        return tendered_amount;
    }

    public void setTendered_amount(String tendered_amount) {
        this.tendered_amount = tendered_amount;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public String getChange_bu() {
        return change_bu;
    }

    public void setChange_bu(String change_bu) {
        this.change_bu = change_bu;
    }

    public String getCount_rider() {
        return count_rider;
    }

    public void setCount_rider(String count_rider) {
        this.count_rider = count_rider;
    }

    public String getMain_rider_stat() {
        return main_rider_stat;
    }

    public void setMain_rider_stat(String main_rider_stat) {
        this.main_rider_stat = main_rider_stat;
    }
}
