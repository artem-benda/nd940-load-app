package com.udacity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

enum class DownloadState {
    SUCCESS,
    FAILURE
}

@Parcelize
data class DownloadResultModel(
    val fileName: String,
    val downloadState: DownloadState
) : Parcelable
