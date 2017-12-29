package com.bingo.bingoframe.domain.model;

import com.bingo.bingoframe.domain.db.AppDatabase;
import com.bingo.bingoframe.domain.db.dao.UserDao;
import com.bingo.bingoframe.domain.db.dao.UserEntityMapper;
import com.bingo.bingoframe.domain.db.entity.Companion;
import com.bingo.bingoframe.domain.db.entity.UserEntity;
import com.bingo.bingoframe.domain.http.cache.CacheService;
import com.bingo.bingoframe.domain.http.service.UserService;
import com.bingo.library.data.IRepositoryManager;
import com.bingo.library.di.scope.ActivityScope;
import com.bingo.library.mvp.BaseModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictProvider;

/**
 * @author bingo.
 * @date Create on 2017/12/14.
 * @Description 展示 Model 的用法
 */
@ActivityScope
public class UserModel extends BaseModel {

    UserEntityMapper userEntityMapper;

    UserDao userDao;

    @Inject
    public UserModel(IRepositoryManager repositoryManager, UserEntityMapper userEntityMapper) {
        super(repositoryManager);
        this.userEntityMapper = userEntityMapper;
        userDao = mRepositoryManager.obtainRoomDatabase(AppDatabase.class, "bingo_frame").userDao();
    }

    public Flowable<List<User>> listUser(int lastIdQueried, boolean update) {
        return mRepositoryManager.obtainCacheService(CacheService.class)
                .getUsers(mRepositoryManager.obtainRetrofitService(UserService.class).getUsers(), new DynamicKey(10), new EvictProvider(update))
                .flatMap(listReply -> Flowable.just(listReply.getData()))
                .map(users -> {
                    UserEntity[] userEntities = new UserEntity[users.size()];
                    for (int i = 0; i < users.size() ; i++) {
                        User u = users.get(i);
                        Companion companion = new Companion();
                        companion.age = 20;
                        companion.name = "default";
                        u.setCompanion(companion);
                        userEntities[i] = userEntityMapper.mapToCache(u);
                    }
                    userDao.deleteAll();
                    userDao.insertUsers(userEntities);
                    return users;
                });
    }

    public Flowable<List<User>> listLocalUser() {
        return Flowable.defer(() -> Flowable.just(userDao.selectAll())
                .map(userEntities -> {
                    List<User> users = new ArrayList<>();
                    for (UserEntity userEntity : userEntities) {
                        users.add(userEntityMapper.mapFromCache(userEntity));
                    }
                    return users;
                }));
    }

    public Completable deleteUser(int id) {
        return Completable.defer(() -> {
            userDao.deleteUser(id);
            return Completable.complete();
        });
    }

    public Single<Integer> insertUser(User user) {
        return Single.defer(() -> Single.just((int)userDao.insertUser(userEntityMapper.mapToCache(user)).longValue()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        userDao = null;
    }
}
