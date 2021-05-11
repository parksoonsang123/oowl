package com.ooowl.oowl;

public class FollowItem {

    private String follow;
    private String follower;



    public FollowItem(String follow,String follower) {

        this.follow = follow;
        this.follower = follower;
    }

    public FollowItem() {
    }

    public String getFollow() {
        return follow;
    }

    public void setFollow(String follow) {
        this.follow = follow;
    }

    public String getFollower() {
        return follower;
    }

    public void setFollower(String follower) {
        this.follower = follower;
    }
}
