package com.example.nikecore.ui.nfc

import android.app.PendingIntent
import android.content.Intent
import android.nfc.NfcAdapter
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.nikecore.R
import timber.log.Timber

class NfcFragment : Fragment() {

    companion object {
        fun newInstance() = NfcFragment()
    }

    private lateinit var viewModel: NfcViewModel
    private lateinit var nfcAdapter: NfcAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        nfcAdapter = NfcAdapter.getDefaultAdapter(requireContext())
        return inflater.inflate(R.layout.nfc_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NfcViewModel::class.java)
        // TODO: Use the ViewModel
    }
    override fun onResume() {
        super.onResume()
        val pendingIntent = PendingIntent.getActivity(
            requireContext(),
            0,
            Intent(requireContext(), javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
            0
        )
        nfcAdapter.enableForegroundDispatch(requireActivity(), pendingIntent, null, null)
    }
    override fun onPause() {
        super.onPause()
        nfcAdapter.disableForegroundDispatch(requireActivity())
        nfcAdapter.disableReaderMode(requireActivity())
    }

    override fun onStart() {
        super.onStart()
        nfcAdapter.enableReaderMode(requireActivity(),
            {
                // TODO: use NFC tag
                Timber.d("nfc tag")

                requireActivity().runOnUiThread(Runnable {
                    Toast.makeText(
                        requireContext(),
                        "nfc tag",
                        Toast.LENGTH_SHORT
                    ).show()
                })

            },
            NfcAdapter.FLAG_READER_NFC_A or NfcAdapter.FLAG_READER_NFC_B or NfcAdapter.FLAG_READER_NFC_F or NfcAdapter.FLAG_READER_NFC_V or NfcAdapter.FLAG_READER_NFC_BARCODE,
            null
        )
    }

    override fun onStop() {
        super.onStop()
        Timber.d("nfc stop")
        nfcAdapter.disableReaderMode(requireActivity())
    }

}