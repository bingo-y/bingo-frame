package com.bingo.library.base.delegate;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

/**
 * @author bingo.
 * @date Create on 2017/12/5.
 * @Description {@link  IActivityDelegate} 默认实现类
 */

public class ActivityDelegate implements IActivityDelegate, HasSupportFragmentInjector {

    private Activity mActivity;
    private IActivity iActivity;

    @Inject
    DispatchingAndroidInjector<Fragment> mFragmentInjector;

    public ActivityDelegate(Activity activity) {
        this.mActivity = activity;
        this.iActivity = (IActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (iActivity.useEventBus()) {
            //如果要使用eventbus请将此方法返回true
            //注册到事件主线
            EventBus.getDefault().register(mActivity);
        }
        //Dagger.Android 依赖注入
        if (iActivity.injectable()) {
            AndroidInjection.inject(mActivity);
        }
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public void onDestroy() {
        if (iActivity != null && iActivity.useEventBus()) {
            //如果要使用eventbus请将此方法返回true
            EventBus.getDefault().unregister(mActivity);
        }
        this.iActivity = null;
        this.mActivity = null;
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return this.mFragmentInjector;
    }
}
