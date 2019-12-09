package com.example.desiregallery.ui.screens.base

import androidx.recyclerview.widget.RecyclerView

/**
 * @author babaetskv on 06.12.19
 */
abstract class BaseAdapter<T : Any> : RecyclerView.Adapter<BaseViewHolder<T>>() {
    protected abstract val items: List<T>

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {
        val item = items[position]
        holder.bind(item)
    }
}
