package com.example.proyectofinal.Model

import com.google.gson.annotations.SerializedName

data class RespuestaSeriesTv(
    @SerializedName("results") val resultados: List<SerieTvDto>,
    @SerializedName("page") val pagina: Int,
    @SerializedName("total_pages") val totalPaginas: Int,
    @SerializedName("total_results") val totalResultados: Int
)

data class SerieTvDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val titulo: String,
    @SerializedName("overview") val descripcion: String,
    @SerializedName("poster_path") val rutaPoster: String,
    @SerializedName("backdrop_path") val rutaBackdrop: String?,
    @SerializedName("vote_average") val puntuacion: Double,
    @SerializedName("first_air_date") val fechaEstreno: String,
    @SerializedName("number_of_seasons") val numeroTemporadas: Int = 0,
    @SerializedName("number_of_episodes") val numeroEpisodios: Int = 0,
    @SerializedName("vote_count") val cantidadVotos: Int = 0,
)

data class SerieTv(
    val id: Int,
    val titulo: String,
    val descripcion: String,
    val rutaPoster: String,
    val rutaBackdrop: String?,
    val puntuacion: Double,
    val fechaEstreno: String,
    val numeroTemporadas: Int = 0,
    val numeroEpisodios: Int = 0,
    val cantidadVotos: Int = 0,
)

fun SerieTvDto.aSerieTv() = SerieTv(
    id = id,
    titulo = titulo,
    descripcion = descripcion,
    rutaPoster = "https://image.tmdb.org/t/p/w500$rutaPoster",
    rutaBackdrop = rutaBackdrop?.let { "https://image.tmdb.org/t/p/w1280$it" },
    puntuacion = puntuacion,
    fechaEstreno = fechaEstreno,
    numeroTemporadas = numeroTemporadas,
    numeroEpisodios = numeroEpisodios,
    cantidadVotos = cantidadVotos,
)

data class RespuestaCreditosSerie(
    @SerializedName("cast") val elenco: List<SerieTvDto>
)
