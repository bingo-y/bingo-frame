package com.bingo.bingoframe.domain.http.service;

import com.bingo.bingoframe.domain.http.Api;
import com.bingo.bingoframe.domain.model.User;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.GET;

/**
 * @author bingo.
 * @date Create on 2017/12/14.
 * @Description
 */

public interface UserService {

    @GET(Api.BASE_URL + "/android10/Sample-Data/master/Android-CleanArchitecture/users.json")
    Flowable<List<User>> getUsers();

}
