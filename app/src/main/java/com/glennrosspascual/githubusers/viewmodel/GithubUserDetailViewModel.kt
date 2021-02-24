package com.glennrosspascual.githubusers.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.glennrosspascual.githubusers.model.GithubUser
import com.glennrosspascual.githubusers.model.Result
import com.glennrosspascual.githubusers.model.repository.GithubUserRepository
import com.glennrosspascual.githubusers.model.repository.PostNoteResult
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.Disposable
import javax.inject.Inject
import javax.inject.Named

class GithubUserDetailViewModel
        @Inject constructor(private val repository: GithubUserRepository,
                            @Named("uiScheduler") private var uiScheduler: Scheduler?,
                            @Named("ioScheduler") private var ioScheduler : Scheduler?) : ViewModel() {

    val githubUserLiveData : LiveData<Result<GithubUser>>
        get() = itemsMLD

    private val itemsMLD = MutableLiveData<Result<GithubUser>>()

    val noteResultLiveData : LiveData<Result<PostNoteResult>>
        get() = postUserNoteResultMLD

    private val postUserNoteResultMLD = MutableLiveData<Result<PostNoteResult>>()

    private var disposableLoad = Disposable.disposed()
    private var disposableFavorite = Disposable.disposed()

    fun loadGithubUser(login : String) {
        itemsMLD.value = Result.Loading

        var usersLoad = repository.loadGithubUser(login)

        ioScheduler?.run {
            usersLoad = usersLoad.subscribeOn(this)
        }

        uiScheduler?.run {
            usersLoad = usersLoad.observeOn(this)
        }

        disposableLoad = usersLoad.subscribe(this::githubUsersLoaded, this::githubUsersLoadError)

    }

    private fun githubUsersLoaded(github : GithubUser) {
        itemsMLD.value = Result.Success(github)
    }

    private fun githubUsersLoadError(error : Throwable) {
        itemsMLD.value = Result.Error(error as Exception)
    }

    fun updateNotes(notes : String = "", login : String, index : Int) {
        postUserNoteResultMLD.value = Result.Loading

        var postFavorite = repository.postNotes(notes, login, index)

        ioScheduler?.run {
            postFavorite = postFavorite.subscribeOn(this)
        }

        uiScheduler?.run {
            postFavorite = postFavorite.observeOn(this)
        }

        disposableLoad = postFavorite.subscribe(this::userNotesPosted, this::userNotesPostError)
    }

    private fun userNotesPostError(error: Throwable) {
        postUserNoteResultMLD.value = Result.Error(error)
    }

    private fun userNotesPosted(result : PostNoteResult) {
        postUserNoteResultMLD.value = Result.Success(result)
    }

}