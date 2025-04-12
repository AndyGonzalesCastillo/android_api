package com.example.proyectofinal.State

import com.example.proyectofinal.Model.ImagenesPelicula
import com.example.proyectofinal.Model.Pelicula
import com.example.proyectofinal.Model.VideoDto

data class PeliculasState(
    val peliculasPopulares: List<Pelicula> = emptyList(),
    val peliculasTendencia: List<Pelicula> = emptyList(),
    val peliculasMejorValoradas: List<Pelicula> = emptyList(),
    val peliculasProximas: List<Pelicula> = emptyList(),
    val detalle: Pelicula? = null,
    val peliculasRecomendadas: List<Pelicula> = emptyList(),
    val peliculasSimilares: List<Pelicula> = emptyList(),
    val estaCargando: Boolean = false,
    val imagenesPelicula: ImagenesPelicula? = null,
    val videosPelicula: List<VideoDto> = emptyList()

)