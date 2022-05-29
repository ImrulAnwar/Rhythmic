package com.example.rhythmic.ui.activities.main_activity.fragments.bottom_nav.home.top_nav.album

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.rhythmic.databinding.FragmentAlbumBinding

class AlbumFragment : Fragment() {

        private var _binding: FragmentAlbumBinding? = null
        private val binding get() = _binding!!

        override fun onCreateView(
                inflater: LayoutInflater, container: ViewGroup?,
                savedInstanceState: Bundle?
        ): View? {
                _binding = FragmentAlbumBinding.inflate(inflater, container, false)
                val root: View = binding.root

                return root
        }
}