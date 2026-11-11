package com.practicum.playlistmaker.presentation

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.practicum.playlistmaker.presentation.Mediateka
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.presentation.search.ui.SearchActivity
import com.practicum.playlistmaker.presentation.settings.ui.SettingsActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnSearch = findViewById<Button>(R.id.btnSearch)
        val btnMedia = findViewById<Button>(R.id.btnMedia)
        val btnSetting = findViewById<Button>(R.id.btnSetting)

        btnMedia.setOnClickListener {
            val mediaIntent = Intent(this, Mediateka::class.java)
            startActivity(mediaIntent)
        }

        btnSetting.setOnClickListener {
            val settingIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingIntent)
        }


        btnSearch.setOnClickListener {
            val searchIntent = Intent(this, SearchActivity::class.java)
            startActivity(searchIntent)
        }

        }
    }