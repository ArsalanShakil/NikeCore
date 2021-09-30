package com.example.nikecore.statistics

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.nikecore.R
import com.example.nikecore.others.TrackingUtilities
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.statistics_fragment.*
import java.lang.Math.round

@AndroidEntryPoint
class StatisticsFragment : Fragment() {

    private val statisticsViewModel: StatisticsViewModel by viewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.statistics_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToObservers()
    }

    private fun subscribeToObservers() {
        statisticsViewModel.totalTimeRun.observe(viewLifecycleOwner, Observer {
            it?.let {
                val totalTimeRun = TrackingUtilities.getFormattedStopWatchTime(it)
                timeValueStatsTxt.text = totalTimeRun
            }
        })
        statisticsViewModel.totalDistance.observe(viewLifecycleOwner, Observer {
            it?.let {
                val km = it / 1000f
                val totalDistance = round(km * 10f) / 10f
                val totalDistanceString = "${totalDistance}"
                distanceValueStatsTxt.text = totalDistanceString
            }
        })
        statisticsViewModel.totalAvgSpeed.observe(viewLifecycleOwner, Observer {
            it?.let {
                val avgSpeed = round(it * 10f) / 10f
                val avgSpeedString = "${avgSpeed}"
                avgSpeedValueStatsTxt.text = avgSpeedString
            }
        })
        statisticsViewModel.totalCaloriesBurned.observe(viewLifecycleOwner, Observer {
            it?.let {
                val totalCalories = "${it}"
                caloriesValueStatsTxt.text = totalCalories
            }
        })
    }

}