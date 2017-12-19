package com.bingo.bingoframe;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.bingo.bingoframe.domain.model.User;
import com.bingo.bingoframe.ui.UserContract;
import com.bingo.bingoframe.ui.UserPresenter;
import com.bingo.common.base.BaseActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * @author bingo.
 * @date Create on 2017/12/11.
 * @Description
 */

public class UserActivity extends BaseActivity<UserPresenter> implements UserContract.View {


    @BindView(R.id.name_list)
    TextView nameList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public int initLayout(Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        mPresenter.listUser(true);
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
        }
        nameList.setText(sb.toString());
    }

    @Override
    public boolean useEventBus() {
        return false;
    }
}
