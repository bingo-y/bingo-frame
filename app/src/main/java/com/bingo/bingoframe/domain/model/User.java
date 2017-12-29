package com.bingo.bingoframe.domain.model;

import com.bingo.bingoframe.domain.db.entity.Companion;

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

    private Companion companion;

    public User() {
    }

    public User(int id, String full_name, int followers) {
        this.id = id;
        this.full_name = full_name;
        this.followers = followers;
    }

    public User(int id, String full_name, int followers, Companion companion) {
        this.id = id;
        this.full_name = full_name;
        this.followers = followers;
        this.companion = companion;
    }

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

    public Companion getCompanion() {
        return companion;
    }

    public void setCompanion(Companion companion) {
        this.companion = companion;
    }
}
