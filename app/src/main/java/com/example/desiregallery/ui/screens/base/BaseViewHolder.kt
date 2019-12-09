package com.example.desiregallery.ui.screens.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * @author babaetskv on 06.12.19
 */
abstract class BaseViewHolder<T : Any>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun bind(item: T)
}
