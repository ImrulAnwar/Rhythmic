package com.example.rhythmic.ui.activities.main_activity.fragments.bottom_nav.home.top_nav.artist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rhythmic.adapters.HorizontalAdapter
import com.example.rhythmic.databinding.FragmentArtistBinding
import com.example.rhythmic.ui.activities.main_activity.fragments.bottom_nav.home.top_nav.album.AlbumViewModel
import com.example.rhythmic.ui.activities.main_activity.fragments.bottom_nav.home.top_nav.album.ArtistViewModel
import dagger.hilt.android.AndroidEntryPoint

private const val  TAG = "ArtistFragment"
@AndroidEntryPoint
class ArtistFragment : Fragment(){

        private var _binding: FragmentArtistBinding? = null
        private val binding get() = _binding!!
        private val  adapter: HorizontalAdapter by lazy { HorizontalAdapter(requireContext(), TAG) }
        private lateinit var artistViewModel: ArtistViewModel

        override fun onCreateView(
                inflater: LayoutInflater, container: ViewGroup?,
                savedInstanceState: Bundle?
        ): View {
                _binding = FragmentArtistBinding.inflate(inflater, container, false)
                val root: View = binding.root
                artistViewModel = ViewModelProvider(this)[ArtistViewModel::class.java]
                binding.rvArtistList.adapter = adapter
                binding.rvArtistList.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
                artistViewModel.getAllArtists().observe(viewLifecycleOwner){
                        adapter.setData(it)
                }
                return root
        }
}