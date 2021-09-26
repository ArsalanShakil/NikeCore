package com.example.nikecore.ui.askinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.nikecore.R
import kotlinx.android.synthetic.main.askinfo_fragment.*

class AskinfoFragment : Fragment() {

    private lateinit var viewModel: AskinfoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.askinfo_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getStartedAskBtn.setOnClickListener {
            findNavController().navigate(R.id.action_askinfoFragment_to_navigation_run)
        }
    }

}