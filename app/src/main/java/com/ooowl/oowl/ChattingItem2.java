package com.ooowl.oowl;

public class ChattingItem2 {

    private String contents;
    private String senderid;
    private String receiverid;
    private String time;
    private String chatid;
    private boolean isseen;

    public ChattingItem2(String contents, String senderid, String receiverid, String time, String chatid, boolean isseen) {
        this.contents = contents;
        this.senderid = senderid;
        this.receiverid = receiverid;
        this.time = time;
        this.chatid = chatid;
        this.isseen = isseen;
    }

    public ChattingItem2() {
    }


    public boolean isIsseen() {
        return isseen;
    }

    public void setIsseen(boolean isseen) {
        this.isseen = isseen;
    }

    public String getChatid() {
        return chatid;
    }

    public void setChatid(String chatid) {
        this.chatid = chatid;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSenderid() {
        return senderid;
    }

    public void setSenderid(String senderid) {
        this.senderid = senderid;
    }

    public String getReceiverid() {
        return receiverid;
    }

    public void setReceiverid(String receiverid) {
        this.receiverid = receiverid;
    }
}
