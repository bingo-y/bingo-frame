package com.bingo.library.di.module;

import android.app.Application;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;

import com.bingo.library.data.IRepositoryManager;
import com.bingo.library.data.RepositoryManager;
import com.bingo.library.data.cache.Cache;
import com.bingo.library.data.cache.CacheType;
import com.bingo.library.support.lifecycle.ActivityLifecycle;
import com.bingo.library.support.lifecycle.ActivityLifecycleForRxLifecycle;
import com.bingo.library.support.lifecycle.FragmentLifecycle;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * @author bingo.
 * @date Create on 2017/12/4.
 * @Description 提供一些框架必须的实例的 {@link Module}
 */
@Module
public abstract class AppModule {

    @Singleton
    @Provides
    static public Gson provideGson(Application application, @Nullable GsonConfiguration configuration) {
        GsonBuilder builder = new GsonBuilder();
        if (configuration != null) {
            configuration.configGson(application, builder);
        }
        return builder.create();
    }

    @Binds
    abstract IRepositoryManager bindRepositoryManager(RepositoryManager repositoryManager);

    @Singleton
    @Provides
    static public Cache<String, Object> provideExtras(Cache.Factory cacheFactory) {
        return cacheFactory.build(CacheType.EXTRAS);
    }

    @Binds
    @Named("ActivityLifecycle")
    abstract Application.ActivityLifecycleCallbacks bindActivityLifecycle(ActivityLifecycle activityLifecycle);

    @Binds
    @Named("ActivityLifecycleForRxLifecycle")
    abstract Application.ActivityLifecycleCallbacks bindActivityLifecycleForRxLifecycle(ActivityLifecycleForRxLifecycle activityLifecycleForRxLifecycle);

    @Binds
    abstract FragmentManager.FragmentLifecycleCallbacks bindFragmentLifecycle(FragmentLifecycle fragmentLifecycle);

    @Singleton
    @Provides
    static List<FragmentManager.FragmentLifecycleCallbacks> provideFragmentLifecycles(){
        return new ArrayList<>();
    }

    public interface GsonConfiguration {
        void configGson(Context context, GsonBuilder builder);
    }


}
