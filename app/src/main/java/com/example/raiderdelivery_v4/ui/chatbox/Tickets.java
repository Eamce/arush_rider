package com.example.raiderdelivery_v4.ui.chatbox;

public class Tickets {

private String ticketname;
private String id;


    public Tickets( String id, String ticketname) {
        this.id = id;
        this.ticketname = ticketname;
    }

    public String getName() {
        return ticketname;
    }

    public void setName(String ticketname) {
        this.ticketname = ticketname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

