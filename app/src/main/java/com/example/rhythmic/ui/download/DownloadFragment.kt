package com.example.rhythmic.ui.download

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.rhythmic.R
import com.example.rhythmic.databinding.FragmentDownloadBinding
import com.example.rhythmic.ui.home.HomeViewModel

class DownloadFragment : Fragment() {
        private var _binding: FragmentDownloadBinding? = null
        private lateinit var downloadViewModel: DownloadViewModel

        // This property is only valid between onCreateView and
        // onDestroyView.
        private val binding get() = _binding!!

        override fun onCreateView(
                inflater: LayoutInflater, container: ViewGroup?,
                savedInstanceState: Bundle?
        ): View {
                downloadViewModel =
                        ViewModelProvider(this)[DownloadViewModel::class.java]
                // Inflate the layout for this fragment
                _binding = FragmentDownloadBinding.inflate(inflater, container, false)
                val root: View = binding.root

                val textView: TextView = binding.textDownload
                downloadViewModel.text.observe(viewLifecycleOwner) {
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