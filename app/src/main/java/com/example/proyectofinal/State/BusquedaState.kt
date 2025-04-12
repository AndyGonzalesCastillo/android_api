package com.example.proyectofinal.State

import com.example.proyectofinal.Model.Pelicula
import com.example.proyectofinal.Model.Persona
import com.example.proyectofinal.Model.SerieTv

data class BusquedaState(
    val peliculasBuscadas: List<Pelicula> = emptyList(),
    val serieTvBuscadas: List<SerieTv> = emptyList(),
    val personasBuscadas: List<Persona> = emptyList(),
    val estaCargando: Boolean = false,
    val ultimaBusqueda: String = "",
    val mostrarAmbos: Boolean = false
)