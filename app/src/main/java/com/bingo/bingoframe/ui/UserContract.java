package com.bingo.bingoframe.ui;

import android.app.Activity;

import com.bingo.bingoframe.domain.model.User;
import com.bingo.library.mvp.IPresenter;
import com.bingo.library.mvp.IView;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.List;

/**
 * @author bingo.
 * @date Create on 2017/12/18.
 * @Description
 */

public interface UserContract {

    interface View extends IView, PermissionCallback {
        void onUserSuccess(List<User> users);

        RxPermissions getRxPermissions();

        Activity getActivity();
    }

    interface Presenter extends IPresenter {

        void listUser(boolean update);

        /**
         * 外部存储权限
         */
        void requestExternalStorage();
    }

}
