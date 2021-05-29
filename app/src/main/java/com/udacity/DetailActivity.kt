package com.udacity

import android.content.Intent
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
            val intent = Intent(applicationContext, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

        NotificationManagerCompat.from(this.applicationContext)
            .cancelAll()
    }

    companion object {
        const val DOWNLOAD_RESULT = "DOWNLOAD_RESULT"
    }
}
