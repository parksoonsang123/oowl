package com.ooowl.oowl.Notification;

public class NotificationData {
    public SendData data;
    public String to;

    public NotificationData(SendData data, String to) {
        this.data = data;
        this.to = to;
    }
}
