package com.bingo.bingoframe.di.module;

import android.app.Activity;

import com.bingo.bingoframe.UserActivity;
import com.bingo.bingoframe.ui.UserContract;
import com.bingo.library.di.scope.ActivityScope;
import com.tbruyelle.rxpermissions2.RxPermissions;

import javax.inject.Named;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * @author bingo.
 * @date Create on 2017/12/18.
 * @Description
 */
@Module(includes = {UserModule.Declarations.class, RxPermissionModule.class})
public final class UserModule {


    @Module
    public interface Declarations {
        @Binds
        UserContract.View provideUserView(UserActivity userActivity);

        @Binds
        Activity provideUserActivity(UserActivity userActivity);
    }

    @ActivityScope
    @Provides
    @Named("text")
    public String provideText(UserActivity userActivity) {
        return userActivity.text;
    }

}
