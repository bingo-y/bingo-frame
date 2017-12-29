package com.bingo.library.di.module;

import android.app.Application;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.bingo.library.data.cache.Cache;
import com.bingo.library.data.cache.CacheType;
import com.bingo.library.data.cache.LruCache;
import com.bingo.library.data.http.BaseUrl;
import com.bingo.library.data.http.GlobalHttpHandler;
import com.bingo.library.support.utils.FileUtils;
import com.bingo.library.support.utils.ThreadPoolUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * @author bingo.
 * @date Create on 2017/11/2.
 * @Description
 */
@Module
public class GlobalConfigModule {

    private static final long DEFAULT_ALIVE_TIME =  60;

    private HttpUrl mApiUrl;
    private BaseUrl mBaseUrl;
    private GlobalHttpHandler mHandler;
    private List<Interceptor> mInterceptors;
//    private ResponseErrorListener mErrorListener;
    private File mCacheFile;
    private ClientModule.RetrofitConfiguration mRetrofitConfiguration;
    private ClientModule.OkhttpConfiguration mOkhttpConfiguration;
    private ClientModule.RxCacheConfiguration mRxCacheConfiguration;
    private AppModule.GsonConfiguration mGsonConfiguration;
    private HttpLoggingInterceptor.Level mPrintHttpLogLevel;
    private Cache.Factory mCacheFactory;
    private DatabaseModule.RoomConfiguration mRoomConfiguration;
    private Executor mExecutor;

    private GlobalConfigModule(Builder builder) {
        this.mApiUrl = builder.apiUrl;
        this.mBaseUrl = builder.baseUrl;
        this.mHandler = builder.handler;
        this.mInterceptors = builder.interceptors;
//        this.mErrorListener = builder.responseErrorListener;
        this.mCacheFile = builder.cacheFile;
        this.mRetrofitConfiguration = builder.retrofitConfiguration;
        this.mOkhttpConfiguration = builder.okhttpConfiguration;
        this.mRxCacheConfiguration = builder.rxCacheConfiguration;
        this.mGsonConfiguration = builder.gsonConfiguration;
        this.mPrintHttpLogLevel = builder.printHttpLogLevel;
        this.mCacheFactory = builder.cacheFactory;
        this.mRoomConfiguration = builder.roomConfiguration;
    }

    public static Builder builder() {
        return new Builder();
    }


    @Singleton
    @Provides
    @Nullable
    List<Interceptor> provideInterceptors() {
        return mInterceptors;
    }


    /**
     * 提供 BaseUrl,默认使用 <"https://api.github.com/">
     *
     * @return
     */
    @Singleton
    @Provides
    HttpUrl provideBaseUrl() {
        if (mBaseUrl != null) {
            HttpUrl httpUrl = mBaseUrl.url();
            if (httpUrl != null) {
                return httpUrl;
            }
        }
        return mApiUrl == null ? HttpUrl.parse("https://api.github.com/") : mApiUrl;
    }


    /**
     * 提供处理 Http 请求和响应结果的处理类
     *
     * @return
     */
    @Singleton
    @Provides
    @Nullable
    GlobalHttpHandler provideGlobalHttpHandler() {
        return mHandler;
    }


    /**
     * 提供缓存文件
     */
    @Singleton
    @Provides
    File provideCacheFile(Application application) {
        try {
            return mCacheFile == null ? FileUtils.getAppFileInExt(application, FileUtils.AppDirTypeInExt.CACHE) : mCacheFile;
        } catch (FileUtils.NotFoundExternalSD notFoundExternalSD) {
            notFoundExternalSD.printStackTrace();
        }
        return null;
    }


    /**
     * 提供处理 RxJava 错误的管理器的回调
     *
     * @return
     */
//    @Singleton
//    @Provides
//    ResponseErrorListener provideResponseErrorListener() {
//        return mErrorListener == null ? ResponseErrorListener.EMPTY : mErrorListener;
//    }


    @Singleton
    @Provides
    @Nullable
    ClientModule.RetrofitConfiguration provideRetrofitConfiguration() {
        return mRetrofitConfiguration;
    }

    @Singleton
    @Provides
    @Nullable
    ClientModule.OkhttpConfiguration provideOkhttpConfiguration() {
        return mOkhttpConfiguration;
    }

    @Singleton
    @Provides
    @Nullable
    ClientModule.RxCacheConfiguration provideRxCacheConfiguration() {
        return mRxCacheConfiguration;
    }

    @Singleton
    @Provides
    @Nullable
    AppModule.GsonConfiguration provideGsonConfiguration() {
        return mGsonConfiguration;
    }

    @Singleton
    @Provides
    HttpLoggingInterceptor.Level providePrintHttpLogLevel() {
        return mPrintHttpLogLevel == null?
                HttpLoggingInterceptor.Level.NONE : mPrintHttpLogLevel;
    }

    @Singleton
    @Provides
    Cache.Factory provideCacheFactory(Application application) {
        return mCacheFactory == null ? type -> {
            //若想自定义 LruCache 的 size, 或者不想使用 LruCache , 想使用自己自定义的策略
            //并使用 GlobalConfigModule.Builder#cacheFactory() 扩展
            return new LruCache(type.calculateCacheSize(application));
        } : mCacheFactory;
    }

    @Singleton
    @Provides
    DatabaseModule.RoomConfiguration provideRoomConfiguration() {
        return mRoomConfiguration == null ? DatabaseModule.RoomConfiguration.EMPTY : mRoomConfiguration;
    }

    @Singleton
    @Provides
    Executor provideUseCaseExcutor() {
        return mExecutor == null
                ? ThreadPoolUtils.createThreadPoolExecutor(1, 2,
                DEFAULT_ALIVE_TIME, TimeUnit.SECONDS, "UseCase")
                : mExecutor;
    }


    public static final class Builder {
        private HttpUrl apiUrl;
        private BaseUrl baseUrl;
        private GlobalHttpHandler handler;
        private List<Interceptor> interceptors;
//        private ResponseErrorListener responseErrorListener;
        private File cacheFile;
        private ClientModule.RetrofitConfiguration retrofitConfiguration;
        private ClientModule.OkhttpConfiguration okhttpConfiguration;
        private ClientModule.RxCacheConfiguration rxCacheConfiguration;
        private AppModule.GsonConfiguration gsonConfiguration;
        private HttpLoggingInterceptor.Level printHttpLogLevel;
        private Cache.Factory cacheFactory;
        private DatabaseModule.RoomConfiguration roomConfiguration;
        private Executor executor;

        private Builder() {
        }

        public Builder baseurl(String baseUrl) {
            //基础url
            if (TextUtils.isEmpty(baseUrl)) {
                throw new NullPointerException("BaseUrl can not be empty");
            }
            this.apiUrl = HttpUrl.parse(baseUrl);
            return this;
        }

        public Builder baseurl(BaseUrl baseUrl) {
            if (baseUrl == null) {
                throw new NullPointerException("BaseUrl can not be null");
            }
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder globalHttpHandler(GlobalHttpHandler handler) {
            //用来处理http响应结果
            this.handler = handler;
            return this;
        }

        public Builder addInterceptor(Interceptor interceptor) {
            //动态添加任意个interceptor
            if (interceptors == null) {
                interceptors = new ArrayList<>();
            }
            this.interceptors.add(interceptor);
            return this;
        }


//        public Builder responseErrorListener(ResponseErrorListener listener) {//处理所有RxJava的onError逻辑
//            this.responseErrorListener = listener;
//            return this;
//        }


        public Builder cacheFile(File cacheFile) {
            this.cacheFile = cacheFile;
            return this;
        }

        public Builder retrofitConfiguration(ClientModule.RetrofitConfiguration retrofitConfiguration) {
            this.retrofitConfiguration = retrofitConfiguration;
            return this;
        }

        public Builder okhttpConfiguration(ClientModule.OkhttpConfiguration okhttpConfiguration) {
            this.okhttpConfiguration = okhttpConfiguration;
            return this;
        }

        public Builder rxCacheConfiguration(ClientModule.RxCacheConfiguration rxCacheConfiguration) {
            this.rxCacheConfiguration = rxCacheConfiguration;
            return this;
        }

        public Builder gsonConfiguration(AppModule.GsonConfiguration gsonConfiguration) {
            this.gsonConfiguration = gsonConfiguration;
            return this;
        }

        public Builder printHttpLogLevel(HttpLoggingInterceptor.Level printHttpLogLevel) {
            //是否让框架打印 Http 的请求和响应信息
            if (printHttpLogLevel == null) {
                this.printHttpLogLevel = HttpLoggingInterceptor.Level.NONE;
            } else {
                this.printHttpLogLevel = printHttpLogLevel;
            }
            return this;
        }

        public Builder cacheFactory(Cache.Factory cacheFactory) {
            this.cacheFactory = cacheFactory;
            return this;
        }

        public Builder roomConfiguration(DatabaseModule.RoomConfiguration roomConfiguration) {
            this.roomConfiguration = roomConfiguration;
            return this;
        }

        public Builder excutor(Executor executor) {
            this.executor = executor;
            return this;
        }

        public GlobalConfigModule build() {
            return new GlobalConfigModule(this);
        }


    }

}
