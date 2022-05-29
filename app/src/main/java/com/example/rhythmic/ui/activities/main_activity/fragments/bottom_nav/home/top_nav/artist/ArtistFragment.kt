package com.example.rhythmic.ui.activities.main_activity.fragments.bottom_nav.home.top_nav.artist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.rhythmic.databinding.FragmentArtistBinding

class ArtistFragment : Fragment() {

        private var _binding: FragmentArtistBinding? = null
        private val binding get() = _binding!!

        override fun onCreateView(
                inflater: LayoutInflater, container: ViewGroup?,
                savedInstanceState: Bundle?
        ): View {
                _binding = FragmentArtistBinding.inflate(inflater, container, false)
                val root: View = binding.root

                return root
        }
}