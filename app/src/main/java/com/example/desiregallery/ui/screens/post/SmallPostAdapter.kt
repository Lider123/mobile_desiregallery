package com.example.desiregallery.ui.screens.post

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.desiregallery.R
import com.example.desiregallery.data.models.Post
import com.example.desiregallery.databinding.ItemPostSmallBinding
import com.example.desiregallery.ui.screens.base.BaseAdapter

/**
 * @author babaetskv on 06.11.19
 */
class SmallPostAdapter(override val items: List<Post>) : BaseAdapter<Post>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmallPostViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return DataBindingUtil.inflate<ItemPostSmallBinding>(
            inflater,
            R.layout.item_post_small,
            parent,
            false
        ).let {
            SmallPostViewHolder(it)
        }
    }
}
