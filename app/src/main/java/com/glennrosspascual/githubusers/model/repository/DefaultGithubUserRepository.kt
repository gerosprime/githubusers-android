package com.glennrosspascual.githubusers.model.repository

import com.glennrosspascual.githubusers.database.GithubUsersDao
import com.glennrosspascual.githubusers.http.GithubWebService
import com.glennrosspascual.githubusers.model.GithubUser
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.PublishSubject
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class DefaultGithubUserRepository @Inject constructor(private val webService: GithubWebService,
                                                      private val database: GithubUsersDao)
    : GithubUserRepository {

    private val _notePostLiveEvent : PublishSubject<PostNoteResult> = PublishSubject.create()

    override val noteToggleLiveEvent: PublishSubject<PostNoteResult>
        get() = _notePostLiveEvent

    override fun postNotes(notes: String,
                           login: String, index : Int): Single<PostNoteResult> = Single.fromCallable {
            database.updateGithubUser(notes, login)
            PostNoteResult(notes, login, index)
        }.doAfterSuccess { noteToggleLiveEvent.onNext(it) }

    override fun loadGithubUsers(lastItemUserId : Int, currentItems : List<GithubUser>,
                                 pageSize: Int): Observable<List<GithubUser>> = Observable.create {

        val result = mutableListOf<GithubUser>()
        currentItems.run {
            result.addAll(this)
        }

        // Retain our favorited items
        val databaseResult = database.getUsersRecordOffLimit(currentItems.size, pageSize)
        val map = mutableMapOf<String, GithubUser>()

        // Copy a reference to githubusers (From database) in this map for a fast search O(1) runtime.
        // If we want to save memory we may do manual search in list itself with O(n) runtime.
        // Thus, trading memory usage with cpu usage.
        for (githubUser in databaseResult) {
            map[githubUser.login] = githubUser
        }

        if (databaseResult.isNotEmpty()) {
            result.addAll(databaseResult)
            it.onNext(result)
        }

        // Fetch from remote

        val usersRemoteCall =
            webService.loadUsers(lastItemUserId, pageSize)

        val userItemsRemote = usersRemoteCall.execute()
        val itemsRemoteResult = userItemsRemote.body() ?: listOf()

        // If we get items. We attach database only fields(notes and record id)
        // Then insert back into database
        if (itemsRemoteResult.isNotEmpty()) {

            // If we have an existing database items
            // We will just copy the details from the remote version
            for (userFromRemote in itemsRemoteResult) {
                if (map.containsKey(userFromRemote.login)) {
                    val githubUserFromDB = map[userFromRemote.login]
                    githubUserFromDB?.run {
                        userFromRemote.recordId = this.recordId
                        userFromRemote.notes = this.notes
                        copyDetailsFrom(userFromRemote)
                    }
                } else {
                    // If there's no existing database record, we still just add it in list
                    result.add(userFromRemote)
                }
            }
            // Still, update the database record or insert new ones.
            database.insertUsers(itemsRemoteResult)

            it.onNext(result)
        }

        // If we get empty on both data source, we will give an error
        if (!userItemsRemote.isSuccessful) {
            if (result.isEmpty()) {
                throw HttpException(userItemsRemote)
            }
        }


        it.onComplete()
    }

    override fun loadGithubUser(login: String): Single<GithubUser> = Single.fromCallable {

        var result = database.getGithubUserRecord(login)

        // Fetch from remote
        val usersRemoteCall = webService.loadUserDetail(login)

        try {
            val userItemsRemote = usersRemoteCall.execute()
            val itemsRemoteResult = userItemsRemote.body()

            // If we get items. We attach database only fields(notes and record id)
            // Then insert back into database
            itemsRemoteResult?.run {
                notes = result.notes
                recordId = result.recordId
                database.insertUsers(listOf(this))
                result = this
            }

        } catch (exception : Exception) {
            throw exception
        }

        result

    }

    override fun search(term: String): Single<List<GithubUser>> = Single.fromCallable {
        database.searchUsersByUsernameOrNotes(term, "%$term%")
    }
}