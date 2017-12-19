package com.bingo.bingoframe.ui;

import com.bingo.bingoframe.domain.model.User;
import com.bingo.bingoframe.domain.model.UserModel;
import com.bingo.common.rx.RxFlatMapFlowable;
import com.bingo.common.rx.RxHttpErrorFuncFlowable;
import com.bingo.library.di.scope.ActivityScope;
import com.bingo.library.mvp.BasePresenter;
import com.bingo.library.support.utils.RxLifecycleUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
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

    @Inject
    public UserPresenter(UserContract.View view, UserModel userModel) {
        super(view);
        this.userModel = userModel;
        addModel(userModel);
    }

    @Override
    public void listUser(boolean update) {
        Disposable disposable = userModel.listUser(100, update)
                .onErrorResumeNext(new RxHttpErrorFuncFlowable<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mView))
                .subscribe(users -> {
                    mView.onUserSuccess(users);
                }, throwable -> {
                    Timber.e(throwable);
                });

        addDispose(disposable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        userModel = null;
    }
}
