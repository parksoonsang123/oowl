package com.ooowl.oowl;

public class UsersItem {

    private String email;
    private String idToken;
    private String nickname;
    private String passward;

    public UsersItem() {
    }

    public UsersItem(String email, String idToken, String nickname, String passward) {
        this.email = email;
        this.idToken = idToken;
        this.nickname = nickname;
        this.passward = passward;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassward() {
        return passward;
    }

    public void setPassward(String passward) {
        this.passward = passward;
    }
}
