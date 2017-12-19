package com.bingo.common.rx;

import com.bingo.common.rx.exception.ApiThrowable;

import org.reactivestreams.Publisher;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * @author bingo.
 * @date Create on 2017/12/7.
 * @Description Observable Http错误转换
 */

public class RxHttpErrorFunc<T> implements Function<Throwable, Observable<T>> {
    @Override
    public Observable<T> apply(Throwable throwable) throws Exception {
        return Observable.create(emitter -> emitter.onError(ApiThrowable.handleException(throwable)));
    }
}
