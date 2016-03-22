package com.github.scache.introrxforandroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.scache.introrxforandroid.api.GithubService;
import com.github.scache.introrxforandroid.api.RetrofitManager;
import com.github.scache.introrxforandroid.model.GithubUser;

import retrofit2.Retrofit;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private Subscription makeApiStream() {
        Retrofit retrofit = RetrofitManager.getInstance();
        return retrofit.create(GithubService.class)
                .users()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GithubUser>() {
                    @Override public void onCompleted() {

                    }

                    @Override public void onError(Throwable e) {

                    }

                    @Override public void onNext(GithubUser githubUser) {

                    }
                })
    }
}
