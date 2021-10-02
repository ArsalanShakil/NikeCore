package com.example.nikecore.ui.run

import android.Manifest
import android.content.Context
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.nikecore.R
import com.example.nikecore.databinding.FragmentRunBinding
import com.example.nikecore.others.Constants.ACTION_START_OR_RESUME_SERVICE
import com.example.nikecore.others.Constants.REQUEST_CODE_LOCATION_PERMISSION
import com.example.nikecore.others.TrackingUtilities
import com.example.nikecore.ui.MainActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_run.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import www.sanju.motiontoast.MotionToast

@AndroidEntryPoint
class RunFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private var gpsStatus: Boolean = false
    private val runViewModel: RunViewModel by viewModels()
    private var pathPoints = mutableListOf<com.example.nikecore.services.Polyline>()

    private var _binding: FragmentRunBinding? = null
    private var map: GoogleMap? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentRunBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestPermissions()
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync {
            map = it
        }
        startRunBtn.setOnClickListener {
            locationEnabled()
            if (gpsStatus) {
                findNavController().navigate(R.id.action_navigation_run_to_countingFragment)
                (activity as MainActivity).sendCommandToService(ACTION_START_OR_RESUME_SERVICE)
            } else {
                MotionToast.darkToast(requireActivity(),
                    getString(R.string.info),
                    getString(R.string.location_services),
                    MotionToast.TOAST_ERROR,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.SHORT_DURATION,
                    ResourcesCompat.getFont(requireContext(),R.font.helvetica_regular))

            }

        }
        settingsBtn.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_run_to_settingsFragment)
        }
        statsBtn.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_run_to_statisticsFragment)
        }

    }

    private fun requestPermissions() {
        if (TrackingUtilities.hasLocationPermissions(requireContext())) {
            return
        }
        EasyPermissions.requestPermissions(
            this,
            "You need to accept location permissions to use this app.",
            REQUEST_CODE_LOCATION_PERMISSION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestPermissions()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {}

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }


    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState)
    }

    private fun locationEnabled() {
        val locationManager =
            activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        gpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }
}