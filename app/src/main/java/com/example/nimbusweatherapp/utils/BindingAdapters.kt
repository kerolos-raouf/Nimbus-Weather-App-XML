package com.example.nimbusweatherapp.utils

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("app:showContent")
fun showHomeContent(view: View, show: Boolean)
{
    if(show)
    {
        view.visibility = View.VISIBLE
    }
    else
    {
        view.visibility = View.GONE
    }
}

@BindingAdapter("app:showPermissionLayout")
fun showHomePermissionLayout(view: View, show: Boolean)
{
    if(!show)
    {
        view.visibility = View.VISIBLE
    }
    else
    {
        view.visibility = View.GONE
    }
}