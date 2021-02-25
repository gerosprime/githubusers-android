package com.glennrosspascual.githubusers.model

import com.glennrosspascual.githubusers.http.GithubWebService
import io.reactivex.rxjava3.core.Observable
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class GithubConnectionPinger(private val url : String) {

    companion object {
        const val TIMEOUT = 5000
    }

    fun ping() : Observable<Boolean> = Observable.create {


        while (!it.isDisposed) {
            try {
                val siteUrl = URL(url)
                val connection = siteUrl.openConnection() as HttpURLConnection
                connection.requestMethod = "GET";
                connection.connectTimeout = TIMEOUT;
                connection.connect();

                val code = connection.responseCode;
                it.onNext(true)
                break
            } catch (exception : Exception) {
                it.onNext(false)
            }
        }
        it.onComplete()

    }
}