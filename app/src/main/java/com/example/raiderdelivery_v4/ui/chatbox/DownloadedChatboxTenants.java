package com.example.raiderdelivery_v4.ui.chatbox;

public class DownloadedChatboxTenants {
    String id;
    String tenant_name;

    public DownloadedChatboxTenants(String id, String tenant_name) {
        this.id = id;
        this.tenant_name = tenant_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTenant_name() {
        return tenant_name;
    }

    public void setTenant_name(String tenant_name) {
        this.tenant_name = tenant_name;
    }
}
