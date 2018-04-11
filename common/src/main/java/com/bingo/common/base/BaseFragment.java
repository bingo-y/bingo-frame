package com.bingo.common.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bingo.library.base.delegate.IFragment;
import com.bingo.library.data.cache.Cache;
import com.bingo.library.data.cache.CacheType;
import com.bingo.library.mvp.IPresenter;
import com.bingo.library.support.lifecycle.FragmentLifecycleable;
import com.bingo.library.support.utils.ArmsUtils;
import com.trello.rxlifecycle2.android.FragmentEvent;

import javax.inject.Inject;

import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

/**
 * @author bingo.
 * @date Create on 2017/12/5.
 * @Description 因为 Java 只能单继承,所以如果要用到需要继承特定 @{@link Fragment} 的三方库,那你就需要自己自定义 @{@link Fragment}
 * 继承于这个特定的 @{@link Fragment},然后再按照 {@link BaseFragment} 的格式,将代码复制过去,记住一定要实现{@link IFragment}
 */

public abstract class BaseFragment<P extends IPresenter> extends Fragment implements IFragment, FragmentLifecycleable {

    protected final String TAG = this.getClass().getSimpleName();
    private final BehaviorSubject<FragmentEvent> mLifecycleSubject = BehaviorSubject.create();
    private Cache<String, Object> mCache;


    @Inject
    @Nullable
    protected P mPresenter;

    @NonNull
    @Override
    public synchronized Cache<String, Object> provideCache() {
            if (mCache == null) {
                mCache = ArmsUtils.INSTANCE.obtainArmsComponent(getActivity()).cacheFactory().build(CacheType.FRAGMENT_CACHE);
            }
            return mCache;
    }


    @NonNull
    @Override
    public final Subject<FragmentEvent> provideLifecycleSubject() {
            return mLifecycleSubject;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return initView(inflater, container, savedInstanceState);
    }


    @Override
    public void onDestroy() {
            super.onDestroy();
            //释放资源
            if (mPresenter != null) {
                mPresenter.onDestroy();
            }
            this.mPresenter = null;
    }


    /**
     * 是否使用eventBus,默认为使用(true)，
     *
     * @return
     */
    @Override
    public boolean useEventBus() {
            return false;
    }

    /**
     * 是否依赖注入，默认注入(true)
     * @return
     */
    @Override
    public boolean injectable() {
        return true;
    }
}
