package com.example.rhythmic.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.rhythmic.R
import com.example.rhythmic.data.entities.Song

class VerticalAdapter(var songList: List<Song>, viewModel: ViewModel) : RecyclerView.Adapter<VerticalAdapter.VerticalViewHolder>(){
        inner class VerticalViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

        override fun getItemCount(): Int = songList.size

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VerticalViewHolder {
                val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.vertical_item, parent, false)
                return VerticalViewHolder(view)
        }

        override fun onBindViewHolder(holder: VerticalViewHolder, position: Int) {
                val currentSong = songList[position]
        }

}