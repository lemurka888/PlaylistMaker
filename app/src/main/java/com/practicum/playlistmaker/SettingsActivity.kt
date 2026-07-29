package com.practicum.playlistmaker

import android.os.Bundle
import android.widget.ImageView
import android.content.Intent
import android.net.Uri
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val btnShare = findViewById<LinearLayout>(R.id.btnShare)
        val btnSupport = findViewById<LinearLayout>(R.id.btnSupport)
        val btnUserAgree = findViewById<LinearLayout>(R.id.btnUserAgree)

        btnBack.setOnClickListener {
            val btnBackIntent = Intent(this, MainActivity::class.java)
            startActivity(btnBackIntent)
        }

        btnShare.setOnClickListener {
            val message = getString(R.string.share_message)
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, message)
            }
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share_message)))
        }

        btnSupport.setOnClickListener {
            val supportIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_email)))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_subject))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.support_body))
            }
            startActivity(supportIntent)
        }

        btnUserAgree.setOnClickListener {
            val url = getString(R.string.user_agree_url)
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(browserIntent)
        }
    }
}