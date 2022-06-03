package com.example.rhythmic.ui.activities.main_activity.fragments.bottom_nav.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rhythmic.R
import com.example.rhythmic.adapters.VerticalAdapter
import com.example.rhythmic.data.entities.Song
import com.example.rhythmic.databinding.FragmentHomeBinding
import com.example.rhythmic.domain.util.UIFunctions
import com.example.rhythmic.ui.activities.main_activity.fragments.bottom_nav.home.top_nav.album.AlbumFragment
import com.example.rhythmic.ui.activities.main_activity.fragments.bottom_nav.home.top_nav.artist.ArtistFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

        @Inject
        lateinit var uiFunctions: UIFunctions
        private var _binding: FragmentHomeBinding? = null
        private val binding get() = _binding!!
        private lateinit var homeViewModel: HomeViewModel
        private lateinit var allSongs: LiveData<List<Song>>
        private val adapter: VerticalAdapter by lazy { VerticalAdapter(requireActivity()) }
        var artistFragment: ArtistFragment? = null
        var albumFragment: AlbumFragment? = null
        private var fm: FragmentManager? = null

        override fun onCreateView(
                inflater: LayoutInflater,
                container: ViewGroup?,
                savedInstanceState: Bundle?
        ): View {
                albumFragment = AlbumFragment()
                artistFragment = ArtistFragment()
                fm = requireActivity().supportFragmentManager
                uiFunctions.setActionBarLogo(activity = activity as AppCompatActivity)
                homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
                _binding = FragmentHomeBinding.inflate(inflater, container, false)
                val root: View = binding.root

                allSongs = homeViewModel.getAllSongs()

                binding.rvHomeFragment.adapter = adapter
                binding.rvHomeFragment.layoutManager = LinearLayoutManager(requireActivity())

                allSongs.observe(viewLifecycleOwner) {
                        allSongs.value?.let { adapter.setData(it) }
                }

                handleAlbumAndArtistButtonClicks()

                return root
        }

        override fun onDestroyView() {
                super.onDestroyView()
                _binding = null
//                fm = null
                albumFragment = null
                artistFragment = null
        }

        private fun handleAlbumAndArtistButtonClicks() {
                var isAlbumClicked = true
                var isArtistClicked = false

                if (isAlbumClicked) {
                        albumButtonClicked()
                } else if (isArtistClicked) {
                        artistButtonClicked()
                }

                binding.ibArtist.setOnClickListener {
                        if (isAlbumClicked) {
                                artistButtonClicked()
                                isAlbumClicked = false
                                isArtistClicked = true
                        }
                }
                binding.ibAlbum.setOnClickListener {
                        if (isArtistClicked) {
                                albumButtonClicked()
                                isAlbumClicked = true
                                isArtistClicked = false
                        }
                }
        }

        private fun albumButtonClicked() {
                binding.ibArtist.setBackgroundResource(R.drawable.grey_background)
                binding.ibAlbum.setBackgroundResource(R.drawable.black_background)
                binding.ibArtist.setColorFilter(resources.getColor(R.color.black))
                binding.ibAlbum.setColorFilter(resources.getColor(R.color.text_color_2))

                albumFragment?.let {
                        fm!!.beginTransaction().replace(
                                R.id.fragmentContainerView,
                                it
                        ).commit()
                }
        }

        private fun artistButtonClicked() {
                binding.ibArtist.setBackgroundResource(R.drawable.black_background)
                binding.ibAlbum.setBackgroundResource(R.drawable.grey_background)
                binding.ibArtist.setColorFilter(resources.getColor(R.color.text_color_2))
                binding.ibAlbum.setColorFilter(resources.getColor(R.color.black))
                artistFragment?.let {
                        fm!!.beginTransaction().replace(
                                R.id.fragmentContainerView,
                                it
                        ).commit()
                }
        }

}