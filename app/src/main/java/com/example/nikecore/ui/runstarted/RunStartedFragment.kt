package com.example.nikecore.ui.runstarted

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import com.example.nikecore.R
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.nikecore.others.Constants
import com.example.nikecore.ui.MainActivity
import kotlinx.android.synthetic.main.run_started_fragment.*


class RunStartedFragment : Fragment() {

    companion object {
        fun newInstance() = RunStartedFragment()
    }

    private lateinit var viewModel: RunStartedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.run_started_fragment, container, false)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().popBackStack(R.id.navigation_run,false)

        }
        pauseRunBtn.setOnClickListener {
            (activity as MainActivity).sendCommandToService(Constants.ACTION_PAUSE_SERVICE)
            findNavController().navigate(R.id.action_runStartedFragment_to_runPausedFragment)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RunStartedViewModel::class.java)
        // TODO: Use the ViewModel
    }

}