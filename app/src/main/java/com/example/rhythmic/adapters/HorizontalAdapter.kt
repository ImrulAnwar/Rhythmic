package com.example.rhythmic.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rhythmic.R
import com.example.rhythmic.data.entities.Song
import com.example.rhythmic.databinding.HorizontalItemBinding
import com.example.rhythmic.domain.util.SongDiffUtil

class HorizontalAdapter(val context: Context, val from: String) :
        RecyclerView.Adapter<HorizontalAdapter.HorizontalViewHolder>() {
        private var songList = emptyList<Song>()
                inner class HorizontalViewHolder(val binding: HorizontalItemBinding): RecyclerView.ViewHolder(binding.root)

        override fun getItemCount(): Int = songList.size

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorizontalAdapter.HorizontalViewHolder {
                val view = HorizontalItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false

                )
                return HorizontalViewHolder(view)



        }

        override fun onBindViewHolder(holder: HorizontalAdapter.HorizontalViewHolder , position: Int) {


                val currentSong = songList[position]
                if (from == "AlbumFragment")
                        holder.binding.tvAlbumName.text = currentSong.album
                else
                        holder.binding.tvAlbumName.text = currentSong.artist
                //scroll animation
                AnimationUtils.loadAnimation(
                        holder.itemView.context,
                        android.R.anim.fade_in
                ).also {
                        holder.itemView.startAnimation(it)
                }

                Glide.with(context).load(currentSong.imagePath)
                        .placeholder(R.mipmap.ic_launcher).centerCrop()
                        .into(holder.binding.ivArtistArt)
        }

        fun setData(newSongList: List<Song>) {
                val diffUtil = SongDiffUtil(songList, newSongList)
                val diffResults = DiffUtil.calculateDiff(diffUtil)
                songList = newSongList
                diffResults.dispatchUpdatesTo(this)
        }

}