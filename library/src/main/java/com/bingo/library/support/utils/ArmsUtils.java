package com.bingo.library.support.utils;

import android.app.Application;
import android.content.Context;

import com.bingo.library.base.App;
import com.bingo.library.di.component.AppComponent;

/**
 * @author bingo.
 * @date Create on 2017/12/5.
 * @Description 获取 ArmsComponent 来拿到框架里的一切
 * {@link com.bingo.library.di.component.AppComponent}
 */

public enum ArmsUtils {

    /**
     * 单例模式枚举实现
     */
    INSTANCE;

    /**
     * 获取 {@link AppComponent}，使用 Dagger 对外暴露的方法
     *
     * @param context Context
     * @return ArmsComponent
     */
    public AppComponent obtainArmsComponent(Context context) {
        return obtainArmsComponent((Application) context.getApplicationContext());
    }

    /**
     * 获取 {@link AppComponent}，使用 Dagger 对外暴露的方法
     *
     * @param application Application
     * @return ArmsComponent
     */
    public AppComponent obtainArmsComponent(Application application) {
        Preconditions.checkState(application instanceof App, "Application does not implements IArms");
        return ((App) application).getAppComponent();
    }

}
