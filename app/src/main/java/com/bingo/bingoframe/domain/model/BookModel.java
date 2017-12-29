package com.bingo.bingoframe.domain.model;

import com.bingo.bingoframe.domain.db.AppDatabase;
import com.bingo.bingoframe.domain.db.dao.BookDao;
import com.bingo.bingoframe.domain.db.entity.Book;
import com.bingo.library.data.IRepositoryManager;
import com.bingo.library.di.scope.ActivityScope;
import com.bingo.library.mvp.BaseModel;

import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.Flowable;

/**
 * @author bingo.
 * @date Create on 2017/12/29.
 * @Description
 */
@ActivityScope
public class BookModel extends BaseModel {

    BookDao bookDao;

    @Inject
    public BookModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
        bookDao = repositoryManager.obtainRoomDatabase(AppDatabase.class, AppDatabase.DB_NAME).bookDao();
    }

    public Completable insertBooks(List<Book> books) {
        return Completable.defer(() -> {
            bookDao.insertBooks(books);
            return Completable.complete();
        });
    }

    public Flowable<List<Book>> selectBooks(String userName) {
        return bookDao.selectBookByUserName(userName);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        bookDao = null;
    }
}
