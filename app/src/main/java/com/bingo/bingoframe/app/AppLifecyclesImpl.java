package com.bingo.bingoframe.app;

import android.app.Application;
import android.content.Context;

import com.bingo.bingoframe.BuildConfig;
import com.bingo.bingoframe.domain.db.AppDatabase;
import com.bingo.library.base.delegate.AppLifecycles;
import com.bingo.library.support.utils.ArmsUtils;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * @author bingo.
 * @date Create on 2017/12/12.
 * @Description 展示 {@link AppLifecycles} 的用法
 */

public class AppLifecyclesImpl implements AppLifecycles {

    @Override
    public void attachBaseContext(Context base) {
        //这里比 onCreate 先执行,常用于 MultiDex 初始化,插件化框架的初始化
//          MultiDex.install(base);
    }

    @Override
    public void onCreate(Application application) {
        if (LeakCanary.isInAnalyzerProcess(application)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }

        if (BuildConfig.DEBUG) {
            //Timber初始化
            //Timber 是一个日志框架容器,外部使用统一的Api,内部可以动态的切换成任何日志框架(打印策略)进行日志打印
            //并且支持添加多个日志框架(打印策略),做到外部调用一次 Api,内部却可以做到同时使用多个策略
            //比如添加三个策略,一个打印日志,一个将日志保存本地,一个将日志上传服务器
            Timber.plant(new Timber.DebugTree());
            // 如果你想将框架切换为 Logger 来打印日志,请使用下面的代码,如想切换为其他日志框架请根据下面的方式扩展
//                    Logger.addLogAdapter(new AndroidLogAdapter());
//                    Timber.plant(new Timber.DebugTree() {
//                        @Override
//                        protected void log(int priority, String tag, String message, Throwable t) {
//                            Logger.log(priority, tag, message, t);
//                        }
//                    });
            ButterKnife.setDebug(true);
        }
        //leakCanary内存泄露检查
        ArmsUtils.INSTANCE.obtainArmsComponent(application).extras().put(RefWatcher.class.getName(), BuildConfig.DEBUG ? LeakCanary.install(application) : RefWatcher.DISABLED);

        // init db
        ArmsUtils.INSTANCE.obtainArmsComponent(application).repositoryManager().obtainRoomDatabase(AppDatabase.class, AppDatabase.DB_NAME);

    }

    @Override
    public void onTerminate(Application application) {

    }

}
