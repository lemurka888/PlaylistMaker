package com.practicum.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.data.Track
import java.text.SimpleDateFormat
import java.util.Locale
import android.util.Log

class TrackAdapter(
    private var tracks: List<Track>,
    private val onItemClick: (Track) -> Unit
) : RecyclerView.Adapter<TrackAdapter.TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent,false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = tracks[position]
        holder.bind(track)

        holder.itemView.setOnClickListener {
            onItemClick(track)
        }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    fun updateTracks(newTracks: List<Track>) {
        tracks = newTracks
        notifyDataSetChanged()
    }

    class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // связываем элементы из разметки
        private val tvTrackName: TextView = itemView.findViewById(R.id.tvTrackName)
        private val tvArtistName: TextView = itemView.findViewById(R.id.tvArtistName)
        private val tvTrackTime: TextView = itemView.findViewById(R.id.tvTrackTime)
        private val ivTrackCover: ImageView = itemView.findViewById(R.id.ivTrackCover)


        fun bind(track: Track) {
            tvTrackName.text = track.trackName
            tvArtistName.text = track.artistName
            val radius = itemView.resources.getDimensionPixelSize(R.dimen.cover_radius)

            val dateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
            val formattedTime = dateFormat.format(track.trackTimeMillis)

            tvTrackTime.text = "• $formattedTime"

            Glide.with(itemView)
                .load(track.artworkUrl100)
                .centerCrop()
                .transform(RoundedCorners(radius))
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_placeholder)
                .into(ivTrackCover)
        }
    }
}