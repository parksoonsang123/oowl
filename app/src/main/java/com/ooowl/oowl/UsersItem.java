package com.ooowl.oowl;

public class UsersItem {

    private String email;
    private String idToken;
    private String nickname;
    private String passward;
    private String following;
    private String follower;

    public UsersItem() {
    }

    public UsersItem(String email, String idToken, String nickname, String passward, String following, String follower) {
        this.email = email;
        this.idToken = idToken;
        this.nickname = nickname;
        this.passward = passward;
        this.following = following;
        this.follower = follower;
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
