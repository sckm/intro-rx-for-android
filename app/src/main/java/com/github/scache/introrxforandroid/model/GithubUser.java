package com.github.scache.introrxforandroid.model;

import com.google.gson.annotations.SerializedName;

public class GithubUser {
    @SerializedName("login")
    public String login;

    @SerializedName("id")
    public long id;

    @SerializedName("avatar_url")
    public String avatarUrl;

    // TODO 必要なもの追加
}
