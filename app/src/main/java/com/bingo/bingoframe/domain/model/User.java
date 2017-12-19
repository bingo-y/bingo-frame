package com.bingo.bingoframe.domain.model;

/**
 * @author bingo.
 * @date Create on 2017/12/14.
 * @Description
 */

public class User {


    /**
     * id : 1
     * full_name : Simon Hill
     * followers : 7484
     */

    private int id;
    private String full_name;
    private int followers;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }
}
