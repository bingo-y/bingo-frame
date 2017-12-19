package com.bingo.common.rx;

import com.bingo.common.rx.exception.ApiThrowable;

import org.reactivestreams.Publisher;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;

/**
 * @author bingo.
 * @date Create on 2017/12/7.
 * @Description Flowable Http错误转换
 */

public class RxHttpErrorFuncFlowable<T> implements Function<Throwable, Publisher<T>> {
    @Override
    public Publisher<T> apply(Throwable throwable) throws Exception {
        return Flowable.create(emitter -> emitter.onError(ApiThrowable.handleException(throwable)),
                BackpressureStrategy.BUFFER);
    }
}
