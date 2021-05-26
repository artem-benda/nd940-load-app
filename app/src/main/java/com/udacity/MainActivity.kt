package com.udacity

import android.app.DownloadManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.databinding.DataBindingUtil
import com.udacity.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(binding.toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        binding.contentMain.customButton.setOnClickListener {
            val selectedRepository = getSelectedGithubRepository(binding)

            if (selectedRepository != null) {
                download(selectedRepository.url)
            } else {
                Toast.makeText(this, R.string.select_download_option, Toast.LENGTH_LONG).show()
            }
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
        }
    }

    private fun download(url: String) {
        val request =
            DownloadManager.Request(Uri.parse(url))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request) // enqueue puts the download request in the queue.
    }

    private fun getSelectedGithubRepository(binding: ActivityMainBinding): GithubRepository? {
        var selectedRepository: GithubRepository? = null
        when {
            binding.contentMain.optionGlide.isChecked -> {
                selectedRepository = GithubRepository.GLIDE
            }
            binding.contentMain.optionLoadApp.isChecked -> {
                selectedRepository = GithubRepository.LOAD_APP
            }
            binding.contentMain.optionRetrofit.isChecked -> {
                selectedRepository = GithubRepository.RETROFIT
            }
        }

        return selectedRepository
    }

    private enum class GithubRepository(val url: String) {
        GLIDE("https://github.com/bumptech/glide/archive/refs/heads/master.zip"),
        LOAD_APP("https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"),
        RETROFIT("https://github.com/square/retrofit/archive/refs/heads/master.zip");
    }

    companion object {
        private const val CHANNEL_ID = "channelId"
    }
}
