package com.bingo.library.mvp;

import com.bingo.library.data.IRepositoryManager;

/**
 * @author bingo.
 * @date Create on 2017/12/8.
 * @Description
 */

public class BaseModel implements IModel {

    /**
     * 用于管理网络请求层,以及数据缓存层
     */
    protected IRepositoryManager mRepositoryManager;

    public BaseModel(IRepositoryManager repositoryManager) {
        this.mRepositoryManager = repositoryManager;
    }

    /**
     * 在框架中 {@link BasePresenter#onDestroy()} 时会默认调用 {@link IModel#onDestroy()}
     */
    @Override
    public void onDestroy() {
        mRepositoryManager = null;
    }

}
