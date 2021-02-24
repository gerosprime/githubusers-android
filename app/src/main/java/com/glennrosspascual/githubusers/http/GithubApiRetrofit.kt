package com.glennrosspascual.githubusers.http

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class GithubApiRetrofit(retrofitBuilder: Retrofit.Builder)
    : GithubApi {

    private val retrofit = retrofitBuilder
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("https://api.github.com/")
        .build()

    private val githubWS : GithubWebService by lazy {
        retrofit.create(GithubWebServiceRetrofit::class.java)
    }

    override val githubWebService : GithubWebService = githubWS

}