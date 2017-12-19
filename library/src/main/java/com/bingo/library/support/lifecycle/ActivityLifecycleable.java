package com.bingo.library.support.lifecycle;

import android.app.Activity;

import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.ActivityEvent;

/**
 * @author bingo.
 * @date Create on 2017/11/2.
 * @Description 让 {@link Activity} 实现此接口,即可正常使用 {@link RxLifecycle}
 */

public interface ActivityLifecycleable extends Lifecycleable<ActivityEvent> {
}
