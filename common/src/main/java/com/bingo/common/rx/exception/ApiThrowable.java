package com.bingo.common.rx.exception;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.HttpException;
import timber.log.Timber;

/**
 * @author bingo.
 * @date Create on 2017/12/7.
 * @Description Http异常信息
 */

public class ApiThrowable {

    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;
    private static final int ACCESS_DENIED = 302;
    private static final int HANDEL_ERROR = 417;

    public static ApiException handleException(Throwable e) {

        Timber.e(e);
        ApiException ex;
        if(e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            ex = new ApiException(ApiErrorType.HTTP_ERROR, httpException.code(), e);
            switch (httpException.code()) {
                case UNAUTHORIZED:
                    ex.setMessage("未授权的请求");
                    break;
                case FORBIDDEN:
                    ex.setMessage("禁止访问");
                    break;
                case NOT_FOUND:
                    ex.setMessage("服务器地址未找到");
                    break;
                case REQUEST_TIMEOUT:
                    ex.setMessage("请求超时");
                    break;
                case GATEWAY_TIMEOUT:
                    ex.setMessage("网关响应超时");
                    break;
                case INTERNAL_SERVER_ERROR:
                    ex.setMessage("服务器异常");
                    break;
                case BAD_GATEWAY:
                    ex.setMessage("无效的请求");
                    break;
                case SERVICE_UNAVAILABLE:
                    ex.setMessage("服务器不可用");
                    break;
                case ACCESS_DENIED:
                    ex.setMessage("网络错误");
                    break;
                case HANDEL_ERROR:
                    ex.setMessage("接口处理失败");
                    break;
                default:
                    ex.setMessage("请求失败");
                    break;
            }
            return ex;
        } else if (e instanceof UnknownHostException) {
            ex = new ApiException(ApiErrorType.HTTP_ERROR, ERROR.NETWORD_ERROR, e);
            ex.setMessage("网络不可用");
            return ex;
        } else if (e instanceof SocketTimeoutException) {
            ex = new ApiException(ApiErrorType.HTTP_ERROR, ERROR.TIMEOUT_ERROR, e);
            ex.setMessage("请求网络超时");
            return ex;
        } else {
            ex = new ApiException(ApiErrorType.HTTP_ERROR, ERROR.UNKNOWN, e);
            ex.setMessage("请求失败");
            return ex;
        }
    }


    /**
     * 约定异常
     */
    public class ERROR {

        /**
         * 未知错误
         */
        public static final int UNKNOWN = 1000;
        /**
         * 解析错误
         */
        public static final int PARSE_ERROR = 1001;
        /**
         * 网络错误
         */
        public static final int NETWORD_ERROR = 1002;
        /**
         * 协议出错
         */
        public static final int HTTP_ERROR = 1003;

        /**
         * 证书出错
         */
        public static final int SSL_ERROR = 1005;

        /**
         * 连接超时
         */
        public static final int TIMEOUT_ERROR = 1006;

        /**
         * 证书未找到
         */
        public static final int SSL_NOT_FOUND = 1007;

        /**
         * 出现空值
         */
        public static final int NULL = -100;

        /**
         * 格式错误
         */
        public static final int FORMAT_ERROR = 1008;
    }

}
