package com.example.rhythmic.ui.activities.main_activity.fragments.bottom_nav.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.rhythmic.databinding.FragmentSearchBinding
import com.example.rhythmic.domain.util.UIFunctions
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment() {
        @Inject
        lateinit var uiFunctions: UIFunctions
        private var _binding: FragmentSearchBinding? = null
        private lateinit var searchViewModel: SearchViewModel
        private val binding get() = _binding!!

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
                uiFunctions.setActionBarLogo(activity = activity as AppCompatActivity)
                searchViewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
                _binding = FragmentSearchBinding.inflate(inflater, container, false)
                val root: View = binding.root

                val textView: TextView = binding.textGallery
                searchViewModel.text.observe(viewLifecycleOwner) {
                        textView.text = it
                }

                return root
        }

        override fun onDestroyView() {
                super.onDestroyView()
                _binding = null
        }

}