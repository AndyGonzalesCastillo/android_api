package com.example.proyectofinal.State

import com.example.proyectofinal.Model.Pelicula
import com.example.proyectofinal.Model.Persona
import com.example.proyectofinal.Model.SerieTv

data class PersonasState(
    val personasPopulares: List<Persona> = emptyList(),
    val detallePersona: Persona? = null,
    val peliculasCelebridad: List<Pelicula> = emptyList(),
    val seriesCelebridad: List<SerieTv> = emptyList(),
    val imagenesPersona: List<String> = emptyList(),
    val estaCargando: Boolean = false
)
