package com.nbakh.weatherforecast.adapters

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.nbakh.weatherforecast.network.getFormattedDate
import com.nbakh.weatherforecast.network.icon_url_prefix
import com.nbakh.weatherforecast.network.icon_url_suffix

@BindingAdapter("app:setDateTime")
fun setDateTime(tv: TextView, dt: Long) {
    tv.text = getFormattedDate(dt, "EEE hh:mmaa")
}

@BindingAdapter("app:setIcon")
fun setIcon(imageView: ImageView, icon: String) {
    val iconUrl = "$icon_url_prefix$icon$icon_url_suffix"
    Glide.with(imageView.context)
        .load(iconUrl).into(imageView)
}