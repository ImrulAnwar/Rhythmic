package com.example.rhythmic.ui.activities.main_activity.fragments.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.rhythmic.databinding.FragmentLibraryBinding
import com.example.rhythmic.domain.util.UIFunctions
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LibraryFragment : Fragment() {
        @Inject
        lateinit var uiFunctions: UIFunctions
        private var _binding: FragmentLibraryBinding? = null
        private lateinit var libraryViewModel: LibraryViewModel
        private val binding get() = _binding!!

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
                uiFunctions.setActionBarLogo(activity = activity as AppCompatActivity)
                libraryViewModel = ViewModelProvider(this).get(LibraryViewModel::class.java)
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