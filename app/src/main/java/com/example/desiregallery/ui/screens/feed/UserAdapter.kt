package com.example.desiregallery.ui.screens.feed

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.desiregallery.R
import com.example.desiregallery.data.models.User
import com.example.desiregallery.ui.screens.base.BaseAdapter
import com.example.desiregallery.ui.screens.base.BaseViewHolder

/**
 * @author babaetskv on 09.12.19
 */
class UserAdapter(override val items: List<User>) : BaseAdapter<User>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<User> {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }
}
