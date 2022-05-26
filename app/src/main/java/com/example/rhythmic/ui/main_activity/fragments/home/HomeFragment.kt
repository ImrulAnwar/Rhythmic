package com.example.rhythmic.ui.main_activity.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rhythmic.R
import com.example.rhythmic.adapters.VerticalAdapter
import com.example.rhythmic.data.entities.Song
import com.example.rhythmic.databinding.FragmentHomeBinding
import com.example.rhythmic.domain.util.UIFunctions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

        @Inject
        lateinit var uiFunctions: UIFunctions
        private var _binding: FragmentHomeBinding? = null
        private lateinit var homeViewModel: HomeViewModel
        private val binding get() = _binding!!
        private lateinit var allSongs: LiveData<List<Song>>
        private val adapter: VerticalAdapter by lazy { VerticalAdapter(requireContext()) }

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
                binding.rvHomeFragment.layoutManager = LinearLayoutManager(requireContext())

                allSongs.observe(viewLifecycleOwner) {
                        allSongs.value?.let { adapter.setData(it) }
                }

                return root
        }


        override fun onDestroyView() {
                super.onDestroyView()
                _binding = null
        }
}