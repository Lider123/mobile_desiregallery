package com.example.desiregallery.auth

import io.reactivex.subjects.PublishSubject

/**
 * @author babaetskv on 20.09.19
 */
class AccountProvider {
    var currAccount: IAccount? = null
        set(value) {
            mObservable.onNext(Wrapper(value))
            field = value
        }
    val mObservable = PublishSubject.create<Wrapper<IAccount>>()

    data class Wrapper<T>(val value: T?)
}
