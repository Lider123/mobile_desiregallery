package com.example.desiregallery.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.desiregallery.R
import kotlinx.android.synthetic.main.item_comment.view.*

class CommentsAdapter(
    private val items: List<String>,
    private val context: Context
) : androidx.recyclerview.widget.RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): CommentsAdapter.ViewHolder {
        return CommentsAdapter.ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: CommentsAdapter.ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    class ViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        private val textView = view.item_comment_text_view

        fun bind(item: String) {
            textView.text = item
        }
    }
}