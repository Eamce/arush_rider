package com.example.raiderdelivery_v4.ui.chatbox;

public class DownloadedChatMessages {
    private String id;
    private String contact_type_from;
    private String from_id;
    private String contact_type_to;
    private String to_id;
    private String body;
    private String attachment;
    private String remove_status;
    private String seen;
    private String seen_at;
    private String created_at;
    private String updated_at;

    public DownloadedChatMessages(String id, String contact_type_from, String from_id, String contact_type_to, String to_id, String body, String attachment, String remove_status, String seen, String seen_at, String created_at, String updated_at) {
        this.id = id;
        this.contact_type_from = contact_type_from;
        this.from_id = from_id;
        this.contact_type_to = contact_type_to;
        this.to_id = to_id;
        this.body = body;
        this.attachment = attachment;
        this.remove_status = remove_status;
        this.seen = seen;
        this.seen_at = seen_at;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContact_type_from() {
        return contact_type_from;
    }

    public void setContact_type_from(String contact_type_from) {
        this.contact_type_from = contact_type_from;
    }

    public String getFrom_id() {
        return from_id;
    }

    public void setFrom_id(String from_id) {
        this.from_id = from_id;
    }

    public String getContact_type_to() {
        return contact_type_to;
    }

    public void setContact_type_to(String contact_type_to) {
        this.contact_type_to = contact_type_to;
    }

    public String getTo_id() {
        return to_id;
    }

    public void setTo_id(String to_id) {
        this.to_id = to_id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public String getRemove_status() {
        return remove_status;
    }

    public void setRemove_status(String remove_status) {
        this.remove_status = remove_status;
    }

    public String getSeen() {
        return seen;
    }

    public void setSeen(String seen) {
        this.seen = seen;
    }

    public String getSeen_at() {
        return seen_at;
    }

    public void setSeen_at(String seen_at) {
        this.seen_at = seen_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
