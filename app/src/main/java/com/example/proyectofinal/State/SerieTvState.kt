package com.example.proyectofinal.State

import com.example.proyectofinal.Model.ImagenesPelicula
import com.example.proyectofinal.Model.Pelicula
import com.example.proyectofinal.Model.SerieTv
import com.example.proyectofinal.Model.VideoDto

data class SeriesTvState(
    val seriesAlAire: List<SerieTv> = emptyList(),
    val seriesTendencia: List<SerieTv> = emptyList(),
    val seriesMejorValoradas: List<SerieTv> = emptyList(),
    val seriesPopulares: List<SerieTv> = emptyList(),
    val detalle: SerieTv? = null,
    val seriesRecomendadas: List<SerieTv> = emptyList(),
    val seriesSimilares: List<SerieTv> = emptyList(),
    val estaCargando: Boolean = false,
    val imagenesSerie: ImagenesPelicula? = null,
    val videosSerie: List<VideoDto> = emptyList()
)
