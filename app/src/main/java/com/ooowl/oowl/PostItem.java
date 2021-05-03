package com.ooowl.oowl;

import java.util.ArrayList;

public class PostItem {
    private String jjimcnt;
    private String title;
    private String contents;
    private String writetime;
    private String postid;
    private String userid;
    private ArrayList<String> imageurilist = new ArrayList<>();
    private ArrayList<String> imagenamelist = new ArrayList<>();
    private String price;
    private String nickname;
    private String transyesno;
    private String tradeyesno;
    private String suggest;

    public PostItem(String jjimcnt, String title, String contents, String writetime, String postid, String userid, ArrayList<String> imageurilist, ArrayList<String> imagenamelist, String price, String nickname, String transyesno, String tradeyesno, String suggest) {
        this.jjimcnt = jjimcnt;
        this.title = title;
        this.contents = contents;
        this.writetime = writetime;
        this.postid = postid;
        this.userid = userid;
        this.imageurilist = imageurilist;
        this.imagenamelist = imagenamelist;
        this.price = price;
        this.nickname = nickname;
        this.transyesno = transyesno;
        this.tradeyesno = tradeyesno;
        this.suggest = suggest;
    }

    public PostItem() {
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getJjimcnt() {
        return jjimcnt;
    }

    public void setJjimcnt(String jjimcnt) {
        this.jjimcnt = jjimcnt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getWritetime() {
        return writetime;
    }

    public void setWritetime(String writetime) {
        this.writetime = writetime;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public ArrayList<String> getImageurilist() {
        return imageurilist;
    }

    public void setImageurilist(ArrayList<String> imageurilist) {
        this.imageurilist = imageurilist;
    }

    public ArrayList<String> getImagenamelist() {
        return imagenamelist;
    }

    public void setImagenamelist(ArrayList<String> imagenamelist) {
        this.imagenamelist = imagenamelist;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTransyesno() {
        return transyesno;
    }

    public void setTransyesno(String transyesno) {
        this.transyesno = transyesno;
    }

    public String getTradeyesno() {
        return tradeyesno;
    }

    public void setTradeyesno(String tradeyesno) {
        this.tradeyesno = tradeyesno;
    }

    public String getSuggest() {
        return suggest;
    }

    public void setSuggest(String suggest) {
        this.suggest = suggest;
    }
}

