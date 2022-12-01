package com.example.raiderdelivery_v4.ui.chatbox;

public class DownloadedChatboxUsers {
    private String name;
    private String login_status;
    private String id;

    public DownloadedChatboxUsers(String id,String name, String login_status) {
        this.id = id;
        this.name = name;
        this.login_status = login_status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin_status() {
        return login_status;
    }

    public void setLogin_status(String login_status) {
        this.login_status = login_status;
    }


}
