package com.example.desiregallery.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.desiregallery.R
import com.example.desiregallery.Utils
import com.example.desiregallery.models.Comment
import kotlinx.android.synthetic.main.item_comment.view.*

class CommentsAdapter(private val items: List<Comment>, val context: Context) : RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): CommentsAdapter.ViewHolder {
        return CommentsAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: CommentsAdapter.ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val textView = view.item_comment_text
        private val timestampView = view.item_comment_timestamp

        fun bind(item: Comment) {
            textView.text = item.text
            timestampView.text = Utils.dateToString(item.timestamp)
        }
    }
}