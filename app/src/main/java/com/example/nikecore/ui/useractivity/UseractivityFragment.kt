package com.example.nikecore.ui.useractivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.nikecore.databinding.FragmentUseractivityBinding

class UseractivityFragment : Fragment() {

    private lateinit var useractivityViewModel: UseractivityViewModel
    private var _binding: FragmentUseractivityBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
        useractivityViewModel =
            ViewModelProvider(this).get(UseractivityViewModel::class.java)

        _binding = FragmentUseractivityBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDashboard
        useractivityViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}