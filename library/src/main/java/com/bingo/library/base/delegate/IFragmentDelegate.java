package com.bingo.library.base.delegate;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * @author bingo.
 * @date Create on 2017/12/4.
 * @Description @link Fragment} 代理类,用于框架内部在每个 {@link Fragment} 的对应生命周期中插入需要的逻辑
 */

public interface IFragmentDelegate {
    String FRAGMENT_DELEGATE = "fragment_delegate";

    void onAttach(@NonNull Context context);

    void onCreate(@Nullable Bundle savedInstanceState);

    void onCreateView(@Nullable View view, @Nullable Bundle savedInstanceState);

    void onActivityCreate(@Nullable Bundle savedInstanceState);

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onSaveInstanceState(@NonNull Bundle outState);

    void onDestroyView();

    void onDestroy();

    void onDetach();

    /**
     * Return true if the fragment is currently added to its activity.
     */
    boolean isAdded();

}
