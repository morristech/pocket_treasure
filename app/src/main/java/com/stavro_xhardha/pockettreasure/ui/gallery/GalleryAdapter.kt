package com.stavro_xhardha.pockettreasure.ui.gallery

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.stavro_xhardha.pockettreasure.R
import com.stavro_xhardha.pockettreasure.model.UnsplashResult
import com.stavro_xhardha.pockettreasure.ui.news.NetworkStatus
import com.stavro_xhardha.pockettreasure.ui.news.ViewHolderType
import kotlinx.android.synthetic.main.single_item_image.view.*

class GalleryAdapter(val contract: GalleryContract) :
    PagedListAdapter<UnsplashResult, GalleryAdapter.GalleryViewHolder>(DIFF_UTIL) {
    private var viewHolderType: ViewHolderType = ViewHolderType.VIEW_PROGRESS

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder =
        getViewType().let {
            return when (it) {
                ViewHolderType.VIEW_ITEMS -> {
                    GalleryViewHolder(
                        LayoutInflater.from(parent.context).inflate(
                            R.layout.single_item_image,
                            parent,
                            false
                        )
                    )
                }
                ViewHolderType.VIEW_PROGRESS -> GalleryViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.single_item_progress,
                        parent,
                        false
                    )
                )
            }
        }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        getItem(position).let { holder.bind(it, contract) }
    }

    class GalleryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(
            unsplashResult: UnsplashResult?,
            contract: GalleryContract
        ) {
            with(itemView) {
                Picasso.get().load(unsplashResult?.photoUrls?.thumbnailUrl)
                    .fit()
                    .error(R.drawable.no_img_available)
                    .placeholder(R.drawable.img_placeholder)
                    .into(ivUnsplashImage)
                tvImageDescription.text = "By ${unsplashResult?.user?.userFullName ?: "Anonymous"}"

                flImageHolder.setOnClickListener {
                    contract.onImageHolderClicked(unsplashResult?.photoUrls?.raw!!)
                }
            }
        }
    }

    fun setCurrentStatus(it: NetworkStatus?) {
        if (it != null) {
            when (it) {
                NetworkStatus.LOADING -> setViewType(ViewHolderType.VIEW_PROGRESS)
                NetworkStatus.SUCCESS -> setViewType(ViewHolderType.VIEW_ITEMS)
            }
        }
    }

    private fun setViewType(viewHolderType: ViewHolderType) {
        this.viewHolderType = viewHolderType
    }

    private fun getViewType(): ViewHolderType = viewHolderType
}

val DIFF_UTIL = object : DiffUtil.ItemCallback<UnsplashResult>() {
    override fun areItemsTheSame(oldItem: UnsplashResult, newItem: UnsplashResult): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: UnsplashResult, newItem: UnsplashResult): Boolean =
        oldItem.id == newItem.id && oldItem.description == newItem.description
                && oldItem.altDescription == newItem.description
                && oldItem.photoUrls == newItem.photoUrls
}