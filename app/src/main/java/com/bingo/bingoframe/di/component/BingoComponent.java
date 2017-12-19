package com.bingo.bingoframe.di.component;

import com.bingo.bingoframe.app.MainApp;
import com.bingo.bingoframe.di.module.BingoModule;
import com.bingo.library.di.component.AppComponent;
import com.bingo.library.di.scope.AppScope;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author bingo.
 * @date Create on 2017/12/18.
 * @Description
 */
@AppScope
@Component(modules = BingoModule.class, dependencies = AppComponent.class)
public interface BingoComponent {

    /**
     * 注入application
     * @param app
     */
    void inject(MainApp app);

}
