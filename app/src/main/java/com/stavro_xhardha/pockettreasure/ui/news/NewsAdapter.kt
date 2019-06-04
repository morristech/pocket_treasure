package com.stavro_xhardha.pockettreasure.ui.news

import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.stavro_xhardha.pockettreasure.model.News

class NewsAdapter : PagedListAdapter<News, NewsAdapter.NewsViewHolder>(DiffUtilCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        //return NewsViewHolder(null)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
    }

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    }
}

class DiffUtilCallBack : DiffUtil.ItemCallback<News>() {
    override fun areItemsTheSame(oldItem: News, newItem: News): Boolean = oldItem.title == newItem.title

    override fun areContentsTheSame(oldItem: News, newItem: News): Boolean {
        return oldItem.title == newItem.title
                && oldItem.author == newItem.author
                && oldItem.content == newItem.content
                && oldItem.urlOfImage == newItem.urlOfImage
                && oldItem.description == newItem.description
    }
}