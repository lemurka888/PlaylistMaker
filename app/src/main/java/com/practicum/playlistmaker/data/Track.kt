package com.practicum.playlistmaker.data

import java.io.Serializable

data class Track(
    val trackId: Long?, //
    val trackName: String?, // Название композиции
    val artistName: String?, // Имя исполнителя
    val trackTimeMillis: Long?, // Продолжительность трека в миллисекундах
    val collectionName: String?, // Название альбома
    val releaseDate: String?, // Год релиза трека
    val primaryGenreName: String?, // Жанр трека
    val country: String?, // Страна исполнителя
    val artworkUrl100: String? // Ссылка на изображение обложки
) : Serializable {

    fun getCoverArtwork(): String {
        return artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg") ?: ""
    }
}