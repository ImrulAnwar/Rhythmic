package com.example.rhythmic.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewParent
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rhythmic.R
import com.example.rhythmic.data.entities.Song
import com.example.rhythmic.databinding.VerticalItemBinding
import com.example.rhythmic.di.AdapterEntryPoint
import com.example.rhythmic.domain.repo.Repository
import com.example.rhythmic.domain.util.SongDiffUtil
import com.example.rhythmic.ui.activities.now_playing_activity.NowPlayingActivity
import com.example.rhythmic.ui.activities.now_playing_activity.NowPlayingViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors
import javax.inject.Inject

class VerticalAdapter @Inject constructor(
        val context: Activity,
        val from: String
) :
        RecyclerView.Adapter<VerticalAdapter.VerticalViewHolder>() {
        private lateinit var nowPlayingViewModel: NowPlayingViewModel
        private var songList = emptyList<Song>()
        private val utilitiesEntryPoint =
                EntryPointAccessors.fromApplication(
                        context.applicationContext, AdapterEntryPoint::class.java)
        val repository = utilitiesEntryPoint.repository

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
                val currentSong =songList[position]
                holder.binding.tvSongTitle.text = currentSong.title
                holder.binding.tvDuration.text = currentSong.artist

                AnimationUtils.loadAnimation(
                        holder.itemView.context,
                        android.R.anim.fade_in
                ).also {
                        holder.itemView.startAnimation(it)
                }

                Glide.with(context).load(currentSong.imagePath)
                        .placeholder(R.mipmap.ic_launcher).centerCrop()
                        .into(holder.binding.ivAlbumArt)

                holder.itemView.setOnClickListener {
                        repository.setCurrentSongList(songList)
                        Intent(context, NowPlayingActivity::class.java).also {
                                for (i in songList.indices)
                                        it.putExtra("position", position)
                                context.startActivity(
                                        it
                                )
                        }
                }

        }

        fun setData(newSongList: List<Song>) {
                val diffUtil = SongDiffUtil(songList, newSongList)
                val diffResults = DiffUtil.calculateDiff(diffUtil)
                songList = newSongList
                diffResults.dispatchUpdatesTo(this)
        }

}