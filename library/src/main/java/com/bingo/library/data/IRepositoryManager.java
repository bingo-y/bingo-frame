package com.bingo.library.data;

import android.content.Context;

/**
 * @author bingo.
 * @date Create on 2017/10/27.
 * @Description 用来管理网络请求层,以及数据缓存层,以后可能添加数据库请求层
 */

public interface IRepositoryManager {

    /**
     * 根据传入的 Class 获取对应的 Retrofit service
     *
     * @param service
     * @param <T>
     * @return
     */
    <T> T obtainRetrofitService(Class<T> service);

    /**
     * 根据传入的 Class 获取对应的 RxCache service
     *
     * @param cache
     * @param <T>
     * @return
     */
    <T> T obtainCacheService(Class<T> cache);

    /**
     * 清理所有缓存
     */
    void clearAllCache();

    Context getContext();
}
