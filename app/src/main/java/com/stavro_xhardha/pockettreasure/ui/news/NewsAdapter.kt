package com.stavro_xhardha.pockettreasure.ui.news

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.stavro_xhardha.pockettreasure.R
import com.stavro_xhardha.pockettreasure.model.News
import kotlinx.android.synthetic.main.single_item_news.view.*

class NewsAdapter(private val newsAdapterContract: NewsAdapterContract) :
    PagedListAdapter<News, NewsAdapter.NewsViewHolder>(DiffUtilCallBack()) {
    private var viewHolderType: ViewHolderType = ViewHolderType.VIEW_PROGRESS

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        getViewType().let {
            return when (it) {
                ViewHolderType.VIEW_ITEMS -> {
                    val view = LayoutInflater.from(parent.context).inflate(R.layout.single_item_news, parent, false)
                    NewsViewHolder(view)
                }
                ViewHolderType.VIEW_PROGRESS -> {
                    val view = LayoutInflater.from(parent.context).inflate(R.layout.single_item_progress, parent, false)
                    NewsViewHolder(view)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        getItem(position).let { holder.bind(it, newsAdapterContract) }
    }

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(
            news: News?,
            newsAdapterContract: NewsAdapterContract
        ) {
            with(itemView) {
                tvTitle.text = news?.title
                tvAuthor.text = "By ${news?.author ?: "Anonymous"}"
                tvDescription.text = news?.description
                tvSource.text = news?.newsSource?.name

                Picasso.get().load(news?.urlOfImage).fit()
                    .placeholder(R.drawable.no_img_available).error(R.drawable.no_img_available).into(ivPostImage)

                cvCurrentNews.setOnClickListener {
                    val currentUrl = news?.url
                    if (currentUrl != null) {
                        newsAdapterContract.onCurrentNewsClick(currentUrl)
                    }
                }

                ivShare.setOnClickListener {
                    val currentUrl = news?.url
                    val currentTitle = news?.title
                    if (currentUrl != null && currentTitle != null)
                        newsAdapterContract.onShareClick(currentUrl, currentTitle)
                }
            }
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

    fun setCurrentStatus(it: NetworkStatus?) {
        when (it) {
            NetworkStatus.FAILED -> newsAdapterContract.showErrorMessage()
            NetworkStatus.LOADING -> setViewType(ViewHolderType.VIEW_PROGRESS)
            NetworkStatus.SUCCESS -> setViewType(ViewHolderType.VIEW_ITEMS)
        }
    }

    private fun setViewType(viewHolderType: ViewHolderType) {
        this.viewHolderType = viewHolderType
    }

    private fun getViewType(): ViewHolderType = viewHolderType
}