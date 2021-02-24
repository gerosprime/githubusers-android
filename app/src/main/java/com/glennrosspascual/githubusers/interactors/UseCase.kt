package com.glennrosspascual.githubusers.interactors

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single

abstract class UseCase(protected var uiScheduler: Scheduler? = null,
                       protected var ioScheduler: Scheduler? = null,
                       protected var networkScheduler: Scheduler? = null) {

    protected fun cancel() {

    }

    protected fun <T> setupSchedulersSingle(singleSource: Single<T>, backgroundScheduler: Scheduler?): Single<T> {
        var result = singleSource
        backgroundScheduler?.run {
            result = result.subscribeOn(this)
        }

        uiScheduler?.run {
            result = result.observeOn(this)
        }

        return result
    }

    protected fun <T> setupSchedulersObservable(observable: Observable<T>,
                                                backgroundScheduler: Scheduler?): Observable<T> {

        var result = observable
        backgroundScheduler?.run {
            result = result.subscribeOn(this)
        }

        uiScheduler?.run {
            result = result.observeOn(this)
        }

        return result
    }


}
