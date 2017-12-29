package com.bingo.library.data;

/**
 * @author bingo.
 * @date Create on 2017/12/28.
 * @Description Interface for model mappers. It provides helper methods that facilitate
 * retrieving of models from outer data source layers
 *
 * @param <T> the cached model input type
 * @param <T> the remote model input type
 * @param <V> the model return type
 */

public interface EntityMapper<T, V> {

    V mapFromCache(T type);

    T mapToCache(V type);

}
