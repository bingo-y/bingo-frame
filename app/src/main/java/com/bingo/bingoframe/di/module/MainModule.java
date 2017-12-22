package com.bingo.bingoframe.di.module;

import android.app.Activity;

import com.bingo.bingoframe.MainActivity;
import com.bingo.bingoframe.ui.MainContract;

import dagger.Binds;
import dagger.Module;

/**
 * @author bingo.
 * @date Create on 2017/12/22.
 * @Description
 */
@Module(includes = RxPermissionModule.class)
public abstract class MainModule {

    @Binds
    public abstract MainContract.View provideMainView(MainActivity mainActivity);

    @Binds
    public abstract Activity provideMainActivity(MainActivity userActivity);


}
