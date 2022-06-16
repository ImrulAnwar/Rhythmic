package com.example.rhythmic.ui.activities.main_activity.fragments.bottom_nav.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
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

private const val TAG = "HomeFragment"
@AndroidEntryPoint
class HomeFragment : Fragment() {

        @Inject lateinit var uiFunctions: UIFunctions
        private var _binding: FragmentHomeBinding? = null
        private val binding get() = _binding!!
        private lateinit var homeViewModel: HomeViewModel
        private lateinit var allSongs: LiveData<List<Song>>
        private val adapter: VerticalAdapter by lazy { VerticalAdapter(requireActivity(), TAG) }
//        @Inject lateinit var artistFragment: ArtistFragment
//        @Inject lateinit var albumFragment: AlbumFragment
        private var clicked: Boolean= false
        private val rotateClockwise: Animation by lazy{ AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_clockwise)}
        private val rotateCounterClockwise: Animation by lazy{ AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_counter_clockwise)}
        private val toTop: Animation by lazy{ AnimationUtils.loadAnimation(requireContext(), R.anim.to_top)}
        private val toBottom: Animation by lazy{ AnimationUtils.loadAnimation(requireContext(), R.anim.to_bottom)}
        private val toLeft: Animation by lazy{ AnimationUtils.loadAnimation(requireContext(), R.anim.to_left)}
        private val toRight: Animation by lazy{ AnimationUtils.loadAnimation(requireContext(), R.anim.to_right)}
        private val toTopLeft: Animation by lazy{ AnimationUtils.loadAnimation(requireContext(), R.anim.to_top_left)}
        private val toBottomRight: Animation by lazy{ AnimationUtils.loadAnimation(requireContext(), R.anim.to_bottom_right)}
        private val fm: FragmentManager by  lazy { requireActivity().supportFragmentManager }

        override fun onCreateView(
                inflater: LayoutInflater,
                container: ViewGroup?,
                savedInstanceState: Bundle?
        ): View {
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
                binding.fabSort.setOnClickListener {
                        setVisibility(clicked)
                        setAnimation(clicked)
                        clicked = !clicked
                }
                binding.fabByDate.setOnClickListener {
                        Toast.makeText(requireContext(), "By Date", Toast.LENGTH_SHORT).show()
                }

                binding.fabByName.setOnClickListener {
                        Toast.makeText(requireContext(), "By Name", Toast.LENGTH_SHORT).show()
                }

                binding.fabBySize.setOnClickListener {
                        Toast.makeText(requireContext(), "By Size", Toast.LENGTH_SHORT).show()
                }
                handleAlbumAndArtistButtonClicks()

                return root
        }

        private fun setAnimation(clicked: Boolean) {
                if (!clicked) {
                        binding.fabBySize.startAnimation(toLeft)
                        binding.fabByName.startAnimation(toTop)
                        binding.fabByDate.startAnimation(toTopLeft)
                        binding.fabSort.startAnimation(rotateCounterClockwise)
                } else {
                        binding.fabBySize.startAnimation(toRight)
                        binding.fabByName.startAnimation(toBottom)
                        binding.fabByDate.startAnimation(toBottomRight)
                        binding.fabSort.startAnimation(rotateClockwise)
                }
        }

        private fun setVisibility(clicked: Boolean) {
                if (!clicked) {
                        binding.fabBySize.visibility = View.VISIBLE
                        binding.fabByName.visibility = View.VISIBLE
                        binding.fabByDate.visibility = View.VISIBLE
                } else {
                        binding.fabBySize.visibility = View.INVISIBLE
                        binding.fabByName.visibility = View.INVISIBLE
                        binding.fabByDate.visibility = View.INVISIBLE
                }
        }

        override fun onDestroyView() {
                super.onDestroyView()
                _binding = null
//                fm = null
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

                AlbumFragment().let {
                        fm.beginTransaction().replace(
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
                ArtistFragment().let {
                        fm.beginTransaction().replace(
                                R.id.fragmentContainerView,
                                it
                        ).commit()
                }
        }

}