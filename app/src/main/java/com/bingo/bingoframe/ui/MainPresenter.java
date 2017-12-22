package com.bingo.bingoframe.ui;

import android.Manifest;
import android.os.Build;

import com.bingo.library.di.scope.ActivityScope;
import com.bingo.library.mvp.BasePresenter;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * @author bingo.
 * @date Create on 2017/12/22.
 * @Description
 */
@ActivityScope
public class MainPresenter extends BasePresenter<MainContract.View> implements MainContract.Presenter {

    @Inject
    public MainPresenter(MainContract.View view) {
        super(view);
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
}
