package com.bingo.common.rx.exception;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.bingo.common.rx.exception.ApiErrorType.HTTP_ERROR;
import static com.bingo.common.rx.exception.ApiErrorType.SERVER_ERROR;

/**
 * @author bingo.
 * @date Create on 2017/12/7.
 * @Description
 */
@IntDef({HTTP_ERROR, SERVER_ERROR})
@Retention(RetentionPolicy.SOURCE)
public @interface ApiErrorType {

    /**
     * 网络异常
     */
    int HTTP_ERROR = 1;

    /**
     * 服务端数据异常
     */
    int SERVER_ERROR = 2;
}
