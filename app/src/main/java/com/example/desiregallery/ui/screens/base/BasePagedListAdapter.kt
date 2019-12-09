package com.example.desiregallery.ui.screens.base

import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil

/**
 * @author babaetskv on 06.12.19
 */
abstract class BasePagedListAdapter<T : Any>(callback: DiffUtil.ItemCallback<T>) :
    PagedListAdapter<T, BaseViewHolder<T>>(callback) {

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {
        val item = getItem(position) as T
        holder.bind(item)
    }
}
