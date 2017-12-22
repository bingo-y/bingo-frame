package com.bingo.bingoframe.ui;

import android.Manifest;
import android.os.Build;

import com.bingo.bingoframe.domain.model.User;
import com.bingo.bingoframe.domain.model.UserModel;
import com.bingo.common.rx.RxFlatMapFlowable;
import com.bingo.common.rx.RxHttpErrorFuncFlowable;
import com.bingo.library.di.scope.ActivityScope;
import com.bingo.library.mvp.BasePresenter;
import com.bingo.library.support.utils.RxLifecycleUtils;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

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
    String text;

    @Inject
    public UserPresenter(UserContract.View view, UserModel userModel, @Named("text") String text) {
        super(view);
        this.userModel = userModel;
        this.text = text;
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
                    User user = new User();
                    user.setFull_name(text);
                    users.add(user);
                    mView.onUserSuccess(users);
                }, throwable -> {
                    Timber.e(throwable);
                });

        addDispose(disposable);
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
        super.onDestroy();
        userModel = null;
    }
}
