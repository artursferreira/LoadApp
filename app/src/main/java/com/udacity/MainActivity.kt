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
import androidx.annotation.StringDef
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.udacity.util.sendNotification
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.io.File


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var downloadManager: DownloadManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    private lateinit var selectedUrl: REPOSITORY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        radio_group.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.glide -> {
                    selectedUrl = REPOSITORY.GLIDE
                }
                R.id.load_app -> {
                    selectedUrl = REPOSITORY.STARTER
                }
                R.id.retrofit -> {
                    selectedUrl = REPOSITORY.RETROFIT
                }
            }
        }

        custom_button.setOnClickListener {
            download()
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val downloadId = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

            if (downloadId == -1L) return

            // query download status

            // query download status
            val cursor: Cursor =
                downloadManager.query(downloadId?.let { DownloadManager.Query().setFilterById(it) })
            if (cursor.moveToFirst()) {
                val status: Int =
                    cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

                notificationManager.sendNotification(
                    getString(selectedUrl.text),
                    status,
                    applicationContext
                )

            } else {
                // download is cancelled
            }
        }
    }

    private fun download() {
        if (::selectedUrl.isInitialized.not()) {
            Toast.makeText(this, R.string.select_an_option, Toast.LENGTH_SHORT).show()
        } else {
            val request =
                DownloadManager.Request(Uri.parse(selectedUrl.url))
                    .setTitle(getString(R.string.app_name))
                    .setDescription(getString(R.string.app_description))
                    .setRequiresCharging(false)
                    .setAllowedOverMetered(true)
                    .setAllowedOverRoaming(true)

            downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            downloadID =
                downloadManager.enqueue(request)// enqueue puts the download request in the queue.
            custom_button.buttonState = ButtonState.Loading
        }
    }

    enum class REPOSITORY(val text: Int, val url: String) {
        GLIDE(R.string.glide_text, GLIDE_URL),
        RETROFIT(R.string.retrofit_text, RETROFIT_URL),
        STARTER(R.string.load_app_text, STARTER_URL)
    }
    companion object {
        private const val GLIDE_URL =
            "https://github.com/bumptech/glide/archive/master.zip"
        private const val RETROFIT_URL =
            "https://github.com/square/retrofit/archive/master.zip"
        private const val STARTER_URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
    }

}
