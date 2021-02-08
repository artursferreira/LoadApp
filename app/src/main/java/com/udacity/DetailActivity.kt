package com.udacity

import android.app.DownloadManager
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        val downloadStatus = intent.getIntExtra(STATUS, DownloadManager.STATUS_FAILED)

        file_name.text = intent.getStringExtra(FILE_NAME)
        status.apply {
            if (downloadStatus == DownloadManager.STATUS_SUCCESSFUL) {
                text = getString(R.string.success_text)
                setTextColor(getColor(R.color.colorPrimary))
            } else {
                text = getString(R.string.fail_text)
                setTextColor(Color.RED)
            }
        }

        ok_button.setOnClickListener { finish() }
    }

    companion object {
        const val FILE_NAME = "file_name"
        const val STATUS = "status"
    }

}
