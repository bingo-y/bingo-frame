package com.bingo.library.base.delegate;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * @author bingo.
 * @date Create on 2017/12/4.
 * @Description {@link Activity} 代理类,用于框架内部在每个 {@link Activity} 的对应生命周期中插入需要的逻辑
 */

public interface IActivityDelegate {

    String ACTIVITY_DELEGATE = "activity_delegate";

    void onCreate(@Nullable Bundle savedInstanceState);

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onSaveInstanceState(@NonNull Bundle outState);

    void onDestroy();

}
