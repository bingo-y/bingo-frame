package com.bingo.common.base.bean;

/**
 * @author bingo.
 * @date Create on 2017/12/7.
 * @Description 请求返回基类
 */

public class BaseResult<T> {

    /**
     * 响应码
     */
    public int code;

    /**
     * 消息提示
     */
    public String msg;

    /**
     * 实体数据
     */
    public T data;
}
