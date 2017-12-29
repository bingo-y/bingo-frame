package com.bingo.bingoframe.domain.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

/**
 * @author bingo.
 * @date Create on 2017/12/28.
 * @Description
 */
@Entity(tableName = "user")
public class UserEntity {

    @PrimaryKey
    private int id;

    @ColumnInfo(name = "full_name")
    private String fullName;

    private int followers;

    @Embedded(prefix = "companion")
    private Companion companion;

    public UserEntity() {
    }

    @Ignore
    public UserEntity(int id, String fullName, int followers) {
        this.id = id;
        this.fullName = fullName;
        this.followers = followers;
    }

    @Ignore
    public UserEntity(int id, String fullName, int followers, Companion companion) {
        this.id = id;
        this.fullName = fullName;
        this.followers = followers;
        this.companion = companion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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
