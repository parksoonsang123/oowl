package com.ooowl.oowl;

public class ChatListItem {

    private String chatid;
    private String postid;
    private String myid;
    private String sellid;


    public ChatListItem(String chatid, String postid, String myid, String sellid) {
        this.chatid = chatid;
        this.postid = postid;
        this.myid = myid;
        this.sellid = sellid;
    }

    public ChatListItem() {}


    public String getChatid() {
        return chatid;
    }

    public void setChatid(String chatid) {
        this.chatid = chatid;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getMyid() {
        return myid;
    }

    public void setMyid(String myid) {
        this.myid = myid;
    }

    public String getSellid() {
        return sellid;
    }

    public void setSellid(String sellid) {
        this.sellid = sellid;
    }
}
