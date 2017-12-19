package com.bingo.bingoframe.domain.http.cache;

import com.bingo.bingoframe.domain.model.User;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictProvider;
import io.rx_cache2.LifeCache;
import io.rx_cache2.Reply;
import io.rx_cache2.internal.RxCache;

/**
 * @author bingo.
 * @date Create on 2017/12/18.
 * @Description 展示 {@link RxCache#using(Class)} 中需要传入的 Providers 的使用方式
 */

public interface CacheService {

    @LifeCache(duration = 2, timeUnit = TimeUnit.MINUTES)
    Flowable<Reply<List<User>>> getUsers(Flowable<List<User>> users, DynamicKey lastId, EvictProvider evictProvider);

}
