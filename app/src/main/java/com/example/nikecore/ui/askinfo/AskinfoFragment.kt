package com.example.nikecore.ui.askinfo

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.nikecore.MainActivity
import com.example.nikecore.R
import kotlinx.android.synthetic.main.askinfo_fragment.*

class AskinfoFragment : Fragment() {

    companion object {
        fun newInstance() = AskinfoFragment()
    }

    private lateinit var viewModel: AskinfoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.askinfo_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AskinfoViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getStartedAskBtn.setOnClickListener {
            findNavController().navigate(R.id.action_askinfoFragment_to_navigation_run)
        }
    }

}