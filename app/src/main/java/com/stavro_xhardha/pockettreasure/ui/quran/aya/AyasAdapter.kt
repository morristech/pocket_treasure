package com.stavro_xhardha.pockettreasure.ui.quran.aya

import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.stavro_xhardha.pockettreasure.R
import com.stavro_xhardha.pockettreasure.brain.DIFF_UTIL_AYA
import com.stavro_xhardha.pockettreasure.brain.FIRST_SURAH_SENTENCE
import com.stavro_xhardha.pockettreasure.model.Aya
import kotlinx.android.synthetic.main.single_item_aya.view.*

class AyasAdapter() :
    PagedListAdapter<Aya, AyasAdapter.AyasViewHolder>(DIFF_UTIL_AYA) {
    val mediaPlayer = MediaPlayer()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AyasViewHolder =
        AyasViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.single_item_aya, parent, false))

    override fun onBindViewHolder(holder: AyasViewHolder, position: Int) {
        getItem(position).let { holder.bind(it, mediaPlayer) }
    }

    class AyasViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            aya: Aya?,
            mediaPlayer: MediaPlayer
        ) = with(itemView) {
            if (aya?.ayatText!!.contains(FIRST_SURAH_SENTENCE) && aya.surahNumber != 1) {
                val newAya = aya.ayatText.substring(FIRST_SURAH_SENTENCE.length, aya.ayatText.length)
                tvAyaText.text = newAya
            } else
                tvAyaText.text = aya.ayatText

            tvAyaNumber.text = "${aya.ayatNumber}."

            ivPlayImage.setOnClickListener {
                ivPlayImage.setImageResource(R.drawable.ic_stop_black_24dp)
                if (mediaPlayer.isPlaying) {
                    mediaPlayer.stop()
                    ivPlayImage.setImageResource(R.drawable.ic_play_arrow_black_24dp)
                } else {

                    try {
                        mediaPlayer.setDataSource(aya.audioUrl.replace("http", "https"))
                        mediaPlayer.prepare()
                    } catch (exception: Exception) {
                        exception.printStackTrace()
                    } finally {
                        mediaPlayer.start()
                    }

                    mediaPlayer.setOnCompletionListener {
                        ivPlayImage.setImageResource(R.drawable.ic_play_arrow_black_24dp)
                    }
                }
            }

        }
    }
}