package com.bingo.bingoframe.app;

import com.bingo.bingoframe.di.component.BingoComponent;
import com.bingo.bingoframe.di.component.DaggerBingoComponent;
import com.bingo.common.base.BaseApplication;
import com.bingo.library.di.component.AppComponent;

/**
 * @author bingo.
 * @date Create on 2017/12/18.
 * @Description
 */

public class MainApp extends BaseApplication {

    private BingoComponent bingoComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        bingoComponent = DaggerBingoComponent.builder()
                .appComponent(getAppComponent())
                .build();
        bingoComponent.inject(this);
    }

    public BingoComponent getBingoComponent() {
        return this.bingoComponent;
    }
}
