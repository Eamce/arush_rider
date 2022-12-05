package com.example.raiderdelivery_v4.ui.transaction.food;

public class DownloadedTransactionData {
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
    private String order_from;
    private String create_at;
    private String tendered_amount;
    private String change;
    private String change_bu;
    private String main_rider_stat;
    private String count_rider;
    private String instructions;
    private String submit_status;
    private String payment_platform;
    private String ticket_id;

    public DownloadedTransactionData(String id, String cus_id, String name, String address, String order, String charge, String totalamount, String discount, String view_stat, String ontransit, String delivered, String cancelled,String ticket_no, String contact_no, String no_pack, String order_from, String create_at, String tendered_amount, String change, String change_bu, String main_rider_stat, String count_rider, String instructions, String submit_status, String payment_platform, String ticket_id) {
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
        this.order_from = order_from;
        this.create_at = create_at;
        this.tendered_amount = tendered_amount;
        this.change = change;
        this.change_bu = change_bu;
        this.main_rider_stat = main_rider_stat;
        this.count_rider = count_rider;
        this.instructions = instructions;
        this.submit_status = submit_status;
        this.payment_platform = payment_platform;
        this.ticket_id = ticket_id;
    }

    public String getPayment_platform() {
        return payment_platform;
    }

    public void setPayment_platform(String payment_platform) {
        this.payment_platform = payment_platform;
    }

    public String getSubmit_status() {
        return submit_status;
    }

    public void setSubmit_status(String submit_status) {
        this.submit_status = submit_status;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getCus_id() {
        return cus_id;
    }

    public String getTicket_id() {
        return ticket_id;
    }

    public void setTicket_id(String ticket_id) {
        this.ticket_id = ticket_id;
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

    public String getCus_Id() {
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

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getDiscount() {
        return discount;
    }

    public void setTotalamount(String totalamount) {
        this.totalamount = totalamount;
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

    public String getOrder_from() {
        return order_from;
    }

    public void setOrder_from(String order_from) {
        this.order_from = order_from;
    }

    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
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

    public String getMain_rider_stat() {
        return main_rider_stat;
    }

    public void setMain_rider_stat(String main_rider_stat) {
        this.main_rider_stat = main_rider_stat;
    }

    public String getCount_rider() {
        return count_rider;
    }

    public void setCount_rider(String count_rider) {
        this.count_rider = count_rider;
    }
}
