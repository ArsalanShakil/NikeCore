package com.example.nikecore.ui.runstarted

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.nikecore.R
import androidx.appcompat.app.AppCompatActivity





class RunStartedFragment : Fragment() {

    companion object {
        fun newInstance() = RunStartedFragment()
    }

    private lateinit var viewModel: RunStartedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        return inflater.inflate(R.layout.run_started_fragment, container, false)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RunStartedViewModel::class.java)
        // TODO: Use the ViewModel
    }

}