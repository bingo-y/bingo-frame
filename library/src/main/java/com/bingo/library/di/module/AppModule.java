package com.bingo.library.di.module;

import android.app.Application;
import android.content.Context;
import android.support.annotation.Nullable;

import com.bingo.library.data.IRepositoryManager;
import com.bingo.library.data.RepositoryManager;
import com.bingo.library.data.cache.Cache;
import com.bingo.library.data.cache.CacheType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author bingo.
 * @date Create on 2017/12/4.
 * @Description 提供一些框架必须的实例的 {@link Module}
 */
@Module
public class AppModule {

    private Application mApplication;

    public AppModule(Application application) {
        this.mApplication = application;
    }

    @Singleton
    @Provides
    public Application provideApplication() {
        return mApplication;
    }

    @Singleton
    @Provides
    public Gson provideGson(Application application, @Nullable GsonConfiguration configuration) {
        GsonBuilder builder = new GsonBuilder();
        if (configuration != null)
            configuration.configGson(application, builder);
        return builder.create();
    }

    @Singleton
    @Provides
    public IRepositoryManager provideRepositoryManager(RepositoryManager repositoryManager) {
        return repositoryManager;
    }

    @Singleton
    @Provides
    public Cache<String, Object> provideExtras(Cache.Factory cacheFactory) {
        return cacheFactory.build(CacheType.EXTRAS);
    }

    public interface GsonConfiguration {
        void configGson(Context context, GsonBuilder builder);
    }


}
