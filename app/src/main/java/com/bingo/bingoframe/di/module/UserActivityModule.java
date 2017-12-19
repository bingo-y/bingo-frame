package com.bingo.bingoframe.di.module;

import com.bingo.bingoframe.UserActivity;
import com.bingo.library.di.scope.ActivityScope;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * @author bingo.
 * @date Create on 2017/12/18.
 * @Description
 */
@Module
public abstract class UserActivityModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = UserModule.class)
    abstract UserActivity contributeUserActivity();

}
