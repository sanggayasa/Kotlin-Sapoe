package com.akarinti.sapoe.extension

import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

/**
 * Created by ridwan on 18/02/2018.
 */
fun CompositeDisposable?.safeDispose() {
    this?.clear()
}

fun <T : Any> Flowable<T>.transformCall(): Flowable<T> {
    return this.observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
}

fun <T : Any> Flowable<T>.uiSubscribe(onNext: (T) -> Unit = {},
                                      onError: (Throwable) -> Unit = {},
                                      onComplete: () -> Unit = {}): Disposable {
    return this.transformCall()
            .subscribe(onNext, onError, onComplete)
}

fun <T : Any> Observable<T>.transformCall(): Observable<T> {
    return this.observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .unsubscribeOn(Schedulers.io())
}

fun <T : Any> Observable<T>.uiSubscribe(onNext: (T) -> Unit = {},
                                        onError: (Throwable) -> Unit = {},
                                        onComplete: () -> Unit = {}): Disposable {
    return this.transformCall()
            .subscribe(onNext, onError, onComplete)
}

fun <T : Any> Observable<T>.uiSubscribe(observer: DisposableObserver<T>): Disposable {
    return this.transformCall()
            .subscribeWith(observer)
}