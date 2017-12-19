package com.bingo.bingoframe.di.module;

import com.bingo.bingoframe.UserActivity;
import com.bingo.bingoframe.ui.UserContract;
import com.bingo.library.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * @author bingo.
 * @date Create on 2017/12/18.
 * @Description
 */
@Module
public abstract class UserModule {


    @ActivityScope
    @Binds
    public abstract UserContract.View provideUserView(UserActivity userActivity);
}
