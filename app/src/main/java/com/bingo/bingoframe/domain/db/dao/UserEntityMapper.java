package com.bingo.bingoframe.domain.db.dao;

import com.bingo.bingoframe.domain.db.entity.UserEntity;
import com.bingo.bingoframe.domain.model.User;
import com.bingo.library.data.EntityMapper;
import com.bingo.library.di.scope.ActivityScope;

import javax.inject.Inject;

/**
 * @author bingo.
 * @date Create on 2017/12/28.
 * @Description
 */
@ActivityScope
public class UserEntityMapper implements EntityMapper<UserEntity, User> {

    @Inject
    public UserEntityMapper() {
    }

    @Override
    public User mapFromCache(UserEntity type) {
        return new User(type.getId(), type.getFullName(), type.getFollowers(), type.getCompanion());
    }

    @Override
    public UserEntity mapToCache(User type) {
        return new UserEntity(type.getId(), type.getFull_name(), type.getFollowers(), type.getCompanion());
    }
}
