package com.bingo.library.base.delegate;

import android.app.Application;
import android.content.Context;

/**
 * @author bingo.
 * @date Create on 2017/12/5.
 * @Description 用于代理 {@link Application} 的生命周期
 */

public interface AppLifecycles {

    void attachBaseContext(Context base);

    void onCreate(Application application);

    void onTerminate(Application application);

}
