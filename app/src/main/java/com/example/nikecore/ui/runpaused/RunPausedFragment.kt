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
import com.example.nikecore.database.Run
import com.example.nikecore.others.Constants.ACTION_START_OR_RESUME_SERVICE
import com.example.nikecore.others.Constants.ACTION_STOP_SERVICE
import com.example.nikecore.others.Constants.MAP_ZOOM
import com.example.nikecore.others.Constants.POLYLINE_COLOR
import com.example.nikecore.others.Constants.POLYLINE_WIDTH
import com.example.nikecore.others.TrackingUtilities
import com.example.nikecore.services.Polyline
import com.example.nikecore.services.TrackingServices
import com.example.nikecore.ui.MainActivity
import com.example.nikecore.ui.run.RunViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.run_paused_fragment.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import kotlin.math.round

@AndroidEntryPoint
class RunPausedFragment : Fragment() {

    private var isTracking = false
    private var pathPoints = mutableListOf<Polyline>()
    private var curTimeInMillis = 0L

    private var map: GoogleMap? = null

    @set:Inject
    var weight = 80f


    private val viewModel: RunPausedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.d("ONVIEWCREATED")
        return inflater.inflate(R.layout.run_paused_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapViewRunPaused.onCreate(savedInstanceState)

        mapViewRunPaused.getMapAsync {
            Timber.d("mapView $it")
            map = it
            subscribeToObservers(it)
        }
        resumeRunBtn.setOnClickListener {
            toggleRun()
            findNavController().navigate(R.id.action_runPausedFragment_to_runStartedFragment)

        }
        stopRunBtn.setOnLongClickListener {
            toggleRun()
            showCancelTrackingDialog()
            true
        }
        finishRunBtn.setOnClickListener {
            zoomToSeeWholeTrack()
            endRunAndSaveToDb()

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
            setCurrentLocationMarker(map)
            setStartingPositionMarker(map)
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
        Timber.d("isTracking Paused: $isTracking")

    }


    private fun setStartingPositionMarker(map: GoogleMap) {
        if(pathPoints.isNotEmpty() && pathPoints.first().isNotEmpty()) {
            map.addMarker(
                MarkerOptions()
                    .position(pathPoints.first().first())
            )
        }
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
    private fun zoomToSeeWholeTrack() {
        val bounds = LatLngBounds.Builder()
        for(polyline in pathPoints) {
            for(pos in polyline) {
                bounds.include(pos)
            }
        }

        map?.moveCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds.build(),
                mapViewRunPaused.width,
                mapViewRunPaused.height,
                (mapViewRunPaused.height * 0.05f).toInt()
            )
        )
    }

    private fun endRunAndSaveToDb() {
        map?.snapshot { bmp ->
            var distanceInMeters = 0
            for(polyline in pathPoints) {
                distanceInMeters += TrackingUtilities.calculatePolylineLength(polyline).toInt()
            }
            val avgSpeed = round((distanceInMeters / 1000f) / (curTimeInMillis / 1000f / 60 / 60) * 10) / 10f
            val dateTimestamp = Calendar.getInstance().timeInMillis
            val caloriesBurned = ((distanceInMeters / 1000f) * weight).toInt()
            val run = Run(bmp, dateTimestamp, avgSpeed, distanceInMeters, curTimeInMillis, caloriesBurned)
            viewModel.insertRun(run)
            Snackbar.make(
                requireActivity().findViewById(R.id.container),
                "Run saved successfully",
                Snackbar.LENGTH_LONG
            ).show()
            stopRun()
        }
    }




    private fun setCurrentLocationMarker(map:GoogleMap) {
        if(pathPoints.isNotEmpty() && pathPoints.last().isNotEmpty()) {
            map.addMarker(
                MarkerOptions().position(pathPoints.last().last())
                    .title("run"))?.setIcon(
                (activity as MainActivity).getBitmapDescriptorFromVector(requireContext(), R.drawable.ic_current_loaction_marker_icon)

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

    private fun showCancelTrackingDialog() {
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Cancel the Run?")
            .setMessage("Are you sure to cancel the current run and delete all its data?")
            .setIcon(R.drawable.ic_menu_run)
            .setPositiveButton("Yes") { _, _ ->
                stopRun()
            }
            .setNegativeButton("No") { dialogInterface, _ ->
                dialogInterface.cancel()
            }
            .create()
        dialog.show()
    }

    private fun stopRun() {
        (activity as MainActivity).sendCommandToService(ACTION_STOP_SERVICE)
        findNavController().navigate(R.id.action_runPausedFragment_to_navigation_run)
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