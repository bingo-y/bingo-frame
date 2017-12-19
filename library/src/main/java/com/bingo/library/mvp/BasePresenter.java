package com.bingo.library.mvp;

import android.app.Activity;
import android.app.Service;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.v4.app.Fragment;
import android.support.v4.app.SupportActivity;
import android.view.View;

import com.bingo.library.support.utils.Preconditions;
import com.trello.rxlifecycle2.RxLifecycle;

import org.greenrobot.eventbus.EventBus;

import java.util.LinkedList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;

/**
 * @author bingo.
 * @date Create on 2017/12/8.
 * @Description
 */

public class BasePresenter<V extends IView> implements IPresenter, LifecycleObserver {

    protected final String TAG = this.getClass().getSimpleName();

    protected CompositeDisposable mCompositeDisposable;
    protected List<IModel> mModels;

    protected V mView;


    /**
     * 如果当前页面不需要操作数据,只需要 View 层,则使用此构造函数
     *
     * @param view
     */
    public BasePresenter(V view) {
        Preconditions.checkNotNull(view, "%s cannot be null", IView.class.getName());
        this.mView = view;
        onStart();
    }

    public BasePresenter() {
        onStart();
    }


    @Override
    public void onStart() {
        //将 LifecycleObserver 注册给 LifecycleOwner 后 @OnLifecycleEvent 才可以正常使用
        if (mView != null && mView instanceof LifecycleOwner) {
            ((LifecycleOwner) mView).getLifecycle().addObserver(this);
        }
        // 如果要使用 Eventbus 请将此方法返回 true
        if (useEventBus()) {
            //注册 Eventbus
            EventBus.getDefault().register(this);
        }
    }

    /**
     * 在框架中 {@link Activity#onDestroy()} 时会默认调用 {@link IPresenter#onDestroy()}
     */
    @Override
    public void onDestroy() {
        if (useEventBus()) {
            //解除注册 Eventbus
            EventBus.getDefault().unregister(this);
        }
        //解除订阅
        unDispose();
        clearModels();
        this.mView = null;
        this.mCompositeDisposable = null;
        mModels = null;
    }

    /**
     * 只有当 {@code mView} 不为 null, 并且 {@code mView} 实现了 {@link LifecycleOwner} 时, 此方法才会被调用
     * 所以当您想在 {@link Service} 以及一些自定义 {@link View} 或自定义类中使用 {@code Presenter} 时
     * 您也将不能继续使用 {@link OnLifecycleEvent} 绑定生命周期
     *
     * @param owner link {@link SupportActivity} and {@link Fragment}
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroy(LifecycleOwner owner) {
        /**
         * 注意, 如果在这里调用了 {@link #onDestroy()} 方法, 会出现某些地方引用 {@code mModel} 或 {@code mView} 为 null 的情况
         * 比如在 {@link RxLifecycle} 终止 {@link Observable} 时, 在 {@link io.reactivex.Observable#doFinally(Action)} 中却引用了 {@code mView} 做一些释放资源的操作, 此时会空指针
         * 或者如果你声明了多个 @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY) 时在其他 @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
         * 中引用了 {@code mModel} 或 {@code mView} 也可能会出现此情况
         */
        owner.getLifecycle().removeObserver(this);
    }

    /**
     * 是否使用 {@link EventBus},默认不使用(false)，
     *
     * @return
     */
    public boolean useEventBus() {
        return false;
    }


    /**
     * 将 {@link Disposable} 添加到 {@link CompositeDisposable} 中统一管理
     * 可在 {@link Activity#onDestroy()} 中使用 {@link #unDispose()} 停止正在执行的 RxJava 任务,避免内存泄漏
     * 目前框架已使用 {@link RxLifecycle} 避免内存泄漏,此方法作为备用方案
     *
     * @param disposable
     */
    public void addDispose(Disposable disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }

    /**
     * 停止集合中正在执行的 RxJava 任务
     */
    public void unDispose() {
        if (mCompositeDisposable != null) {
            //保证 Activity 结束时取消所有正在执行的订阅
            mCompositeDisposable.clear();
        }
    }

    /**
     * 添加{@link IModel} 统一管理
     * @param model
     */
    public void addModel(IModel model) {
        if (mModels == null) {
            mModels = new LinkedList<>();
        }
        mModels.add(model);
    }

    /**
     * 清理{@link IModel} 列表
     */
    public void clearModels() {
        if (mModels != null && mModels.size() > 0) {
            for (IModel model : mModels) {
                if (model != null) {
                    model.onDestroy();
                }
            }
            mModels.clear();
        }
    }


}
