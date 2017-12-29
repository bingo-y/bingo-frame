package com.bingo.bingoframe.ui;

import android.Manifest;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
import android.os.Build;

import com.bingo.bingoframe.domain.db.entity.Book;
import com.bingo.bingoframe.domain.model.BookModel;
import com.bingo.bingoframe.domain.model.User;
import com.bingo.bingoframe.domain.model.UserModel;
import com.bingo.common.rx.RxHttpErrorFuncFlowable;
import com.bingo.library.di.scope.ActivityScope;
import com.bingo.library.mvp.BasePresenter;
import com.bingo.library.support.utils.RxLifecycleUtils;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * @author bingo.
 * @date Create on 2017/12/19.
 * @Description
 */
@ActivityScope
public class UserPresenter extends BasePresenter<UserContract.View> implements UserContract.Presenter {

    UserModel userModel;
    BookModel bookModel;
    String text;

    Disposable userdisposable;

    @Inject
    public UserPresenter(UserContract.View view, UserModel userModel, BookModel bookModel, @Named("text") String text) {
        super(view);
        this.userModel = userModel;
        this.bookModel = bookModel;
        this.text = text;
        addModel(userModel);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate() {
        Timber.d("UserPresenter onCreate");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    void onPause() {
        Timber.d("UserPresenter onPause");
    }

    @Override
    public void listUser(boolean update) {
        userdisposable = userModel.listUser(100, update)
                .onErrorResumeNext(new RxHttpErrorFuncFlowable<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mView))
                .subscribe(users -> {
                    User user = new User();
                    user.setFull_name(text);
                    users.add(user);
                    mView.onUserSuccess(users);
                }, throwable -> {
                    Timber.e(throwable);
                });
        addDispose(userdisposable);
    }

    @Override
    public void listLocalUsers() {
        userModel.listLocalUser()
                .onErrorResumeNext(new RxHttpErrorFuncFlowable<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mView))
                .subscribe(users -> {
                    mView.onLocalUserSuccess(users);
                }, throwable -> {
                    Timber.e(throwable);
                });
    }

    @Override
    public void deleteUser(int id) {
        userModel.deleteUser(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mView))
                .subscribe(() -> {
                    Timber.d("Delete " + id + "success");
                }, throwable -> {
                    Timber.e(throwable);
                });
    }

    @Override
    public void insertUser(User user) {
        userModel.insertUser(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mView))
                .subscribe(id -> {
                    mView.onInsertSuccess(id);
                }, throwable -> {
                    Timber.e(throwable);
                });
    }

    @Override
    public void insertBooks(List<Book> books) {
        bookModel.insertBooks(books)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mView))
                .subscribe(() -> {
                    mView.onMessage("插入成功");
                }, throwable -> {
                    Timber.e(throwable);
                });
    }

    @Override
    public void selectBooks(String name) {
        bookModel.selectBooks(name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mView))
                .subscribe(list -> {
                    mView.onBooksSuccess(list);
                }, throwable -> {
                    Timber.e(throwable);
                });
    }

    @Override
    public void requestExternalStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Disposable disposable = mView.getRxPermissions().requestEach(Manifest.permission.READ_EXTERNAL_STORAGE)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(permission -> {
                        if (mView != null) {
                            if (permission.granted) {
                                // `permission.name` is granted !
                                mView.permissionsGranted();
                            } else if (permission.shouldShowRequestPermissionRationale) {
                                // Denied permission without ask never again
                                mView.shouldShowRequestPermissions();
                            } else {
                                // Denied permission with ask never again
                                // Need to go to the settings
                                mView.openPermissionsInSetting();
                            }
                        }
                    }, throwable -> Timber.d(throwable));
            addDispose(disposable);
        } else {
            if (mView != null) {
                mView.permissionsGranted();
            }
        }
    }

    @Override
    public void onDestroy() {
        Timber.d("user disposable: " + userdisposable.isDisposed());
        super.onDestroy();
        Timber.d("UserPresenter onDestroy");
        userModel = null;
        bookModel = null;
    }
}
