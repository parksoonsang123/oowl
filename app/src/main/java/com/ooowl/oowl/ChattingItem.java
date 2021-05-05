package com.ooowl.oowl;

public class ChattingItem {

    private String contents;
    private String id;
    private String time;
    private String chatid;
    private int viewType;

    public ChattingItem(String contents, String id, String time, String chatid, int viewType) {
        this.contents = contents;
        this.id = id;
        this.time = time;
        this.chatid = chatid;
        this.viewType = viewType;
    }

    public ChattingItem() {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}
