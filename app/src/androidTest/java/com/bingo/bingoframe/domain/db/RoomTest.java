package com.bingo.bingoframe.domain.db;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.bingo.bingoframe.domain.db.dao.UserDao;
import com.bingo.bingoframe.domain.db.entity.UserEntity;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author bingo.
 * @date Create on 2017/12/29.
 * @Description
 */
@RunWith(AndroidJUnit4.class)
public class RoomTest {

    private UserDao userDao;
    private AppDatabase appDatabase;

    @Before
    public void createDB() {
        Context context = InstrumentationRegistry.getTargetContext();
        //将数据库建在内存中，可以让你的测试整体更加一体化，更密闭。
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        userDao = appDatabase.userDao();
    }


    @After
    public void closeDB() {
        appDatabase.close();
    }

    @Test
    public void writeAndRead() {
        UserEntity user = new UserEntity(1, "tutu", 9);
        userDao.insertUser(user);
        UserEntity userEntity = userDao.selectUserById(1);
        Assert.assertEquals(user.getFullName(), userEntity.getFullName());
    }


}
