package com.udacity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.udacity.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val filenameResId = intent.getIntExtra(FILENAME_RES_ID, 0)
        val isDownloadSuccessful = intent.getBooleanExtra(DOWNLOAD_IS_SUCCESSFUL, false)

        val binding: ActivityDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        setSupportActionBar(binding.toolbar)
    }

    companion object {
        const val FILENAME_RES_ID = "FILENAME_RES_ID"
        const val DOWNLOAD_IS_SUCCESSFUL = "DOWNLOAD_IS_SUCCESSFUL"
    }
}
