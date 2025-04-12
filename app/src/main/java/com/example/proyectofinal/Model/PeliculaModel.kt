package com.example.proyectofinal.Model

import com.google.gson.annotations.SerializedName

data class Pelicula(
    val id: Int,
    val titulo: String,
    val descripcion: String,
    val rutaPoster: String,
    val rutaBackdrop: String?,
    val puntuacion: Double,
    val fechaLanzamiento: String,
    val presupuesto: Long = 0,
    val ingresos: Long = 0,
    val duracion: Int = 0,
    val cantidadVotos: Int = 0,
)

data class RespuestaPeliculas(
    @SerializedName("results") val resultados: List<PeliculaDto>,
    @SerializedName("page") val pagina: Int,
    @SerializedName("total_pages") val totalPaginas: Int,
    @SerializedName("total_results") val totalResultados: Int
)

data class PeliculaDto(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val titulo: String,
    @SerializedName("overview") val descripcion: String,
    @SerializedName("poster_path") val rutaPoster: String,
    @SerializedName("backdrop_path") val rutaBackdrop: String?,
    @SerializedName("vote_average") val puntuacion: Double,
    @SerializedName("release_date") val fechaLanzamiento: String,
    @SerializedName("budget") val presupuesto: Long = 0,
    @SerializedName("revenue") val ingresos: Long = 0,
    @SerializedName("runtime") val duracion: Int = 0,
    @SerializedName("vote_count") val cantidadVotos: Int = 0,
)

fun PeliculaDto.aPelicula() = Pelicula(
    id = id,
    titulo = titulo,
    descripcion = descripcion,
    rutaPoster = "https://image.tmdb.org/t/p/w500$rutaPoster",
    rutaBackdrop = rutaBackdrop?.let { "https://image.tmdb.org/t/p/w1280$it" },
    puntuacion = puntuacion,
    fechaLanzamiento = fechaLanzamiento,
    presupuesto = presupuesto,
    ingresos = ingresos,
    duracion = duracion,
    cantidadVotos = cantidadVotos,
)

data class RespuestaCreditosPeliculas(
    @SerializedName("cast") val elenco: List<PeliculaDto>
)

data class ImagenesPelicula(
    @SerializedName("backdrops") val backdrops: List<ImagenDto>,
    @SerializedName("posters") val posters: List<ImagenDto>,
    @SerializedName("logos") val logos: List<ImagenDto>
)

data class ImagenDto(
    @SerializedName("file_path") val rutaArchivo: String,
    @SerializedName("width") val ancho: Int,
    @SerializedName("height") val alto: Int,
    @SerializedName("vote_average") val puntuacionPromedio: Double = 0.0,
    @SerializedName("vote_count") val cantidadVotos: Int = 0
)

data class VideoDto(
    @SerializedName("id") val id: String,
    @SerializedName("iso_639_1") val idioma: String,
    @SerializedName("iso_3166_1") val pais: String,
    @SerializedName("name") val nombre: String,
    @SerializedName("key") val clave: String,
    @SerializedName("site") val sitio: String,
    @SerializedName("size") val tamano: Int,
    @SerializedName("type") val tipo: String,
    @SerializedName("official") val oficial: Boolean,
    @SerializedName("published_at") val fechaPublicacion: String
)

data class RespuestaVideos(
    @SerializedName("id") val id: Int,
    @SerializedName("results") val videos: List<VideoDto>
)
