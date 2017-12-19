package com.bingo.library.mvp;

import android.app.Activity;

/**
 * @author bingo.
 * @date Create on 2017/10/27.
 * @Description
 */

public interface IPresenter {

    /**
     * 做一些初始化操作
     */
    void onStart();

    /**
     * 在框架中 {@link Activity#onDestroy()} 会默认调用{@link IPresenter#onDestroy()}
     */
    void onDestroy();

}
