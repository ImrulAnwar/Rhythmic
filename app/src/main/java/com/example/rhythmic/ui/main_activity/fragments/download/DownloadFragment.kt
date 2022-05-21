package com.example.rhythmic.ui.main_activity.fragments.download

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
                (activity as AppCompatActivity).supportActionBar?.apply {
                        setHomeButtonEnabled(true)
                        setDisplayHomeAsUpEnabled(true)
                        setHomeAsUpIndicator(R.drawable.ic_nav_icon)
                }
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

}