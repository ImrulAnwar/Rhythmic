package com.example.rhythmic.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.rhythmic.R
import com.example.rhythmic.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

        private var _binding: FragmentSearchBinding? = null

        // This property is only valid between onCreateView and
        // onDestroyView.
        private val binding get() = _binding!!

        override fun onCreateView(
                inflater: LayoutInflater,
                container: ViewGroup?,
                savedInstanceState: Bundle?
        ): View {
                val galleryViewModel =
                        ViewModelProvider(this).get(SearchViewModel::class.java)

                _binding = FragmentSearchBinding.inflate(inflater, container, false)
                val root: View = binding.root

                val textView: TextView = binding.textGallery
                galleryViewModel.text.observe(viewLifecycleOwner) {
                        textView.text = it
                }
                return root
        }

        override fun onDestroyView() {
                super.onDestroyView()
                _binding = null
        }

        //needed to set the action bar
        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
                super.onViewCreated(view, savedInstanceState)
                (activity as AppCompatActivity).supportActionBar?.apply {
                        setHomeButtonEnabled(true)
                        setDisplayHomeAsUpEnabled(true)
                        setHomeAsUpIndicator(R.drawable.ic_nav_icon)
                }
        }
}