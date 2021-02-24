package com.glennrosspascual.githubusers.interactors


import com.glennrosspascual.githubusers.model.GithubUser
import com.glennrosspascual.githubusers.model.repository.GithubUserRepository
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.Disposable

class GithubUsersLoadUseCase(private val repository: GithubUserRepository,
                             uiScheduler: Scheduler? = null,
                             networkScheduler: Scheduler? = null,
                             ioScheduler: Scheduler? = null)
    : UseCase(uiScheduler, ioScheduler, networkScheduler) {

    private var disposable = Disposable.disposed()

    var error: (Throwable) -> Unit = {}
    var result: (List<GithubUser>) -> Unit = {}
    var complete: () -> Unit = {}

    fun resetListeners() {
        error = {}
        result = {}
        complete = {}
    }

    fun loadInitial(lastUserId : Int, pageSize : Int, githubUsers: List<GithubUser>) {
        disposable.dispose()
        disposable = setupSchedulersObservable(repository.loadGithubUsers(lastUserId, githubUsers, pageSize),
            networkScheduler).subscribe(result, error, complete)
    }

    fun stop() {
        disposable.dispose()
    }


}