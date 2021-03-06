package com.bingo.library.mvp;

/**
 * @author bingo.
 * @date Create on 2017/12/8.
 * @Description 框架要求框架中的每个 Model 都需要实现此类,以满足规范
 */

public interface IModel {

    /**
     * 在框架中 {@link BasePresenter#onDestroy()} 时会默认调用 {@link IModel#onDestroy()}
     */
    void onDestroy();

}
