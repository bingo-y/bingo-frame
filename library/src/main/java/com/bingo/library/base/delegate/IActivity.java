package com.bingo.library.base.delegate;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.bingo.library.data.cache.Cache;
import com.bingo.library.data.cache.LruCache;
import com.bingo.library.di.component.AppComponent;

import org.greenrobot.eventbus.EventBus;

/**
 * @author bingo.
 * @date Create on 2017/12/4.
 * @Description 框架要求框架中的每个 {@link Activity} 都需要实现此类,以满足规范
 */

public interface IActivity {

    /**
     * 提供在 {@link Activity} 生命周期内的缓存容器, 可向此 {@link Activity} 存取一些必要的数据
     * 此缓存容器和 {@link Activity} 的生命周期绑定, 如果 {@link Activity} 在屏幕旋转或者配置更改的情况下
     * 重新创建, 那此缓存容器中的数据也会被清空, 如果你想避免此种情况请使用 {@link LifecycleModel}
     *
     * @see LifecycleModelProviders#of(FragmentActivity)
     * @return like {@link LruCache}
     */
    @NonNull
    Cache<String, Object> provideCache();

    /**
     * 是否使用 {@link EventBus}
     *
     * @return
     */
    boolean useEventBus();

    /**
     * 初始化 View,如果initView返回0,框架则不会调用{@link Activity#setContentView(int)}
     *
     * @param savedInstanceState
     * @return
     */
    int initLayout(Bundle savedInstanceState);

    /**
     * 初始化数据
     *
     * @param savedInstanceState
     */
    void initData(Bundle savedInstanceState);

    /**
     * 这个Activity是否会使用Fragment,框架会根据这个属性判断是否注册{@link FragmentManager.FragmentLifecycleCallbacks}
     * 如果返回false,那意味着这个Activity不需要绑定Fragment,那你再在这个Activity中绑定继承于 BaseFragment 的Fragment将不起任何作用
     *
     * @return
     */
    boolean useFragment();

    /**
     * Activity 是否依赖注入，如果不需要，则重写此方法，返回 false
     *
     * @return true: 进行依赖注入；false:不进行依赖注入
     */
    boolean injectable();

}
