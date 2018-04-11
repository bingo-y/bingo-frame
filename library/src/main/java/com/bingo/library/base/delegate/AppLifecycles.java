package com.bingo.library.base.delegate;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

/**
 * @author bingo.
 * @date Create on 2017/12/5.
 * @Description 用于代理 {@link Application} 的生命周期
 */

public interface AppLifecycles {

    void attachBaseContext(@NonNull Context base);

    void onCreate(@NonNull Application application);

    void onTerminate(@NonNull Application application);

}
