package com.example.rhythmic.ui.main_activity.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.rhythmic.R
import com.example.rhythmic.databinding.FragmentHomeBinding
import com.example.rhythmic.domain.util.UIFunctions
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

        @Inject
        lateinit var uiFunctions: UIFunctions
        private var _binding: FragmentHomeBinding? = null
        private lateinit var homeViewModel: HomeViewModel
        private val binding get() = _binding!!

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
                uiFunctions.setActionBarLogo(activity = activity as AppCompatActivity)
                homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
                _binding = FragmentHomeBinding.inflate(inflater, container, false)
                val root: View = binding.root



                return root
        }


        override fun onDestroyView() {
                super.onDestroyView()
                _binding = null
        }
}