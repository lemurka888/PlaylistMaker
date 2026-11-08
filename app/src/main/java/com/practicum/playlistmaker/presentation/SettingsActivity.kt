package com.practicum.playlistmaker.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.google.android.material.switchmaterial.SwitchMaterial
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.SettingsInteractor
import com.practicum.playlistmaker.presentation.creator.Creator

class SettingsActivity : AppCompatActivity() {
    private lateinit var settingsInteractor: SettingsInteractor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)
        settingsInteractor = Creator.provideSettingsInteractor(this)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val btnShare = findViewById<LinearLayout>(R.id.btnShare)
        val btnSupport = findViewById<LinearLayout>(R.id.btnSupport)
        val btnUserAgree = findViewById<LinearLayout>(R.id.btnUserAgree)
        val switchDarkTheme = findViewById<SwitchMaterial>(R.id.switchDarkTheme)

        btnBack.setOnClickListener {
            val btnBackIntent = Intent(this, MainActivity::class.java)
            startActivity(btnBackIntent)
            finish()
        }

        switchDarkTheme.isChecked = settingsInteractor.isDarkTheme()
        switchDarkTheme.setOnCheckedChangeListener { _, checked ->
            settingsInteractor.setDarkTheme(checked)
            (applicationContext as App).switchTheme(checked)
        }

        btnShare.setOnClickListener {
            val message = getString(R.string.share_message)
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, message)
            }
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share_message)))
        }

        btnSupport.setOnTouchListener { _, _ ->
            val supportIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_email)))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_subject))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.support_body))
            }
            startActivity(supportIntent)
            true
        }

        btnUserAgree.setOnClickListener {
            val url = getString(R.string.user_agree_url)
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(browserIntent)
        }
    }
}