package com.stavro_xhardha.pockettreasure.ui.quran

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.stavro_xhardha.pockettreasure.R
import com.stavro_xhardha.pockettreasure.model.Surah
import kotlinx.android.synthetic.main.single_item_surah.view.*

class QuranAdapter(val quranAdapterContract: QuranAdapterContract) :
    ListAdapter<Surah, QuranAdapter.QuranViewHolder>(DIFF_UTIL) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuranViewHolder =
        QuranViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.single_item_surah, parent, false))


    override fun onBindViewHolder(holder: QuranViewHolder, position: Int) {
        getItem(position).let { holder.bind(it, quranAdapterContract) }
    }


    class QuranViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(
            surah: Surah?,
            quranAdapterContract: QuranAdapterContract
        ) {
            with(itemView) {
                tvSurahTitle.text = "${surah?.surahNumber}. ${surah?.englishName}"
                tvRelevation.text = "Relevation: ${surah?.revelationType}"
                tvarabicSurahName.text = "${surah?.surahArabicName}"

                cvSurah.setOnClickListener {
                    surah?.surahNumber?.let { clickedSurah -> quranAdapterContract.onSurahClicked(clickedSurah) }
                }
            }
        }
    }
}

val DIFF_UTIL = object : DiffUtil.ItemCallback<Surah>() {
    override fun areItemsTheSame(oldItem: Surah, newItem: Surah): Boolean = oldItem.surahNumber == newItem.surahNumber

    override fun areContentsTheSame(oldItem: Surah, newItem: Surah): Boolean =
        oldItem.englishName == newItem.englishName
                && oldItem.englishTranslation == newItem.englishTranslation
                && oldItem.surahArabicName == newItem.surahArabicName
                && oldItem.revelationType == newItem.revelationType
                && oldItem.surahNumber == newItem.surahNumber

}