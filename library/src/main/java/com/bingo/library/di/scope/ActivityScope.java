package com.bingo.library.di.scope;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author bingo.
 * @date Create on 2017/11/2.
 * @Description
 */

@Scope
@Documented
@Retention(RUNTIME)
public @interface ActivityScope {
}
