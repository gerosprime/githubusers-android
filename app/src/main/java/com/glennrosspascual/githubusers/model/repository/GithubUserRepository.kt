package com.glennrosspascual.githubusers.model.repository

import com.glennrosspascual.githubusers.model.GithubUser
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.PublishSubject

interface GithubUserRepository {

    val noteToggleLiveEvent : PublishSubject<PostNoteResult>

    fun loadGithubUsers(lastItemUserId : Int, currentItems : List<GithubUser> = listOf(), pageSize : Int)
            : Observable<List<GithubUser>>

    fun loadGithubUser(login: String): Single<GithubUser>

    fun postNotes(notes : String = "", login: String, index : Int) : Single<PostNoteResult>

    fun search(term : String)
        : Single<List<GithubUser>>

}