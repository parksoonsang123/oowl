package com.ooowl.oowl.Notification;

public class SendData {

    private String contents;
    private String postid;
    private String chatid;
    private String senderid;


    public SendData(String contents, String postid, String chatid, String senderid) {
        this.contents = contents;
        this.postid = postid;
        this.chatid = chatid;
        this.senderid = senderid;
    }

    public String getChatid() {
        return chatid;
    }

    public void setChatid(String chatid) {
        this.chatid = chatid;
    }

    public String getSenderid() {
        return senderid;
    }

    public void setSenderid(String senderid) {
        this.senderid = senderid;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }
}
