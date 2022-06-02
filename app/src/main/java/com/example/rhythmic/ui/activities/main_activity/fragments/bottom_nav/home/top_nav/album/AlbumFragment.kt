package com.example.rhythmic.ui.activities.main_activity.fragments.bottom_nav.home.top_nav.album

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rhythmic.adapters.HorizontalAdapter
import com.example.rhythmic.databinding.FragmentAlbumBinding
import dagger.hilt.android.AndroidEntryPoint

private const val  TAG = "AlbumFragment"
@AndroidEntryPoint
class AlbumFragment : Fragment() {

        private var _binding: FragmentAlbumBinding? = null
        private val binding get() = _binding!!
        private val  adapter: HorizontalAdapter by lazy { HorizontalAdapter(requireContext(), TAG) }
        private lateinit var albumViewModel: AlbumViewModel


        override fun onCreateView(
                inflater: LayoutInflater, container: ViewGroup?,
                savedInstanceState: Bundle?
        ): View {
                _binding = FragmentAlbumBinding.inflate(inflater, container, false)
                val root: View = binding.root
                albumViewModel = ViewModelProvider(this)[AlbumViewModel::class.java]
                binding.rvAlbumList.adapter = adapter
                binding.rvAlbumList.layoutManager = LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false)
                albumViewModel.getAllAlbums().observe(viewLifecycleOwner) {
                        adapter.setData(it)
                }
                return root
        }
}