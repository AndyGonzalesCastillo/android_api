package com.example.proyectofinal.Navigation

sealed class ObjVistas(val route: String) {
    object Pelicula : ObjVistas("pelicula")
    object ListaPeliculasPopulares : ObjVistas("lista_peliculas_populares")
    object ListaPeliculasTendencia : ObjVistas("lista_peliculas_tendencia")
    object ListaPeliculasMejorValoradas : ObjVistas("lista_peliculas_mejor_valoradas")
    object ListaPeliculasProximas : ObjVistas("lista_proximos_estrenos")
    object DetallesPelicula : ObjVistas("detalles_pelicula")
    object ListaPeliculasRecomendadas : ObjVistas("lista_peliculas_recomendadas")
    object ListaPeliculasSimilares : ObjVistas("lista_peliculas_similares")

    object Buscar : ObjVistas("buscar")
    object ListaPeliculasBuscadas : ObjVistas("lista_peliculas_buscadas")
    object ListaSeriesBuscadas : ObjVistas("lista_series_buscadas")
    object ListaCelebridadesBuscadas : ObjVistas("lista_celebridades_buscadas")

    object SerieTv : ObjVistas("serie_tv")
    object ListaSeriesAlAire : ObjVistas("lista_series_al_aire")
    object ListaSeriesTendencia : ObjVistas("lista_series_tendencia")
    object ListaSeriesMejorValoradas : ObjVistas("lista_series_mejor_valoradas")
    object ListaSeriesPopulares : ObjVistas("lista_series_populares")
    object DetallesSerie : ObjVistas("detalles_serie")
    object ListaSeriesRecomendadas : ObjVistas("lista_series_recomendadas")
    object ListaSeriesSimilares : ObjVistas("lista_series_similares")

    object ListaCelebridades : ObjVistas("lista_celebridades")
    object DetallesCelebridad : ObjVistas("detalles_celebridad")
    object ListaPeliculasCelebridad : ObjVistas("lista_peliculas_celebridad")
    object ListaSeriesCelebridad : ObjVistas("lista_series_celebridad")

    object ReproducirVideo : ObjVistas("reproducir_video")
    object VistaImagenCompleta : ObjVistas("vista_imagen_completa")
}