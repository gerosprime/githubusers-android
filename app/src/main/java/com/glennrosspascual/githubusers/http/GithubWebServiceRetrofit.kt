package com.glennrosspascual.githubusers.http

import com.glennrosspascual.githubusers.model.GithubUser
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubWebServiceRetrofit : GithubWebService {
    @GET("users")
    override fun loadUsers(@Query("since") offset : Int,
                           @Query("per_page") limit : Int)
            : Call<List<GithubUser>>

    @GET("users/{login}")
    override fun loadUserDetail(@Path("login") login: String) : Call<GithubUser>

}