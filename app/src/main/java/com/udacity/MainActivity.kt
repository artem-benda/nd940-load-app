package com.udacity

import android.app.DownloadManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.databinding.DataBindingUtil
import com.udacity.databinding.ActivityMainBinding
import java.lang.IllegalStateException

class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0
    private var startedDownloadOption: GithubRepository? = null

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(binding.toolbar)

        NotificationManagerCompat.from(applicationContext)
            .createChannel(applicationContext)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        binding.contentMain.customButton.setOnClickListener {
            val selectedRepository = getSelectedGithubRepository(binding)

            if (selectedRepository != null) {
                download(selectedRepository.url)
                startedDownloadOption = selectedRepository
            } else {
                Toast.makeText(this, R.string.select_download_option, Toast.LENGTH_LONG).show()
            }
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (downloadID == id) {
                val downloadResultModel = getDownloadResult(downloadID)
                val textResource = if (downloadResultModel.downloadState == DownloadState.SUCCESS)
                    R.string.download_successful
                else
                    R.string.download_failed
                NotificationManagerCompat.from(applicationContext)
                    .sendDownloadResult(
                        applicationContext,
                        resources.getString(R.string.downloaded),
                        resources.getString(textResource),
                        downloadResultModel
                    )
            }
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

    private enum class GithubRepository(val filenameResId: Int, val url: String) {
        GLIDE(R.string.glide_option, "https://github.com/bumptech/glide/archive/refs/heads/master.zip"),
        LOAD_APP(R.string.load_app_option, "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"),
        RETROFIT(R.string.retrofit_option, "https://github.com/square/retrofit/archive/refs/heads/master.zip");
    }

    private fun getDownloadResult(downloadId: Long): DownloadResultModel {
        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        val c: Cursor = downloadManager
            .query(DownloadManager.Query().setFilterById(downloadId))
        if (c.moveToFirst()) {
            val status: Int = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS))
            val isSuccessful = status == DownloadManager.STATUS_SUCCESSFUL
            val url = c.getString(c.getColumnIndex(DownloadManager.COLUMN_URI))
            val fileNameResId = GithubRepository.values().first { it.url == url }.filenameResId
            val fileName = resources.getString(fileNameResId)
            val downloadState = if (isSuccessful) DownloadState.SUCCESS else DownloadState.FAILURE
            return DownloadResultModel(fileName, downloadState)
        }
        throw IllegalStateException()
    }
}
