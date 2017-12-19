package com.bingo.common.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.bingo.library.base.App;
import com.bingo.library.base.delegate.AppDelegate;
import com.bingo.library.base.delegate.AppLifecycles;
import com.bingo.library.di.component.AppComponent;
import com.bingo.library.support.utils.Preconditions;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.support.HasSupportFragmentInjector;

/**
 * @author bingo.
 * @date Create on 2017/12/5.
 * @Description 基础application，自定义application 需要继承它，或者实现对应的方法
 */

public class BaseApplication extends Application implements App, HasActivityInjector {

    private AppLifecycles mAppDelegate;

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    /**
     * 这里会在 {@link BaseApplication#onCreate} 之前被调用,可以做一些较早的初始化
     * 常用于 MultiDex 以及插件化框架的初始化
     *
     * @param base
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        if (mAppDelegate == null) {
            this.mAppDelegate = new AppDelegate(base);
        }
        this.mAppDelegate.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (mAppDelegate != null) {
            this.mAppDelegate.onCreate(this);
        }
    }

    /**
     * 在模拟环境中程序终止时会被调用
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        if (mAppDelegate != null) {
            this.mAppDelegate.onTerminate(this);
        }
    }

    /**
     * 将 {@link AppComponent} 返回出去,供其它地方使用,{@link AppComponent} 中声明的方法所返回的实例
     * 在 {@link #getAppComponent()}拿到对象后都可以直接使用
     *
     * @return
     */
    @NonNull
    @Override
    public AppComponent getAppComponent() {
        Preconditions.checkNotNull(mAppDelegate, "%s cannot be null", AppDelegate.class.getName());
        Preconditions.checkState(mAppDelegate instanceof App, "%s must be implements %s", AppDelegate.class.getName(), App.class.getName());
        return ((App) mAppDelegate).getAppComponent();
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }
}
