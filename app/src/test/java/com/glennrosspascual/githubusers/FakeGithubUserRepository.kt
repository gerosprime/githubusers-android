package com.glennrosspascual.githubusers

import com.glennrosspascual.githubusers.model.GithubUser
import com.glennrosspascual.githubusers.model.repository.GithubUserRepository
import com.glennrosspascual.githubusers.model.repository.PostNoteResult
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.PublishSubject

class FakeGithubUserRepository : GithubUserRepository {

    override fun loadGithubUsers(
        lastItemUserId: Int,
        currentItems: List<GithubUser>,
        pageSize: Int
    ): Observable<List<GithubUser>> = Observable.just(listOf())

    override fun loadGithubUser(login: String): Single<GithubUser> {
        TODO("Not yet implemented")
    }

    override val noteToggleLiveEvent: PublishSubject<PostNoteResult>
        get() = PublishSubject.create()

    override fun postNotes(
        notes: String,
        login: String,
        index: Int
    ): Single<PostNoteResult> {
        TODO("Not yet implemented")
    }

    override fun search(term: String): Single<List<GithubUser>> {
        TODO("Not yet implemented")
    }
}