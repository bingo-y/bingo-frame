package com.bingo.library.base;

import android.support.annotation.NonNull;

import com.bingo.library.di.component.AppComponent;

/**
 * @author bingo.
 * @date Create on 2017/12/5.
 * @Description 框架要求框架中的每个 {@link android.app.Application} 都需要实现此类,以满足规范
 */

public interface App {

    @NonNull
    AppComponent getAppComponent();

}
