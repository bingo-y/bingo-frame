package com.bingo.bingoframe.ui;

import android.app.Activity;

import com.bingo.bingoframe.domain.model.User;
import com.bingo.library.mvp.IPresenter;
import com.bingo.library.mvp.IView;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.List;

/**
 * @author bingo.
 * @date Create on 2017/12/22.
 * @Description
 */

public interface MainContract {

    interface View extends IView, PermissionCallback {
        RxPermissions getRxPermissions();

        Activity getActivity();
    }

    interface Presenter extends IPresenter {

        /**
         * 外部存储权限
         */
        void requestExternalStorage();
    }

}
