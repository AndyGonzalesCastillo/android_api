package com.example.proyectofinal.Conexion

import com.example.proyectofinal.Model.ImagenesPelicula
import com.example.proyectofinal.Model.ImagenesPersonaDto
import com.example.proyectofinal.Model.PeliculaDto
import com.example.proyectofinal.Model.PersonaDto
import com.example.proyectofinal.Model.RespuestaCreditosPeliculas
import com.example.proyectofinal.Model.RespuestaCreditosSerie
import com.example.proyectofinal.Model.RespuestaPeliculas
import com.example.proyectofinal.Model.RespuestaPersonas
import com.example.proyectofinal.Model.RespuestaSeriesTv
import com.example.proyectofinal.Model.RespuestaVideos
import com.example.proyectofinal.Model.SerieTvDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApi {
    @GET("trending/movie/day")
    suspend fun obtenerPeliculasTendencia(
        @Query("api_key") claveApi: String,
        @Query("language") idioma: String = "es-MX",
        @Query("page") pagina: Int = 1
    ): RespuestaPeliculas

    @GET("movie/top_rated")
    suspend fun obtenerPeliculasMejorValoradas(
        @Query("api_key") claveApi: String,
        @Query("language") idioma: String = "es-MX",
        @Query("page") pagina: Int = 1
    ): RespuestaPeliculas

    @GET("movie/upcoming")
    suspend fun obtenerPeliculasProximas(
        @Query("api_key") claveApi: String,
        @Query("language") idioma: String = "es-MX",
        @Query("page") pagina: Int = 1
    ): RespuestaPeliculas

    @GET("movie/popular")
    suspend fun obtenerPeliculasPopulares(
        @Query("api_key") claveApi: String,
        @Query("language") idioma: String = "es-MX",
        @Query("page") pagina: Int = 1
    ): RespuestaPeliculas

    @GET("movie/{movie_id}")
    suspend fun obtenerDetallesPelicula(
        @Path("movie_id") movieId: Int,
        @Query("api_key") claveApi: String,
        @Query("language") idioma: String = "es-MX"
    ): PeliculaDto

    @GET("movie/{movie_id}/recommendations")
    suspend fun obtenerPeliculasRecomendadas(
        @Path("movie_id") movieId: Int,
        @Query("api_key") claveApi: String,
        @Query("language") idioma: String = "es-MX",
        @Query("page") pagina: Int = 1
    ): RespuestaPeliculas

    @GET("movie/{movie_id}/similar")
    suspend fun obtenerPeliculasSimilares(
        @Path("movie_id") movieId: Int,
        @Query("api_key") claveApi: String,
        @Query("language") idioma: String = "es-MX",
        @Query("page") pagina: Int = 1
    ): RespuestaPeliculas


    @GET("search/movie")
    suspend fun buscarPeliculas(
        @Query("api_key") claveApi: String,
        @Query("query") consulta: String,
        @Query("language") idioma: String = "es-MX",
        @Query("page") pagina: Int = 1,
        @Query("include_adult") incluirAdultos: Boolean = false
    ): RespuestaPeliculas

    @GET("movie/{movie_id}/images")
    suspend fun obtenerImagenesPelicula(
        @Path("movie_id") movieId: Int,
        @Query("api_key") claveApi: String
    ): ImagenesPelicula

    @GET("movie/{movie_id}/videos")
    suspend fun obtenerVideosPelicula(
        @Path("movie_id") movieId: Int,
        @Query("api_key") claveApi: String,
        @Query("language") idioma: String = "es-MX"
    ): RespuestaVideos

    @GET("tv/airing_today")
    suspend fun obtenerSeriesAlAire(
        @Query("api_key") claveApi: String,
        @Query("language") idioma: String = "es-MX",
        @Query("page") pagina: Int = 1
    ): RespuestaSeriesTv

    @GET("trending/tv/day")
    suspend fun obtenerSeriesTendencia(
        @Query("api_key") claveApi: String,
        @Query("language") idioma: String = "es-MX",
        @Query("page") pagina: Int = 1
    ): RespuestaSeriesTv

    @GET("tv/top_rated")
    suspend fun obtenerSeriesMejorValoradas(
        @Query("api_key") claveApi: String,
        @Query("language") idioma: String = "es-MX",
        @Query("page") pagina: Int = 1
    ): RespuestaSeriesTv

    @GET("tv/popular")
    suspend fun obtenerSeriesPopulares(
        @Query("api_key") claveApi: String,
        @Query("language") idioma: String = "es-MX",
        @Query("page") pagina: Int = 1
    ): RespuestaSeriesTv

    @GET("tv/{tv_id}")
    suspend fun obtenerDetallesSerie(
        @Path("tv_id") serieId: Int,
        @Query("api_key") claveApi: String,
        @Query("language") idioma: String = "es-MX"
    ): SerieTvDto

    @GET("tv/{tv_id}/recommendations")
    suspend fun obtenerSeriesRecomendadas(
        @Path("tv_id") serieId: Int,
        @Query("api_key") claveApi: String,
        @Query("language") idioma: String = "es-MX",
        @Query("page") pagina: Int = 1
    ): RespuestaSeriesTv

    @GET("tv/{tv_id}/similar")
    suspend fun obtenerSeriesSimilares(
        @Path("tv_id") serieId: Int,
        @Query("api_key") claveApi: String,
        @Query("language") idioma: String = "es-MX",
        @Query("page") pagina: Int = 1
    ): RespuestaSeriesTv

    @GET("person/popular")
    suspend fun obtenerPersonasPopulares(
        @Query("api_key") claveApi: String,
        @Query("language") idioma: String = "es-MX",
        @Query("page") pagina: Int = 1
    ): RespuestaPersonas

    @GET("person/{person_id}")
    suspend fun obtenerDetallePersona(
        @Path("person_id") personId: Int,
        @Query("api_key") claveApi: String,
        @Query("language") idioma: String = "es-MX"
    ): PersonaDto

    @GET("person/{person_id}/movie_credits")
    suspend fun obtenerPeliculasCelebridad(
        @Path("person_id") personId: Int,
        @Query("api_key") claveApi: String,
        @Query("language") idioma: String = "es-MX"
    ): RespuestaCreditosPeliculas

    @GET("person/{person_id}/tv_credits")
    suspend fun obtenerSeriesCelebridad(
        @Path("person_id") personId: Int,
        @Query("api_key") claveApi: String,
        @Query("language") idioma: String = "es-MX",
        @Query("page") pagina: Int = 1
    ): RespuestaCreditosSerie

    @GET("person/{person_id}/images")
    suspend fun obtenerImagenesPersona(
        @Path("person_id") personId: Int,
        @Query("api_key") claveApi: String
    ): ImagenesPersonaDto

    @GET("search/tv")
    suspend fun buscarSeriesTv(
        @Query("api_key") claveApi: String,
        @Query("query") consulta: String,
        @Query("language") idioma: String = "es-MX",
        @Query("page") pagina: Int = 1,
        @Query("include_adult") incluirAdultos: Boolean = false
    ): RespuestaSeriesTv

    @GET("search/person")
    suspend fun buscarPersonas(
        @Query("api_key") claveApi: String,
        @Query("query") consulta: String,
        @Query("language") idioma: String = "es-MX",
        @Query("page") pagina: Int = 1,
        @Query("include_adult") incluirAdultos: Boolean = false
    ): RespuestaPersonas

    @GET("tv/{series_id}/images")
    suspend fun obtenerImagenesSerie(
        @Path("series_id") serieId: Int,
        @Query("api_key") claveApi: String
    ): ImagenesPelicula

    @GET("tv/{series_id}/videos")
    suspend fun obtenerVideosSerie(
        @Path("series_id") serieId: Int,
        @Query("api_key") claveApi: String,
        @Query("language") idioma: String = "es-MX"
    ): RespuestaVideos

}