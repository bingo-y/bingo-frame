package com.bingo.bingoframe.domain.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.bingo.bingoframe.domain.db.dao.BookDao;
import com.bingo.bingoframe.domain.db.dao.UserDao;
import com.bingo.bingoframe.domain.db.entity.Book;
import com.bingo.bingoframe.domain.db.entity.UserEntity;

/**
 * @author bingo.
 * @date Create on 2017/12/28.
 * @Description
 */
@Database(version = 2, entities = {UserEntity.class, Book.class})
public abstract class AppDatabase extends RoomDatabase {

    public static final String DB_NAME = "bingo_frame";

    public abstract UserDao userDao();

    public abstract BookDao bookDao();

}
