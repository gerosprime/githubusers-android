package com.glennrosspascual.githubusers.interactors

import com.glennrosspascual.githubusers.model.GithubConnectionPinger
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.Disposable

class GithubConnectionCheckUseCase(private val pinger: GithubConnectionPinger,
                                   uiScheduler: Scheduler? = null,
                                   networkScheduler: Scheduler? = null,
                                   ioScheduler: Scheduler? = null)
    : UseCase(uiScheduler, ioScheduler, networkScheduler) {

    var error: (Throwable) -> Unit = {}
    var result: (Boolean) -> Unit = {}
    var complete: () -> Unit = {}

    private var disposable = Disposable.disposed()

    fun startConnectionListening() {
        if (disposable.isDisposed) {
            disposable = setupSchedulersObservable(pinger.ping(), networkScheduler)
                .subscribe(result, error, complete)
        }
    }

    fun stopConnectionListening() {
        disposable.dispose()
    }
}