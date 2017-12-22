package com.bingo.bingoframe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.TextView;

import com.bingo.bingoframe.domain.model.User;
import com.bingo.bingoframe.ui.UserContract;
import com.bingo.bingoframe.ui.UserPresenter;
import com.bingo.common.base.BaseActivity;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author bingo.
 * @date Create on 2017/12/11.
 * @Description
 */

public class UserActivity extends BaseActivity<UserPresenter> implements UserContract.View {


    @BindView(R.id.name_list)
    TextView nameList;
    @BindView(R.id.btn_refresh)
    Button btnRefresh;

    @Inject
    RxPermissions rxPermissions;
    public String text;

    boolean firstIn = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        text = getIntent().getStringExtra("text");
        super.onCreate(savedInstanceState);

    }

    @Override
    public int initLayout(Bundle savedInstanceState) {
        return R.layout.activity_user;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        mPresenter.requestExternalStorage();
    }

    @Override
    public void startLoading() {

    }

    @Override
    public void endLoading() {

    }

    @Override
    public void launchActivity(Intent intent) {

    }

    @Override
    public void killMyself() {

    }

    @Override
    public void onUserSuccess(List<User> users) {
        StringBuilder sb = new StringBuilder();
        for (User user : users) {
            sb.append(user.getFull_name());
            sb.append("\n");
        }
        nameList.setText(sb.toString());
    }

    @Override
    public RxPermissions getRxPermissions() {
        return rxPermissions;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public boolean useEventBus() {
        return false;
    }

    @OnClick(R.id.btn_refresh)
    public void onClick() {

        mPresenter.requestExternalStorage();

    }

    @Override
    public void permissionsGranted() {
        mPresenter.listUser(firstIn);
        if (firstIn) {
            firstIn = false;
        }
    }

    @Override
    public void shouldShowRequestPermissions() {

    }

    @Override
    public void openPermissionsInSetting() {

    }
}
