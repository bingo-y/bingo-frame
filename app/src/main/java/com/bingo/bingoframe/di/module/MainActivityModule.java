package com.bingo.bingoframe.di.module;

import com.bingo.bingoframe.MainActivity;
import com.bingo.library.di.scope.ActivityScope;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * @author bingo.
 * @date Create on 2017/12/22.
 * @Description
 */
@Module
public abstract class MainActivityModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = MainModule.class)
    abstract MainActivity contributeMainActivity();

}
