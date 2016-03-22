package com.github.scache.introrxforandroid.api;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {
    static Retrofit mRetrofit;

    public static Retrofit getInstance() {
        if (mRetrofit == null) {
            synchronized (RetrofitManager.class) {
                if (mRetrofit == null) {
                    OkHttpClient client = new OkHttpClient.Builder()
                            .addInterceptor(new StethoInterceptor())
                            .build();

                    mRetrofit = new Retrofit.Builder()
                            .client(client)
                            .baseUrl("https://api.github.com")
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                }
            }
        }

        return mRetrofit;
    }
}
