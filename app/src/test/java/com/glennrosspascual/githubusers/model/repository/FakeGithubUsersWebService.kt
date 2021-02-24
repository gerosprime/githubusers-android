package com.glennrosspascual.githubusers.model.repository

import com.glennrosspascual.githubusers.http.GithubWebService
import com.glennrosspascual.githubusers.model.GithubUser
import retrofit2.Call

class FakeGithubUsersWebService : GithubWebService {

    var error : Boolean = false
    var mockSuccessResponse : List<GithubUser> = listOf()
    lateinit var fakeUser : GithubUser

    override fun loadUsers(offset: Int, limit: Int): Call<List<GithubUser>> {
        return FakeUsersCall(error, mockSuccessResponse)
    }

    override fun loadUserDetail(login: String): Call<GithubUser> {
        val fakeCall = FakeUserDetailCall()
        fakeCall.error = error
        fakeCall.fakeUserDetail = fakeUser
        return fakeCall
    }


}