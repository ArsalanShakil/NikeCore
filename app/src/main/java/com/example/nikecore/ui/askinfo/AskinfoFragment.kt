package com.example.nikecore.ui.askinfo

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.nikecore.R
import com.example.nikecore.others.Constants.KEY_FIRST_TIME_TOGGLE
import com.example.nikecore.others.Constants.KEY_HEIGHT
import com.example.nikecore.others.Constants.KEY_NAME
import com.example.nikecore.others.Constants.KEY_WEIGHT
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.askinfo_fragment.*
import javax.inject.Inject

@AndroidEntryPoint
class AskinfoFragment : Fragment() {

    @Inject
    lateinit var sharedPref: SharedPreferences

    @set:Inject
    var isFirstAppOpen = true

    private lateinit var viewModel: AskinfoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.askinfo_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!isFirstAppOpen) {

            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.askinfoFragment, true)
                .build()
            findNavController().navigate(
                R.id.action_askinfoFragment_to_navigation_run,
                savedInstanceState,
                navOptions
            )
        }

        getStartedAskBtn.setOnClickListener {
            val success = writePersonalDataToSharedPref()
            if (success) {
                findNavController().navigate(R.id.action_askinfoFragment_to_navigation_run)
            } else {
                Snackbar.make(requireView(), "Please enter all the fields", Snackbar.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun writePersonalDataToSharedPref(): Boolean {
        val name = editTextPersonName.text.toString()
        val weight = editTextWeight.text.toString()
        val height = editTextHeight.text.toString()
        if (name.isEmpty() || weight.isEmpty() || height.isEmpty()) {
            return false
        }
        sharedPref.edit()
            .putString(KEY_NAME, name)
            .putFloat(KEY_WEIGHT, weight.toFloat())
            .putFloat(KEY_HEIGHT, height.toFloat())
            .putBoolean(KEY_FIRST_TIME_TOGGLE, false)
            .apply()

        return true
    }

}