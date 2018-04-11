package com.bingo.library.data;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.bingo.library.data.cache.Cache;
import com.bingo.library.data.cache.CacheType;
import com.bingo.library.di.module.DatabaseModule;
import com.bingo.library.support.utils.Preconditions;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Lazy;
import io.rx_cache2.internal.RxCache;
import retrofit2.Retrofit;

/**
 * @author bingo.
 * @date Create on 2017/12/4.
 * @Description 用来管理网络请求层,以及数据缓存层,以后可能添加数据库请求层
 * 提供给 IModel 层必要的 Api 做数据处理
 */
@Singleton
public class RepositoryManager implements IRepositoryManager {

    @Inject
    Lazy<Retrofit> mRetrofit;
    @Inject
    Lazy<RxCache> mRxCache;
    @Inject
    Application mApplication;
    @Inject
    Cache.Factory mCachefactory;
    @Inject
    DatabaseModule.RoomConfiguration mRoomConfiguration;

    private Cache<String, Object> mRetrofitServiceCache;
    private Cache<String, Object> mCacheServiceCache;
    private Cache<String, Object> mRoomDatabaseCache;


    @Inject
    public RepositoryManager() {
    }

    /**
     * 根据传入的 Class 获取对应的 Retrofit service
     *
     * @param service
     * @param <T>
     * @return
     */
    @Override
    public <T> T obtainRetrofitService(Class<T> service) {
        if (mRetrofitServiceCache == null) {
            mRetrofitServiceCache = mCachefactory.build(CacheType.RETROFIT_SERVICE_CACHE);
        }
        Preconditions.checkNotNull(mRetrofitServiceCache, "Cannot return null from a Cache.Factory#build(int) method");
        T retrofitService = (T) mRetrofitServiceCache.get(service.getCanonicalName());
        if (retrofitService == null) {
            retrofitService = mRetrofit.get().create(service);
            mRetrofitServiceCache.put(service.getCanonicalName(), retrofitService);
        }
        return retrofitService;
    }

    /**
     * 根据传入的 Class 获取对应的 RxCache service
     *
     * @param cache
     * @param <T>
     * @return
     */
    @Override
    public <T> T obtainCacheService(Class<T> cache) {
        if (mCacheServiceCache == null) {
            mCacheServiceCache = mCachefactory.build(CacheType.CACHE_SERVICE_CACHE);
        }
        Preconditions.checkNotNull(mCacheServiceCache,"Cannot return null from a Cache.Factory#build(int) method");
        T cacheService = (T) mCacheServiceCache.get(cache.getCanonicalName());
        if (cacheService == null) {
            cacheService = mRxCache.get().using(cache);
            mCacheServiceCache.put(cache.getCanonicalName(), cacheService);
        }
        return cacheService;
    }

    @Override
    public <DB extends RoomDatabase> DB obtainRoomDatabase(Class<DB> database, String dbName) {
        if (mRoomDatabaseCache == null) {
            mRoomDatabaseCache = mCachefactory.build(CacheType.DATABASE_SERVICE_CACHE);
        }
        Preconditions.checkNotNull(mRoomDatabaseCache,"Cannot return null from a Cache.Factory#build(int) method");
        DB roomDatabase = (DB) mRoomDatabaseCache.get(database.getCanonicalName());
        if (roomDatabase == null) {
            RoomDatabase.Builder builder = Room.databaseBuilder(mApplication, database, dbName);
            //自定义 Room 配置
            if (mRoomConfiguration != null) {
                mRoomConfiguration.configRoom(mApplication, builder);
            }
            roomDatabase = (DB) builder.build();
            mRoomDatabaseCache.put(database.getCanonicalName(), roomDatabase);
        }
        return roomDatabase;
    }

    /**
     * 清理所有缓存
     */
    @Override
    public void clearAllCache() {
        mRxCache.get().evictAll();
    }

    @Override
    public Context getContext() {
        return mApplication;
    }
}
