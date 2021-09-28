package com.example.nikecore.ui.runpaused

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.nikecore.R
import kotlinx.android.synthetic.main.run_paused_fragment.*

class RunPausedFragment : Fragment() {

    companion object {
        fun newInstance() = RunPausedFragment()
    }

    private lateinit var viewModel: RunPausedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.run_paused_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        resumeRunBtn.setOnClickListener {
            findNavController().navigate(R.id.action_runPausedFragment_to_runStartedFragment)
        }
        stopRunBtn.setOnLongClickListener {
            findNavController().navigate(R.id.action_runPausedFragment_to_navigation_run)
            true
        }
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RunPausedViewModel::class.java)
        // TODO: Use the ViewModel
    }

}