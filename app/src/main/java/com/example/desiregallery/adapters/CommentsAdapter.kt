package com.example.desiregallery.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.desiregallery.R
import com.example.desiregallery.Utils
import com.example.desiregallery.models.Comment
import kotlinx.android.synthetic.main.item_comment.view.*

class CommentsAdapter(
    private val items: List<Comment>,
    private val context: Context
) : RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val textView = view.item_comment_text
        private val datetimeView = view.item_comment_time

        fun bind(item: Comment) {
            textView.text = item.text
            datetimeView.text = Utils.formatDate(view.context, item.datetime)
        }
    }
}