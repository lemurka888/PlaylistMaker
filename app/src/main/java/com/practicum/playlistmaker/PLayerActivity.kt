package com.practicum.playlistmaker

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.data.Track
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.activity.enableEdgeToEdge

class PlayerActivity : AppCompatActivity() {

    // View — как поля класса
    private lateinit var btnBack: ImageView
    private lateinit var ivCover: ImageView
    private lateinit var tvTrackName: TextView
    private lateinit var tvArtistName: TextView
    private lateinit var tvDurationValue: TextView
    private lateinit var tvAlbumLabel: TextView
    private lateinit var tvAlbumValue: TextView
    private lateinit var tvReleaseDate: TextView
    private lateinit var tvReleaseValue: TextView
    private lateinit var tvGenreValue: TextView
    private lateinit var tvCountryValue: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_player)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(
                left = systemBars.left,
                top = systemBars.top,
                right = systemBars.right,
                bottom = systemBars.bottom
            )
            insets
        }

        // Инициализация View
        btnBack = findViewById(R.id.btnBack)
        ivCover = findViewById(R.id.ivCover)
        tvTrackName = findViewById(R.id.tvTrackName)
        tvArtistName = findViewById(R.id.tvArtistName)
        tvDurationValue = findViewById(R.id.tvDurationValue)
        tvAlbumLabel = findViewById(R.id.tvAlbumLabel)
        tvAlbumValue = findViewById(R.id.tvAlbumValue)
        tvReleaseDate = findViewById(R.id.tvReleaseDate)
        tvReleaseValue = findViewById(R.id.tvReleaseValue)
        tvGenreValue = findViewById(R.id.tvGenreValue)
        tvCountryValue = findViewById(R.id.tvCountryValue)

        val track = intent.getSerializableExtra("track") as? Track
        if (track != null) {
            bindTrack(track)
        }

        btnBack.setOnClickListener { finish() }
    }

    private fun bindTrack(track: Track) {
        tvTrackName.text = track.trackName ?: "—"
        tvArtistName.text = track.artistName ?: "—"
        tvDurationValue.text = formatDuration(track.trackTimeMillis)

        // Альбом
        if (!track.collectionName.isNullOrEmpty()) {
            tvAlbumLabel.visibility = View.VISIBLE
            tvAlbumValue.visibility = View.VISIBLE
            tvAlbumValue.text = track.collectionName
        } else {
            tvAlbumLabel.visibility = View.GONE
            tvAlbumValue.visibility = View.GONE
        }

        // Год
        val year = track.releaseDate?.take(4)
        val yearInt = year?.toIntOrNull()
        if (yearInt != null && yearInt > 0) {
            tvReleaseDate.visibility = View.VISIBLE
            tvReleaseValue.visibility = View.VISIBLE
            tvReleaseValue.text = yearInt.toString()
        } else {
            tvReleaseDate.visibility = View.GONE
            tvReleaseValue.visibility = View.GONE
        }

        // Жанр
        tvGenreValue.text = track.primaryGenreName ?: "—"

        // Страна
        tvCountryValue.text = track.country ?: "—"

        // Обложка
        val radius = resources.getDimensionPixelSize(R.dimen.cover_radius_player)
        Glide.with(this)
            .load(track.getCoverArtwork())
            .centerCrop()
            .transform(RoundedCorners(radius))
            .placeholder(R.drawable.ic_placeholder)
            .error(R.drawable.ic_placeholder)
            .into(ivCover)
    }

    private fun formatDuration(millis: Long?): String {
        if (millis == null || millis < 0) return "00:00"
        val format = SimpleDateFormat("mm:ss", Locale.getDefault())
        return format.format(millis)
    }
}