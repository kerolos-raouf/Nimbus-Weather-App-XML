package com.example.nimbusweatherapp.alertFragment

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.nimbusweatherapp.R
import com.example.nimbusweatherapp.data.model.Alert
import com.example.nimbusweatherapp.data.model.AlertType
import com.example.nimbusweatherapp.databinding.FragmentAlertBinding
import com.example.nimbusweatherapp.mainActivity.Communicator
import com.example.nimbusweatherapp.mainActivity.SharedViewModel
import com.example.nimbusweatherapp.utils.convertUnixToDay
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.Calendar

@AndroidEntryPoint
class AlertFragment : Fragment() {


    private lateinit var binding : FragmentAlertBinding


    //communicator
    private val communicator : Communicator by lazy { activity as Communicator }

    //viewModels
    private val sharedViewModel : SharedViewModel by activityViewModels()
    private val alertViewModel : AlertViewModel by viewModels()

    //adapter
    private lateinit var alertAdapter : AlertRecyclerViewAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_alert, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observers()
    }

    private fun initViews()
    {

        alertAdapter = AlertRecyclerViewAdapter(object : AlertItemListener{
            override fun onDeleteButtonClicked(alert: Alert) {
                alertViewModel.deleteAlert(alert)
            }
        })
        binding.alertRecyclerView.adapter = alertAdapter

        binding.alertMenuIcon.setOnClickListener {
            communicator.openDrawer()
        }
        binding.alertAddAlarmButton.setOnClickListener {

            showDateAndTimePicker()
        }
    }

    private fun observers()
    {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED)
            {
                alertViewModel.alerts.collect{
                    if(it.isEmpty())
                    {
                        binding.alertNoAlertsLayout.visibility = View.VISIBLE
                    }
                    else
                    {
                        binding.alertNoAlertsLayout.visibility = View.GONE
                    }
                    alertAdapter.submitList(it)
                }
            }
        }
    }


    private fun showDateAndTimePicker()
    {
        val calendar = Calendar.getInstance()

        DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, day)

                TimePickerDialog(
                    requireContext(),
                    { _, hour, minute ->
                        calendar.set(Calendar.HOUR_OF_DAY, hour)
                        calendar.set(Calendar.MINUTE, minute)


                        showAlertTypeDialog(calendar.timeInMillis)
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
                ).show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun setAlertTime(alert: Alert)
    {
        alertViewModel.addAlert(alert)
    }


    private fun showAlertTypeDialog(time : Long) {
        val options = arrayOf("Notification", "Alarm Sound")


        var selectedOption = options[0]


        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Choose Alert Type")

        builder.setSingleChoiceItems(options, 0) { _, which ->
            selectedOption = options[which]
        }


        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()

            when (selectedOption) {
                "Notification" -> {
                   setAlertTime(Alert(time,AlertType.Notification))
                }
                "Alarm Sound" -> {
                    setAlertTime(Alert(time,AlertType.Alarm))
                }
            }
        }


        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        builder.create().show()
    }



}