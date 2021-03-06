package com.bingo.library.di.module;

import android.app.Application;
import android.content.Context;
import android.support.annotation.Nullable;

import com.bingo.library.data.http.GlobalHttpHandler;
import com.bingo.library.support.utils.FileUtils;
import com.google.gson.Gson;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.rx_cache2.internal.RxCache;
import io.victoralbertos.jolyglot.GsonSpeaker;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author bingo.
 * @date Create on 2017/11/3.
 * @Description 提供一些三方库客户端实例的 {@link Module}
 */
@Module
public class ClientModule {

    private static final int TIME_OUT = 10;


    /**
     * 提供 {@link Retrofit}
     *
     * @param builder
     * @param client
     * @param httpUrl
     * @return
     * @author: jess
     * @date 8/30/16 1:15 PM
     */
    @Singleton
    @Provides
    static Retrofit provideRetrofit(Application application,
                             @Nullable RetrofitConfiguration configuration,
                             Retrofit.Builder builder,
                             OkHttpClient client,
                             HttpUrl httpUrl,
                             Gson gson) {
        builder
            .baseUrl(httpUrl)
            .client(client);

        if (configuration != null) {
            configuration.configRetrofit(application, builder);
        }

        builder
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson));
        return builder.build();
    }

    /**
     * 提供 {@link OkHttpClient}
     *
     * @param builder
     * @return
     */
    @Singleton
    @Provides
    static OkHttpClient provideClient(Application application, @Nullable OkhttpConfiguration configuration, OkHttpClient.Builder builder, Interceptor intercept
            , @Nullable List<Interceptor> interceptors, @Nullable GlobalHttpHandler handler) {
        builder
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                .addNetworkInterceptor(intercept);

        if (handler != null) {
            builder.addInterceptor(chain -> chain.proceed(handler.onHttpRequestBefore(chain, chain.request())));
        }

        if (interceptors != null) {
            //如果外部提供了interceptor的集合则遍历添加
            for (Interceptor interceptor : interceptors) {
                builder.addInterceptor(interceptor);
            }
        }

        if (configuration != null) {
            configuration.configOkhttp(application, builder);
        }
        return builder.build();
    }


    @Singleton
    @Provides
    static Retrofit.Builder provideRetrofitBuilder() {
        return new Retrofit.Builder();
    }


    @Singleton
    @Provides
    static OkHttpClient.Builder provideClientBuilder() {
        return new OkHttpClient.Builder();
    }


    @Singleton
    @Provides
    static Interceptor provideLoggingInterceptor(HttpLoggingInterceptor.Level level) {
        // 打印请求信息的拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        if (level == null) {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        } else {
            loggingInterceptor.setLevel(level);
        }
        return loggingInterceptor;
    }


    /**
     * 提供 {@link RxCache}
     *
     * @param cacheDirectory RxCache缓存路径
     * @return
     */
    @Singleton
    @Provides
    static RxCache provideRxCache(Application application, @Nullable RxCacheConfiguration configuration, @Named("RxCacheDirectory") File cacheDirectory) {
        RxCache.Builder builder = new RxCache.Builder();
        RxCache rxCache = null;
        if (configuration != null) {
            rxCache = configuration.configRxCache(application, builder);
        }
        if (rxCache != null) {
            return rxCache;
        }
        return builder
                .persistence(cacheDirectory, new GsonSpeaker());
    }

    /**
     * 需要单独给 {@link RxCache} 提供缓存路径
     *
     * @param context
     * @return
     */
    @Singleton
    @Provides
    @Named("RxCacheDirectory")
    static File provideRxCacheDirectory(Application context) {
        try {
            return FileUtils.getAppFileInExt(context, FileUtils.AppDirTypeInExt.RXCACHE);
        } catch (FileUtils.NotFoundExternalSD notFoundExternalSD) {
            notFoundExternalSD.printStackTrace();
        }
        return null;
    }

    public interface RetrofitConfiguration {
        void configRetrofit(Context context, Retrofit.Builder builder);
    }

    public interface OkhttpConfiguration {
        void configOkhttp(Context context, OkHttpClient.Builder builder);
    }

    public interface RxCacheConfiguration {
        /**
         * 若想自定义 RxCache 的缓存文件夹或者解析方式, 如改成 fastjson
         * 请 {@code return rxCacheBuilder.persistence(cacheDirectory, new FastJsonSpeaker());}, 否则请 {@code return null;}
         *
         * @param context
         * @param builder
         * @return
         */
        RxCache configRxCache(Context context, RxCache.Builder builder);
    }

}
