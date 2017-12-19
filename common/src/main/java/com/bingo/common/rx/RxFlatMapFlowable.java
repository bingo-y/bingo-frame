package com.bingo.common.rx;

import com.bingo.common.base.bean.BaseResult;
import com.bingo.common.constant.HttpCst;
import com.bingo.common.rx.exception.ApiErrorType;
import com.bingo.common.rx.exception.ApiException;

import org.greenrobot.eventbus.EventBus;
import org.reactivestreams.Publisher;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author bingo.
 * @date Create on 2017/12/7.
 * @Description RxJava转换器
 */

public class RxFlatMapFlowable<R extends BaseResult<T>, T> implements Function<R, Publisher<R>> {

    @Override
    public Publisher<R> apply(@NonNull R result) throws Exception {
        return Flowable.create(emitter -> {
            if (HttpCst.OK == result.code) {
                emitter.onNext(result);
                emitter.onComplete();
            } else {
                emitter.onError(new ApiException(ApiErrorType.SERVER_ERROR, result.code, result.msg));
            }
        }, BackpressureStrategy.BUFFER);
    }

}
