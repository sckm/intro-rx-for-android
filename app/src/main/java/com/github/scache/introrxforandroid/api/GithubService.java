package com.github.scache.introrxforandroid.api;

import com.github.scache.introrxforandroid.model.GithubUser;

import retrofit2.http.GET;
import rx.Observable;

public interface GithubService {
    @GET("users") Observable<GithubUser> users();
}
