package com.ooowl.oowl;

public class ChattingItem2 {

    private String contents;
    private String id;
    private String time;
    private String chatid;

    public ChattingItem2(String contents, String id, String time, String chatid) {
        this.contents = contents;
        this.id = id;
        this.time = time;
        this.chatid = chatid;
    }

    public ChattingItem2() {
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
}
