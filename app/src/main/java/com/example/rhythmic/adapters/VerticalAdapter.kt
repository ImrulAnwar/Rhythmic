package com.example.rhythmic.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.rhythmic.data.entities.Song
import com.example.rhythmic.databinding.VerticalItemBinding
import com.example.rhythmic.domain.util.SongDiffUtil

class VerticalAdapter(var songList: List<Song>) :
        RecyclerView.Adapter<VerticalAdapter.VerticalViewHolder>() {
        inner class VerticalViewHolder(val binding: VerticalItemBinding) :
                RecyclerView.ViewHolder(binding.root)

        override fun getItemCount(): Int = songList.size

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VerticalViewHolder {
                val view = VerticalItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                )
                return VerticalViewHolder(view)
        }

        override fun onBindViewHolder(holder: VerticalViewHolder, position: Int) {
                val currentSong = songList[position]
                holder.binding.tvSongTitle.text = currentSong.title
                holder.binding.tvDuration.text = currentSong.duration
        }

        fun setData(newSongList: List<Song>) {
                val diffUtil = SongDiffUtil(songList, newSongList)
                val diffResults = DiffUtil.calculateDiff(diffUtil)
                songList = newSongList
                diffResults.dispatchUpdatesTo(this)
        }

}