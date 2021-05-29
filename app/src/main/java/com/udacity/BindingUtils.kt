package com.udacity

import android.graphics.Color
import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("setDownloadStateText")
fun TextView.setDownloadStateText(model: DownloadResultModel?) {
    model?.let {
        val textResId = if (it.downloadState == DownloadState.SUCCESS)
            R.string.state_success
        else
            R.string.state_failure
        text = resources.getString(textResId)
        val colorResId = if (it.downloadState == DownloadState.SUCCESS)
            Color.GREEN
        else
            Color.RED
        setTextColor(colorResId)
    }
}
