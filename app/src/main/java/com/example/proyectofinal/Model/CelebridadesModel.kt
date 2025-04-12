package com.example.proyectofinal.Model

import com.google.gson.annotations.SerializedName

data class Persona(
    val id: Int,
    val nombre: String,
    val genero: Int,
    val ocupacion: String,
    val rutaPerfil: String?,
    val personaje: String? = null,
    val trabajo: String? = null,
    val biografia: String? = null,
    val fechaNacimiento: String? = null,
    val fechaMuerte: String? = null,
    val lugarNacimiento: String? = null,
    val popularidad: Double = 0.0
)

data class RespuestaPersonas(
    @SerializedName("results") val resultados: List<PersonaDto>,
    @SerializedName("page") val pagina: Int,
    @SerializedName("total_pages") val totalPaginas: Int,
    @SerializedName("total_results") val totalResultados: Int
)

data class PersonaDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val nombre: String,
    @SerializedName("gender") val genero: Int,
    @SerializedName("known_for_department") val ocupacion: String,
    @SerializedName("profile_path") val rutaPerfil: String?,
    @SerializedName("character") val personaje: String? = null,
    @SerializedName("job") val trabajo: String? = null,
    @SerializedName("biography") val biografia: String? = null,
    @SerializedName("birthday") val fechaNacimiento: String? = null,
    @SerializedName("deathday") val fechaMuerte: String? = null,
    @SerializedName("place_of_birth") val lugarNacimiento: String? = null,
    @SerializedName("popularity") val popularidad: Double = 0.0
)

fun PersonaDto.aPersona() = Persona(
    id = id,
    nombre = nombre,
    genero = genero,
    ocupacion = ocupacion,
    rutaPerfil = rutaPerfil?.let { "https://image.tmdb.org/t/p/w500$it" },
    personaje = personaje,
    trabajo = trabajo,
    biografia = biografia,
    fechaNacimiento = fechaNacimiento,
    fechaMuerte = fechaMuerte,
    lugarNacimiento = lugarNacimiento,
    popularidad = popularidad
)

data class ImagenesPersonaDto(
    @SerializedName("id") val id: Int,
    @SerializedName("profiles") val perfiles: List<ImagenPerfilDto>
)

data class ImagenPerfilDto(
    @SerializedName("file_path") val rutaArchivo: String,
    @SerializedName("width") val ancho: Int,
    @SerializedName("height") val alto: Int
)