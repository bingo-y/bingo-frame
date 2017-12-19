package com.bingo.common.base;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @author bingo.
 * @date Create on 2017/12/6.
 * @Description
 */

public abstract class BaseService extends Service {

    protected final String TAG = this.getClass().getSimpleName();

    protected CompositeDisposable mCompositeDisposable;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        init();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        unDispose();//解除订阅
        this.mCompositeDisposable = null;
    }

    protected void addDispose(Disposable disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        //将所有subscription放入,集中处理
        mCompositeDisposable.add(disposable);
    }

    protected void unDispose() {
        if (mCompositeDisposable != null) {
            //保证activity结束时取消所有正在执行的订阅
            mCompositeDisposable.clear();
        }
    }

    /**
     * 初始化
     */
    abstract public void init();

}
