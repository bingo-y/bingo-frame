package com.bingo.bingoframe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.bingo.bingoframe.ui.MainContract;
import com.bingo.bingoframe.ui.MainPresenter;
import com.bingo.common.base.BaseActivity;
import com.tbruyelle.rxpermissions2.RxPermissions;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author bingo.
 * @date Create on 2017/12/22.
 * @Description
 */

public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View {

    @BindView(R.id.et_any)
    EditText etAny;
    @BindView(R.id.btn_next)
    Button btnNext;

    @Inject
    MainPresenter mainPresenter;
    @Inject
    RxPermissions rxPermissions;

    @Override
    public int initLayout(Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        mainPresenter.requestExternalStorage();
    }

    @OnClick(R.id.btn_next)
    public void onClick() {
        String text = etAny.getText().toString();
        Intent intent = new Intent(this, UserActivity.class);
        intent.putExtra("text", text);
        startActivity(intent);
    }

    @Override
    public void permissionsGranted() {

    }

    @Override
    public void shouldShowRequestPermissions() {

    }

    @Override
    public void openPermissionsInSetting() {

    }

    @Override
    public void startLoading() {

    }

    @Override
    public void endLoading() {

    }

    @Override
    public RxPermissions getRxPermissions() {
        return rxPermissions;
    }

    @Override
    public Activity getActivity() {
        return this;
    }
}
