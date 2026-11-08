package com.practicum.playlistmaker.data.dto

data class TrackDto(
    val trackId: String?, //
    val trackName: String?, // Название композиции
    val artistName: String?, // Имя исполнителя
    val trackTimeMillis: Long?, // Продолжительность трека в миллисекундах
    val collectionName: String?, // Название альбома
    val releaseDate: String?, // Год релиза трека
    val primaryGenreName: String?, // Жанр трека
    val country: String?, // Страна исполнителя
    val artworkUrl100: String?, // Ссылка на изображение обложки
    val previewUrl: String?
)