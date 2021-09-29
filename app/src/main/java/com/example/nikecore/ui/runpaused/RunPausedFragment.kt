package com.example.nikecore.ui.runpaused

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.nikecore.R
import com.example.nikecore.others.Constants.ACTION_START_OR_RESUME_SERVICE
import com.example.nikecore.others.Constants.ACTION_STOP_SERVICE
import com.example.nikecore.others.Constants.MAP_ZOOM
import com.example.nikecore.others.Constants.POLYLINE_COLOR
import com.example.nikecore.others.Constants.POLYLINE_WIDTH
import com.example.nikecore.others.TrackingUtilities
import com.example.nikecore.services.Polyline
import com.example.nikecore.services.TrackingServices
import com.example.nikecore.ui.MainActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.PolylineOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.run_paused_fragment.*
import kotlinx.android.synthetic.main.run_started_fragment.*
import timber.log.Timber

@AndroidEntryPoint
class RunPausedFragment : Fragment() {

    private var isTracking = false
    private var pathPoints = mutableListOf<Polyline>()
    private var curTimeInMillis = 0L

    private val viewModel: RunPausedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.run_paused_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapViewRunPaused.onCreate(savedInstanceState)

        mapViewRunPaused.getMapAsync {
            Timber.d("mapView $it")
            subscribeToObservers(it)
        }
        resumeRunBtn.setOnClickListener {
            toggleRun()
            findNavController().navigate(R.id.action_runPausedFragment_to_runStartedFragment)

        }
        stopRunBtn.setOnLongClickListener {
            toggleRun()
            findNavController().navigate(R.id.action_runPausedFragment_to_navigation_run)
            true
        }

    }

    private fun subscribeToObservers(map: GoogleMap) {
        TrackingServices.isTracking.observe(viewLifecycleOwner, {
            updateTracking(it)
            Timber.d("observe")
        })

        TrackingServices.pathPoints.observe(viewLifecycleOwner, {
            pathPoints = it
            Timber.d("observe pathpoint $pathPoints")
//            addLatestPolyline(map)
            addAllPolylines(map)
            moveCameraToUser(map)
        })

        TrackingServices.timeRunInMillis.observe(viewLifecycleOwner, Observer {
            curTimeInMillis = it
            val formattedTime = TrackingUtilities.getFormattedStopWatchTime(curTimeInMillis, true)
            distanceValuePausedTxt.text = formattedTime
        })
    }

    private fun toggleRun() {
        if(isTracking) {
            (activity as MainActivity).sendCommandToService(ACTION_STOP_SERVICE)
        } else {
            (activity as MainActivity).sendCommandToService(ACTION_START_OR_RESUME_SERVICE)
        }
    }

    private fun updateTracking(isTracking: Boolean) {
        this.isTracking = isTracking
    }



    private fun moveCameraToUser(map: GoogleMap) {
        Timber.d("move camera $pathPoints")
        if(pathPoints.isNotEmpty() && pathPoints.last().isNotEmpty()) {
            map.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    pathPoints.last().last(),
                    MAP_ZOOM

                )
            )
        }
    }

    private fun addAllPolylines(map: GoogleMap) {
        Timber.d("add allpolyline $pathPoints $map")
        for(polyline in pathPoints) {
            val polylineOptions = PolylineOptions()
                .color(POLYLINE_COLOR)
                .width(POLYLINE_WIDTH)
                .addAll(polyline)
            map.addPolyline(polylineOptions)
        }
    }

    private fun addLatestPolyline(map: GoogleMap) {
        Timber.d("add polyline $pathPoints")
        if(pathPoints.isNotEmpty() && pathPoints.last().size > 1) {
            val preLastLatLng = pathPoints.last()[pathPoints.last().size - 2]
            val lastLatLng = pathPoints.last().last()
            val polylineOptions = PolylineOptions()
                .color(POLYLINE_COLOR)
                .width(POLYLINE_WIDTH)
                .add(preLastLatLng)
                .add(lastLatLng)
            map.addPolyline(polylineOptions)
        }
    }

    override fun onResume() {
        super.onResume()
        mapViewRunPaused?.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapViewRunPaused?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapViewRunPaused?.onStop()
    }

    override fun onPause() {
        super.onPause()
        mapViewRunPaused?.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapViewRunPaused?.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapViewRunPaused?.onSaveInstanceState(outState)
    }

}