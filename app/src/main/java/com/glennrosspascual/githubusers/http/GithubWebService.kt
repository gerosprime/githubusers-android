package com.glennrosspascual.githubusers.http

import com.glennrosspascual.githubusers.model.GithubUser
import retrofit2.Call

interface GithubWebService {
    fun loadUsers(offset : Int,
                  limit : Int) : Call<List<GithubUser>>
    fun loadUserDetail(login : String) : Call<GithubUser>

}