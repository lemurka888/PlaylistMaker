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

class TrackAdapter(
    private val tracks: List<Track>
) : RecyclerView.Adapter<TrackAdapter.TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent,false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = tracks[position]
        holder.bind(track)
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    inner class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // связываем элементы из разметки
        private val tvTrackName: TextView = itemView.findViewById(R.id.tvTrackName)
        private val tvArtistTime: TextView = itemView.findViewById(R.id.tvArtist_Time)
        private val ivTrackCover: ImageView = itemView.findViewById(R.id.ivTrackCover)

        fun bind(track: Track) {
            tvTrackName.text = track.trackName
            tvArtistTime.text = "${track.artistName} • ${track.trackTime}"

            Glide.with(itemView)
                .load(track.artworkUrl100)
                .centerCrop()
                .transform(RoundedCorners(2))
                .placeholder(R.drawable.ic_placeholder)
                .into(ivTrackCover)
        }
    }
}