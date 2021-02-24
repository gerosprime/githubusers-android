package com.glennrosspascual.githubusers.model.repository

import com.glennrosspascual.githubusers.model.GithubUser
import okhttp3.Request
import okhttp3.ResponseBody
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FakeUsersCall(private val error: Boolean,
                    private val fakeList : List<GithubUser> = listOf()) : Call<List<GithubUser>> {

    override fun clone(): Call<List<GithubUser>> {
        TODO("Not yet implemented")
    }

    override fun execute(): Response<List<GithubUser>> {
        return if (error) {
            Response.error(400, ResponseBody.create(null, "Mock error"))
        } else {
            Response.success(fakeList)
        }
    }

    override fun enqueue(callback: Callback<List<GithubUser>>) {
        TODO("Not yet implemented")
    }

    override fun isExecuted(): Boolean {
        TODO("Not yet implemented")
    }

    override fun cancel() {
        TODO("Not yet implemented")
    }

    override fun isCanceled(): Boolean {
        TODO("Not yet implemented")
    }

    override fun request(): Request {
        TODO("Not yet implemented")
    }

    override fun timeout(): Timeout {
        TODO("Not yet implemented")
    }
}