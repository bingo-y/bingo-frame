package com.bingo.library.mvp;

import android.app.Activity;
import android.content.Intent;

/**
 * @author bingo.
 * Create on 2017/10/26
 * Description :
 */

public interface IView {
    /**
     * 开始加载
     */
    void startLoading();

    /**
     * 结束加载
     */
    void endLoading();
}
