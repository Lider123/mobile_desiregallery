package com.example.desiregallery.ui.screens.base

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.desiregallery.data.network.RequestState

/**
 * @author babaetskv on 06.12.19
 */
abstract class BaseDataSource<T> : PageKeyedDataSource<Long, T>() {
    val state: MutableLiveData<RequestState> = MutableLiveData()

    fun updateState(state: RequestState) = this.state.postValue(state)
}
