package com.bingo.library.data.http;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import com.bingo.library.di.module.GlobalConfigModule;

/**
 * @author bingo.
 * @date Create on 2017/10/30.
 * @Description
 * 处理 Http 请求和响应结果的处理类
 * 使用 {@link GlobalConfigModule.Builder(GlobalHttpHandler)} 方法配置
 */

public interface GlobalHttpHandler {

    Response onHttpResultResponse(String httpResult, Interceptor.Chain chain, Response response);

    Request onHttpRequestBefore(Interceptor.Chain chain, Request request);

    /**
     * 空实现
     */
    GlobalHttpHandler EMPTY = new GlobalHttpHandler() {
        @Override
        public Response onHttpResultResponse(String httpResult, Interceptor.Chain chain, Response response) {
            //不管是否处理,都必须将response返回出去
            return response;
        }

        @Override
        public Request onHttpRequestBefore(Interceptor.Chain chain, Request request) {
            //不管是否处理,都必须将request返回出去
            return request;
        }
    };
}
