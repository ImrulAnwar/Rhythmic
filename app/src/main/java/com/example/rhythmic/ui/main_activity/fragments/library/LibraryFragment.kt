package com.example.rhythmic.ui.main_activity.fragments.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.rhythmic.R
import com.example.rhythmic.databinding.FragmentLibraryBinding

class LibraryFragment : Fragment() {

        private var _binding: FragmentLibraryBinding? = null
        private lateinit var libraryViewModel: LibraryViewModel

        // This property is only valid between onCreateView and
        // onDestroyView.
        private val binding get() = _binding!!

        override fun onCreateView(
                inflater: LayoutInflater,
                container: ViewGroup?,
                savedInstanceState: Bundle?
        ): View {
                (activity as AppCompatActivity).supportActionBar?.apply {
                        setHomeButtonEnabled(true)
                        setDisplayHomeAsUpEnabled(true)
                        setHomeAsUpIndicator(R.drawable.ic_nav_icon)
                }
                libraryViewModel =
                        ViewModelProvider(this).get(LibraryViewModel::class.java)

                _binding = FragmentLibraryBinding.inflate(inflater, container, false)
                val root: View = binding.root

                val textView: TextView = binding.textSlideshow
                libraryViewModel.text.observe(viewLifecycleOwner) {
                        textView.text = it
                }
                return root
        }

        override fun onDestroyView() {
                super.onDestroyView()
                _binding = null
        }
}