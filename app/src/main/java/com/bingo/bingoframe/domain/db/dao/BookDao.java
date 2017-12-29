package com.bingo.bingoframe.domain.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.bingo.bingoframe.domain.db.entity.Book;

import java.util.List;

import io.reactivex.Flowable;

/**
 * @author bingo.
 * @date Create on 2017/12/29.
 * @Description
 */
@Dao
public interface BookDao {

    @Insert
    void insertBooks(List<Book> books);

    @Query("SELECT book.id, book.title, book.user_id FROM book " +
            "INNER JOIN user " +
            "ON book.user_id = user.id " +
            "WHERE user.full_name = :name")
    Flowable<List<Book>> selectBookByUserName(String name);

}
