package com.bingo.bingoframe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bingo.bingoframe.domain.db.entity.Book;
import com.bingo.bingoframe.domain.db.entity.Companion;
import com.bingo.bingoframe.domain.model.User;
import com.bingo.bingoframe.ui.UserContract;
import com.bingo.bingoframe.ui.UserPresenter;
import com.bingo.common.base.BaseActivity;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;

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
    @BindView(R.id.local_name_list)
    TextView localNameList;
    @BindView(R.id.btn_load_local)
    Button btnLoadLocal;

    @Inject
    RxPermissions rxPermissions;
    public String text;

    boolean firstIn = true;
    @BindView(R.id.et_user)
    EditText etUser;
    @BindView(R.id.btn_insert)
    Button btnInsert;
    @BindView(R.id.btn_delete)
    Button btnDelete;

    int maxId;
    @BindView(R.id.local_book_list)
    TextView localBookList;
    @BindView(R.id.btn_load_book)
    Button btnLoadBook;
    @BindView(R.id.btn_insert_book)
    Button btnInsertBook;

    final static String[] bookNames = {"java", "python", "js", "android", "swift", "kotlin"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        text = getIntent().getStringExtra("text");
        super.onCreate(savedInstanceState);
        Timber.d("UserActivity onCreate");

    }

    @Override
    protected void onStart() {
        super.onStart();
        Timber.d("UserActivity onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Timber.d("UserActivity onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Timber.d("UserActivity onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Timber.d("UserActivity onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Timber.d("UserActivity onDestroy");
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
    public void onLocalUserSuccess(List<User> users) {
        StringBuilder sb = new StringBuilder();
        for (User user : users) {
            if (user.getCompanion() != null) {
                sb.append(user.getFull_name() + " with " + user.getCompanion().name);
            } else {
                sb.append(user.getFull_name());
            }
            sb.append("\n");
        }
        localNameList.setText(sb.toString());

        maxId = users.get(users.size() - 1).getId();
    }

    @Override
    public void onInsertSuccess(Integer id) {
        this.maxId = id;
    }

    @Override
    public void onBooksSuccess(List<Book> books) {
        StringBuilder sb = new StringBuilder();
        for (Book book : books) {
            sb.append(book.title);
            sb.append("\n");
        }
        localBookList.setText(sb.toString());
    }

    @Override
    public void onMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
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

    @OnClick({R.id.btn_refresh, R.id.btn_load_local, R.id.btn_insert, R.id.btn_delete, R.id.btn_load_book, R.id.btn_insert_book})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_refresh:
                mPresenter.requestExternalStorage();
                break;
            case R.id.btn_load_local:
                mPresenter.listLocalUsers();
                break;
            case R.id.btn_insert:

                String name = etUser.getText().toString();
                Companion companion = new Companion();
                companion.name = "new add";
                companion.age = 18;
                User user = new User(maxId + 1, TextUtils.isEmpty(name) ? "default" : name, 0, companion);
                mPresenter.insertUser(user);

                break;
            case R.id.btn_delete:

                mPresenter.deleteUser(maxId);

                break;
            case R.id.btn_load_book:

                String userName = etUser.getText().toString();
                mPresenter.selectBooks(TextUtils.isEmpty(userName) ? "default" : userName);

                break;
            case R.id.btn_insert_book:

                mPresenter.insertBooks(createBook(etUser.getText().toString()));

                break;
            default:
                break;
        }
    }

    List<Book> createBook(String name) {
        List<Book> list = new ArrayList<>();
        for (int i = 0; i < bookNames.length; i++) {
            Book book = new Book();
            book.title = name +  "_" + bookNames[i];
            book.userId = maxId;
            list.add(book);
        }
        return list;
    }

}
