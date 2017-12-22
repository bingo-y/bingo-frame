package com.bingo.bingoframe.di.module;

import android.app.Activity;

import com.bingo.bingoframe.ui.UserContract;
import com.bingo.library.di.scope.ActivityScope;
import com.tbruyelle.rxpermissions2.RxPermissions;

import dagger.Module;
import dagger.Provides;

/**
 * @author bingo.
 * @date Create on 2017/12/22.
 * @Description
 */
@Module
public class RxPermissionModule {

    @ActivityScope
    @Provides
    public RxPermissions provideRxPermissions(Activity activity) {
        return new RxPermissions(activity);
    }

}
