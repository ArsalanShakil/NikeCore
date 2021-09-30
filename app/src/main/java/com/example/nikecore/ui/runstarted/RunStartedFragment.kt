package com.example.nikecore.ui.runstarted

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.nikecore.R
import com.example.nikecore.others.Constants
import com.example.nikecore.others.TrackingUtilities
import com.example.nikecore.services.TrackingServices
import com.example.nikecore.ui.MainActivity
import kotlinx.android.synthetic.main.run_started_fragment.*
import timber.log.Timber


class RunStartedFragment : Fragment() {

    private var curTimeInMillis = 0L
    private lateinit var viewModel: RunStartedViewModel
    private var isTracking = true


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        TrackingServices.isTracking.postValue(true)
        return inflater.inflate(R.layout.run_started_fragment, container, false)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().popBackStack(R.id.navigation_run,false)

        }
        TrackingServices.isTracking.postValue(true)
        pauseRunBtn.setOnClickListener {
            (activity as MainActivity).sendCommandToService(Constants.ACTION_PAUSE_SERVICE)
            findNavController().navigate(R.id.action_runStartedFragment_to_runPausedFragment)
        }
        TrackingServices.timeRunInMillis.observe(viewLifecycleOwner, Observer {
            curTimeInMillis = it
            val formattedTime = TrackingUtilities.getFormattedStopWatchTime(curTimeInMillis, true)
            distanceValueTxt.text = formattedTime
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RunStartedViewModel::class.java)
        // TODO: Use the ViewModel
    }

    private fun checkTrackingAndNavigate(isTracking: Boolean) {
        if(!isTracking) {
            findNavController().navigate(R.id.action_runStartedFragment_to_runPausedFragment)
        }
    }


}