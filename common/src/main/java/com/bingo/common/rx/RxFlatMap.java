package com.bingo.common.rx;

import com.bingo.common.base.bean.BaseResult;
import com.bingo.common.constant.HttpCst;
import com.bingo.common.rx.exception.ApiErrorType;
import com.bingo.common.rx.exception.ApiException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * @author bingo.
 * @date Create on 2017/12/7.
 * @Description
 */

public class RxFlatMap<R extends BaseResult<T>, T> implements Function<R, Observable<R>> {

    @Override
    public Observable<R> apply(@NonNull R result) throws Exception {
        return Observable.create(emitter -> {
            if (HttpCst.OK == result.code) {
                emitter.onNext(result);
                emitter.onComplete();
            } else {
                emitter.onError(new ApiException(ApiErrorType.SERVER_ERROR, result.code, result.msg));
            }
        });
    }

}
