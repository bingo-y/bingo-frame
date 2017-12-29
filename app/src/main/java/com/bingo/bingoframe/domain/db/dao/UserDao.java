package com.bingo.bingoframe.domain.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.bingo.bingoframe.domain.db.entity.UserEntity;

import java.util.List;

/**
 * @author bingo.
 * @date Create on 2017/12/28.
 * @Description
 */
@Dao
public interface UserDao {

    @Query("SELECT * FROM user")
    List<UserEntity> selectAll();

    @Query("SELECT * FROM user WHERE id = :id")
    UserEntity selectUserById(int id);

    @Query("DELETE FROM user")
    void deleteAll();

    @Query("DELETE FROM user WHERE id = :id")
    void deleteUser(int id);

    @Insert
    void insertUsers(UserEntity... users);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertUser(UserEntity user);

    @Delete
    void deleteUser(UserEntity user);

}
