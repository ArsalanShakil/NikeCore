package com.example.nikecore.ui.runstarted

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.nikecore.R
import com.example.nikecore.others.Constants
import com.example.nikecore.others.TrackingUtilities
import com.example.nikecore.services.Polyline
import com.example.nikecore.services.TrackingServices
import com.example.nikecore.ui.MainActivity
import kotlinx.android.synthetic.main.run_started_fragment.*
import timber.log.Timber


class RunStartedFragment : Fragment() {

    private var curTimeInMillis = 0L
    private var isTracking = true
    private var pathPoints = mutableListOf<Polyline>()



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
            // With blank your fragment BackPressed will be disabled.
        }
        TrackingServices.isTracking.postValue(true)
        pauseRunBtn.setOnClickListener {
            (activity as MainActivity).sendCommandToService(Constants.ACTION_PAUSE_SERVICE)
            findNavController().navigate(R.id.action_runStartedFragment_to_runPausedFragment)
        }
        TrackingServices.timeRunInMillis.observe(viewLifecycleOwner, Observer {
            curTimeInMillis = it
            val formattedTime = TrackingUtilities.getFormattedStopWatchTime(curTimeInMillis, true)
            timeValueTxt.text = formattedTime
        })
        TrackingServices.pathPoints.observe(viewLifecycleOwner, {
            pathPoints = it
            Timber.d("observe pathpoint $pathPoints")
            distanceValueTxt.text = distanceCovered(pathPoints)
        })
    }


/*    private fun checkTrackingAndNavigate(isTracking: Boolean) {
        if (!isTracking) {
            findNavController().navigate(R.id.action_runStartedFragment_to_runPausedFragment)
        }
    }*/

    private fun distanceCovered(pathPoints: MutableList<Polyline>) : String{
        var distanceInMeters = 0F
        for (polyline in pathPoints) {
            distanceInMeters += TrackingUtilities.calculatePolylineLength(polyline)
        }
        val distanceInKm = distanceInMeters /1000

        Timber.d("distanceInKm: $distanceInKm")
        return if (distanceInKm > 0.01F) distanceInKm.toString().substring(0,5) else getString(R.string.zero_value)
    }


}