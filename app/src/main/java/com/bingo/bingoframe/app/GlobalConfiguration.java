package com.bingo.bingoframe.app;

import android.app.Application;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.bingo.bingoframe.BuildConfig;
import com.bingo.bingoframe.domain.http.Api;
import com.bingo.library.base.ConfigModule;
import com.bingo.library.base.delegate.AppLifecycles;
import com.bingo.library.data.http.GlobalHttpHandler;
import com.bingo.library.di.module.DatabaseModule;
import com.bingo.library.di.module.GlobalConfigModule;
import com.bingo.library.support.utils.ArmsUtils;
import com.squareup.leakcanary.RefWatcher;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.logging.HttpLoggingInterceptor;

/**
 * @author bingo.
 * @date Create on 2017/12/11.
 * @Description
 */

public class GlobalConfiguration implements ConfigModule {

    private boolean DEBUG = BuildConfig.DEBUG;

    @Override
    public void applyOptions(Context context, GlobalConfigModule.Builder builder) {
        // 设置http打印日志
        if (!DEBUG) {
            builder.printHttpLogLevel(HttpLoggingInterceptor.Level.NONE);
        } else {
            builder.printHttpLogLevel(HttpLoggingInterceptor.Level.BODY);
        }

        builder.baseurl(Api.BASE_URL)
                .globalHttpHandler(GlobalHttpHandler.EMPTY)
                .gsonConfiguration((context1, gsonBuilder) -> {
                    // 这里可以自己自定义配置Gson的参数
                    gsonBuilder
                            .serializeNulls()//支持序列化null的参数
                            .enableComplexMapKeySerialization();//支持将序列化key为object的map,默认只能序列化key为string的map
                }).retrofitConfiguration((context12, retrofitBuilder) -> {
                    // 这里可以自己自定义配置Retrofit的参数,甚至你可以替换系统配置好的okhttp对象
                }).okhttpConfiguration((context13, okhttpBuilder) -> {
                    // 这里可以自己自定义配置Okhttp的参数

                    //支持 Https,详情请百度
//                    okhttpBuilder.sslSocketFactory();
                    okhttpBuilder.writeTimeout(10, TimeUnit.SECONDS);
                }).rxCacheConfiguration((context14, rxCacheBuilder) -> {
                    //这里可以自己自定义配置 RxCache 的参数
//                    rxCacheBuilder.useExpiredDataIfLoaderNotAvailable(true);
                    // 想自定义 RxCache 的缓存文件夹或者解析方式, 如改成 fastjson, 请 return rxCacheBuilder.persistence(cacheDirectory, new FastJsonSpeaker());
                    // 否则请 return null;
                    return null;
                }).roomConfiguration((context15, builder1) -> {
                    builder1.addMigrations(MIGRATION_1_2);
                });

    }

    @Override
    public void injectAppLifecycle(Context context, List<AppLifecycles> lifecycles) {
        // AppLifecycles 的所有方法都会在基类 Application 的对应的生命周期中被调用,所以在对应的方法中可以扩展一些自己需要的逻辑
        // 可以根据不同的逻辑添加多个实现类
        lifecycles.add(new AppLifecyclesImpl());
    }

    @Override
    public void injectActivityLifecycle(Context context, List<Application.ActivityLifecycleCallbacks> lifecycles) {
        // ActivityLifecycleCallbacks 的所有方法都会在 Activity (包括三方库) 的对应的生命周期中被调用,所以在对应的方法中可以扩展一些自己需要的逻辑
        // 可以根据不同的逻辑添加多个实现类
        lifecycles.add(new ActivityLifecycleCallbacksImpl());
    }

    @Override
    public void injectFragmentLifecycle(Context context, List<FragmentManager.FragmentLifecycleCallbacks> lifecycles) {
        lifecycles.add(new FragmentManager.FragmentLifecycleCallbacks() {
            @Override
            public void onFragmentCreated(FragmentManager fm, Fragment f, Bundle savedInstanceState) {
                super.onFragmentCreated(fm, f, savedInstanceState);
                // 在配置变化的时候将这个 Fragment 保存下来,在 Activity 由于配置变化重建时重复利用已经创建的 Fragment。
                // https://developer.android.com/reference/android/app/Fragment.html?hl=zh-cn#setRetainInstance(boolean)
                // 如果在 XML 中使用 <Fragment/> 标签,的方式创建 Fragment 请务必在标签中加上 android:id 或者 android:tag 属性,否则 setRetainInstance(true) 无效
                // 在 Activity 中绑定少量的 Fragment 建议这样做,如果需要绑定较多的 Fragment 不建议设置此参数,如 ViewPager 需要展示较多 Fragment
                f.setRetainInstance(true);
            }

            @Override
            public void onFragmentDestroyed(FragmentManager fm, Fragment f) {
                super.onFragmentDestroyed(fm, f);
                ((RefWatcher) ArmsUtils
                        .INSTANCE
                        .obtainArmsComponent(f.getActivity())
                        .extras()
                        .get(RefWatcher.class.getName()))
                        .watch(f);
            }
        });
    }

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

        }
    };
}
