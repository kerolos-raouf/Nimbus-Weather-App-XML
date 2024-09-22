package com.example.nimbusweatherapp.utils.customAlertDialog

import android.app.Activity
import androidx.appcompat.app.AlertDialog
import com.example.nimbusweatherapp.R
import com.example.nimbusweatherapp.databinding.CustomAlertDialogBinding

class CustomAlertDialog(
    private val activity : Activity,

)
{

    private lateinit var alertDialog : AlertDialog

    fun showAlertDialog(message : String, actionText : String, buttonBackground : Int, iCustomAlertDialog: ICustomAlertDialog)
    {
        val builder = AlertDialog.Builder(activity)
        val binding = CustomAlertDialogBinding.bind(activity.layoutInflater.inflate(R.layout.custom_alert_dialog, null))
        builder.setView(binding.root)

        binding.alertMessage.text = message
        binding.alertActionButton.text = actionText
        val drawable = activity.getDrawable(R.drawable.allow_button_custom_theme)
        drawable?.setTint(buttonBackground)
        binding.alertActionButton.background = drawable

        binding.alertCancelButton.setOnClickListener{
            alertDialog.dismiss()
        }

        binding.alertActionButton.setOnClickListener {
            iCustomAlertDialog.onActionClicked()
            alertDialog.dismiss()
        }

        alertDialog = builder.create()
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        alertDialog.show()
    }


}