package com.bingo.bingoframe.domain.model;

import com.bingo.bingoframe.domain.http.cache.CacheService;
import com.bingo.bingoframe.domain.http.service.UserService;
import com.bingo.library.data.IRepositoryManager;
import com.bingo.library.di.scope.ActivityScope;
import com.bingo.library.mvp.BaseModel;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictProvider;

/**
 * @author bingo.
 * @date Create on 2017/12/14.
 * @Description 展示 Model 的用法
 */
@ActivityScope
public class UserModel extends BaseModel {

    @Inject
    public UserModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    public Flowable<List<User>> listUser(int lastIdQueried, boolean update) {
        return mRepositoryManager.obtainCacheService(CacheService.class)
                .getUsers(mRepositoryManager.obtainRetrofitService(UserService.class).getUsers(), new DynamicKey(10), new EvictProvider(update))
                .flatMap(listReply -> Flowable.just(listReply.getData()));
    }

}
