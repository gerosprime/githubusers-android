package com.glennrosspascual.githubusers.http

import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit

class GithubApiRetrofitTest {

    private lateinit var subject : GithubApiRetrofit


    @Before
    fun init() {
        subject = GithubApiRetrofit(Retrofit.Builder())
    }

    @Test
    fun githubWebService() {
        val webService = subject.githubWebService
        val content = webService.loadUsers(0, 20)
        content.execute().body()
    }

}