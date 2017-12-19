package com.bingo.library.di.component;

import android.app.Application;

import com.bingo.library.base.delegate.AppDelegate;
import com.bingo.library.data.IRepositoryManager;
import com.bingo.library.data.cache.Cache;
import com.bingo.library.di.module.AppModule;
import com.bingo.library.di.module.ClientModule;
import com.bingo.library.di.module.GlobalConfigModule;
import com.google.gson.Gson;

import java.io.File;

import javax.inject.Singleton;

import dagger.Component;
import okhttp3.OkHttpClient;

/**
 * @author bingo.
 * @date Create on 2017/12/4.
 * @Description
 */
@Singleton
@Component(modules = {AppModule.class, ClientModule.class, GlobalConfigModule.class})
public interface AppComponent {

    /**
     * application
     * @return
     */
    Application application();

    /**
     * 用于管理网络请求层,以及数据缓存层
     * @return
     */
    IRepositoryManager repositoryManager();

//    //RxJava 错误处理管理类
//    RxErrorHandler rxErrorHandler();

    OkHttpClient okHttpClient();

    /**
     * gson
     * @return
     */
    Gson gson();

    /**
     * 缓存文件根目录(RxCache 和 Glide 的缓存都已经作为子文件夹放在这个根目录下),应该将所有缓存都放到这个根目录下,便于管理和清理,可在 GlobalConfigModule 里配置
     * @return
     */
    File cacheFile();

    /**
     * 用来存取一些整个App公用的数据,切勿大量存放大容量数据
     * @return
     */
    Cache<String, Object> extras();

    /**
     * 用于创建框架所需缓存对象的工厂
     * @return
     */
    Cache.Factory cacheFactory();

    void inject(AppDelegate delegate);
}
