package com.example.rhythmic.domain.util

import androidx.recyclerview.widget.DiffUtil
import com.example.rhythmic.data.entities.Song

class SongDiffUtil(
        private val oldList : List<Song>,
        private val newList : List<Song>
) : DiffUtil.Callback(){
        override fun getOldListSize(): Int  = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = oldList[oldItemPosition].id == newList[newItemPosition].id

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = when{
                oldList[oldItemPosition].id !=newList[newItemPosition].id->false
                oldList[oldItemPosition].title !=newList[newItemPosition].title->false
                oldList[oldItemPosition].album!=newList[newItemPosition].album->false
                oldList[oldItemPosition].artist!=newList[newItemPosition].artist->false
                oldList[oldItemPosition].duration!=newList[newItemPosition].duration->false
                oldList[oldItemPosition].path!=newList[newItemPosition].path->false
                oldList[oldItemPosition].imagePath!=newList[newItemPosition].imagePath->false
                oldList[oldItemPosition].isRecent !=newList[newItemPosition].isRecent->false
                oldList[oldItemPosition].isLiked !=newList[newItemPosition].isLiked->false
                else ->true
        }
}