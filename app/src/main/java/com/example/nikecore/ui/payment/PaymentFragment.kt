package com.example.nikecore.ui.payment

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.nikecore.R
import com.example.nikecore.databinding.FragmentPaymentBinding
import kotlinx.android.synthetic.main.fragment_payment.*

class PaymentFragment : Fragment() {

    private lateinit var paymentViewModel: PaymentViewModel
    private var _binding: FragmentPaymentBinding? = null
    private var userMoney = 0

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        paymentViewModel =
            ViewModelProvider(this).get(PaymentViewModel::class.java)

        _binding = FragmentPaymentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        /*val textView: TextView = binding.textNotifications
        paymentViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })*/
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val settings: SharedPreferences = requireContext().getSharedPreferences("user_Balance", 0)
        userMoney = settings.getInt("SNOW_DENSITY", 0) //0 is the default value
        balanceValueTxt.text = resources.getString(R.string.string_euro,userMoney.toString())
        ticketValueTxt.text = (userMoney/10).toString()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}