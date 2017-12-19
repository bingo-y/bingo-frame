package com.bingo.bingoframe.ui;

import com.bingo.bingoframe.domain.model.User;
import com.bingo.library.mvp.IPresenter;
import com.bingo.library.mvp.IView;

import java.util.List;

/**
 * @author bingo.
 * @date Create on 2017/12/18.
 * @Description
 */

public interface UserContract {

    interface View extends IView {
        void onUserSuccess(List<User> users);
    }

    interface Presenter extends IPresenter {
        void listUser(boolean update);
    }

}
