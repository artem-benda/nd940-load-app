package com.udacity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import androidx.databinding.DataBindingUtil
import com.udacity.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val model = intent.getParcelableExtra<DownloadResultModel>(DOWNLOAD_RESULT)

        val binding: ActivityDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        setSupportActionBar(binding.toolbar)

        binding.content.model = model
        binding.content.okButton.setOnClickListener {
            finish()
        }

        NotificationManagerCompat.from(this.applicationContext)
            .cancelAll()
    }

    companion object {
        const val DOWNLOAD_RESULT = "DOWNLOAD_RESULT"
    }
}
