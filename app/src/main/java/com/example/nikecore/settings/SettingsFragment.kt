package com.example.nikecore.settings

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.nikecore.R
import com.example.nikecore.others.Constants.KEY_NAME
import com.example.nikecore.others.Constants.KEY_WEIGHT
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.settings_fragment.*
import www.sanju.motiontoast.MotionToast
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences
    private lateinit var viewModel: SettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.settings_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadFieldsFromSharedPref()
        saveChangesBtn.setOnClickListener {
            val success = applyChangesToSharedPref()
            if (success) {
                MotionToast.createColorToast(requireActivity(),
                    getString(R.string.info),
                    getString(R.string.saved_changes),
                    MotionToast.TOAST_ERROR,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.SHORT_DURATION,
                    ResourcesCompat.getFont(requireContext(),R.font.helvetica_regular))
                findNavController().navigate(R.id.action_settingsFragment_to_navigation_run)
            } else {
                MotionToast.createColorToast(requireActivity(),
                    getString(R.string.info),
                    getString(R.string.please_enter_all),
                    MotionToast.TOAST_ERROR,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.SHORT_DURATION,
                    ResourcesCompat.getFont(requireContext(),R.font.helvetica_regular))
            }
        }
    }

    private fun loadFieldsFromSharedPref() {
        val name = sharedPreferences.getString(KEY_NAME, "")
        val weight = sharedPreferences.getFloat(KEY_WEIGHT, 80f)
        editTextPersonNameSettings.setText(name)
        editTextWeightSettings.setText(weight.toString())
    }

    private fun applyChangesToSharedPref(): Boolean {
        val nameText = editTextPersonNameSettings.text.toString()
        val weightText = editTextWeightSettings.text.toString()
        val heightText = editTextHeightSettings.text.toString()

        if (nameText.isEmpty() || weightText.isEmpty() || heightText.isEmpty()) {
            return false
        }
        sharedPreferences.edit()
            .putString(KEY_NAME, nameText)
            .putFloat(KEY_WEIGHT, weightText.toFloat())
            .apply()
        return true
    }

}