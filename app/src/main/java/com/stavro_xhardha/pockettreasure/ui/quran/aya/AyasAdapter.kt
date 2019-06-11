package com.stavro_xhardha.pockettreasure.ui.quran.aya

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.stavro_xhardha.pockettreasure.R
import com.stavro_xhardha.pockettreasure.model.Aya
import kotlinx.android.synthetic.main.single_item_aya.view.*

class AyasAdapter : PagedListAdapter<Aya, AyasAdapter.AyasViewHolder>(DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AyasViewHolder =
        AyasViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.single_item_aya, parent, false))

    override fun onBindViewHolder(holder: AyasViewHolder, position: Int) {
        getItem(position).let { holder.bind(it) }
    }


    class AyasViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(aya: Aya?) = with(itemView) {
            tvAyaText.text = aya?.ayatText
        }
    }
}

val DIFF_UTIL = object : DiffUtil.ItemCallback<Aya>() {
    override fun areItemsTheSame(oldItem: Aya, newItem: Aya): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Aya, newItem: Aya): Boolean =
        oldItem.ayatText == newItem.ayatText
                && oldItem.audioUrl == newItem.audioUrl
                && oldItem.id == newItem.id

}