package com.glennrosspascual.githubusers.rx

import dagger.Module
import dagger.Provides
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.Executors
import javax.inject.Named
import javax.inject.Singleton

@Module
class RxModule {

    companion object {
        const val MAX_THREADS = 1
    }

    @Provides
    @Named("ioScheduler")
    @Singleton
    fun provideIOScheduler() : Scheduler? = Schedulers.from(Executors.newFixedThreadPool(MAX_THREADS))

    @Provides
    @Named("networkScheduler")
    @Singleton
    fun provideNetworkScheduler() : Scheduler? = Schedulers.from(Executors.newFixedThreadPool(MAX_THREADS))

    @Provides
    @Named("uiScheduler")
    fun provideUIScheduler() : Scheduler? = AndroidSchedulers.mainThread()


}