package com.ooowl.oowl;

//사용자 계정 정보 모델 클래스

public class UserAccount {
    private String idToken; //Firebase Uid
    private String Email;
    private String Password;
    private String Nickname;
    private String following;
    private String follower;
    private String login;
    private String alram;

    public UserAccount() {  }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getAlram() {
        return alram;
    }

    public void setAlram(String alram) {
        this.alram = alram;
    }

    public String getFollowing() {
        return following;
    }

    public void setFollowing(String following) {
        this.following = following;
    }

    public String getFollower() {
        return follower;
    }

    public void setFollower(String follower) {
        this.follower = follower;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getNickname() {
        return Nickname;
    }

    public void setNickname(String nickname) {
        Nickname = nickname;
    }
}