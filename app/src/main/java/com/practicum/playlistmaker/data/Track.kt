package com.practicum.playlistmaker.data

data class Track(
    val trackId: Long?, //
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val trackTimeMillis: Long?, // Продолжительность трека в миллисекундах
    val artworkUrl100: String? // Ссылка на изображение обложки
)