package com.example.rhythmic.ui.activities.main_activity.fragments.download

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.rhythmic.databinding.FragmentDownloadBinding
import com.example.rhythmic.domain.util.UIFunctions
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DownloadFragment : Fragment() {
        @Inject
        lateinit var uiFunctions: UIFunctions
        private var _binding: FragmentDownloadBinding? = null
        private lateinit var downloadViewModel: DownloadViewModel
        private val binding get() = _binding!!

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
                uiFunctions.setActionBarLogo(activity = activity as AppCompatActivity)
                downloadViewModel = ViewModelProvider(this)[DownloadViewModel::class.java]
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