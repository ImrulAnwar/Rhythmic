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

class HomeFragment : Fragment() {

        private var _binding: FragmentHomeBinding? = null
        private lateinit var homeViewModel: HomeViewModel

        // This property is only valid between onCreateView and
        // onDestroyView.
        private val binding get() = _binding!!

        override fun onCreateView(
                inflater: LayoutInflater,
                container: ViewGroup?,
                savedInstanceState: Bundle?
        ): View {
                UIFunctions().setActionBarLogo(activity = activity as AppCompatActivity)
                homeViewModel =
                        ViewModelProvider(this)[HomeViewModel::class.java]

                _binding = FragmentHomeBinding.inflate(inflater, container, false)
                val root: View = binding.root

                val textView: TextView = binding.textHome
                homeViewModel.text.observe(viewLifecycleOwner) {
                        textView.text = it
                }
                return root
        }


        override fun onDestroyView() {
                super.onDestroyView()
                _binding = null
        }
}