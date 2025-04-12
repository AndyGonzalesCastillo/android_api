package com.example.proyectofinal.Navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.rememberNavController
import com.example.proyectofinal.Retrofit.RetrofitTmdb
import com.example.proyectofinal.View.PeliculaDetalleView
import com.example.proyectofinal.View.PeliculaListaView
import com.example.proyectofinal.ViewModel.PeliculasViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.proyectofinal.View.BuscarView
import com.example.proyectofinal.View.CelebridadesListaView
import com.example.proyectofinal.View.CelebridadesView
import com.example.proyectofinal.View.PeliculaView
import com.example.proyectofinal.View.PersonaDetalleView
import com.example.proyectofinal.View.SerieTvDetalleView
import com.example.proyectofinal.View.SerieTvListaView
import com.example.proyectofinal.View.SerieTvView
import com.example.proyectofinal.View.VistaImagenCompleta
import com.example.proyectofinal.View.YouTubeVideoPlayerView
import com.example.proyectofinal.ViewModel.BusquedaViewModel
import com.example.proyectofinal.ViewModel.PersonasViewModel
import com.example.proyectofinal.ViewModel.SeriesTvViewModel

@Composable
fun NavigationView() {
    val navController = rememberNavController()
    val viewModel: PeliculasViewModel = viewModel { PeliculasViewModel(RetrofitTmdb.api) }
    val viewModelSerie: SeriesTvViewModel = viewModel { SeriesTvViewModel(RetrofitTmdb.api) }
    val viewModelCelebridades: PersonasViewModel = viewModel { PersonasViewModel(RetrofitTmdb.api) }
    val viewModelBusqueda: BusquedaViewModel = viewModel { BusquedaViewModel(RetrofitTmdb.api) }

    val state by viewModel.state.collectAsState()
    val stateSerie by viewModelSerie.state.collectAsState()
    val stateCelebridades by viewModelCelebridades.state.collectAsState()
    val stateBusqueda by viewModelBusqueda.state.collectAsState()



    NavHost(navController = navController, startDestination = ObjVistas.Pelicula.route) {
        composable(ObjVistas.Pelicula.route) {
            PeliculaView(
                peliculasPopulares = state.peliculasPopulares,
                peliculasTendencia = state.peliculasTendencia,
                peliculasMejorValoradas = state.peliculasMejorValoradas,
                peliculasProximas = state.peliculasProximas,
                estaCargando = state.estaCargando,
                navController = navController
            )
        }

        composable(ObjVistas.ListaPeliculasPopulares.route) {
            PeliculaListaView(
                titulo = "Películas Populares",
                peliculas = state.peliculasPopulares,
                navController = navController
            )
        }

        composable(ObjVistas.ListaPeliculasTendencia.route) {
            PeliculaListaView(
                titulo = "Películas en Tendencia",
                peliculas = state.peliculasTendencia,
                navController = navController
            )
        }

        composable(ObjVistas.ListaPeliculasMejorValoradas.route) {
            PeliculaListaView(
                titulo = "Películas Mejor Valoradas",
                peliculas = state.peliculasMejorValoradas,
                navController = navController
            )
        }

        composable(ObjVistas.ListaPeliculasProximas.route) {
            PeliculaListaView(
                titulo = "Próximos Estrenos",
                peliculas = state.peliculasProximas,
                navController = navController
            )
        }

        composable("${ObjVistas.DetallesPelicula.route}/{movieId}") { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("movieId")?.toIntOrNull()
            movieId?.let {
                PeliculaDetalleView(
                    movieId = it,
                    navController = navController,
                    viewModel = viewModel
                )
            }
        }

        composable("${ObjVistas.ListaPeliculasRecomendadas.route}/{movieId}") { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("movieId")?.toIntOrNull()
            movieId?.let { id ->
                PeliculaListaView(
                    titulo = "Películas Recomendadas",
                    peliculas = viewModel.state.value.peliculasRecomendadas,
                    navController = navController
                )
            }
        }

        composable(
            route = "${ObjVistas.ReproducirVideo.route}/{videoKey}/{titulo}",
            arguments = listOf(
                navArgument("videoKey") { type = NavType.StringType },
                navArgument("titulo") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val videoKey = backStackEntry.arguments?.getString("videoKey")
            val titulo = backStackEntry.arguments?.getString("titulo")

            if (videoKey != null && titulo != null) {
                YouTubeVideoPlayerView(
                    videoKey = videoKey,
                    titulo = titulo,
                    navController = navController
                )
            }
        }

        composable("${ObjVistas.ListaPeliculasSimilares.route}/{movieId}") { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("movieId")?.toIntOrNull()
            movieId?.let { id ->
                PeliculaListaView(
                    titulo = "Películas Similares",
                    peliculas = viewModel.state.value.peliculasSimilares,
                    navController = navController
                )
            }
        }

        composable(ObjVistas.Buscar.route) {
            BuscarView(
                navController = navController,
                viewModelBusqueda = viewModelBusqueda
            )
        }
        composable(ObjVistas.ListaPeliculasBuscadas.route) {
            PeliculaListaView(
                titulo = "Películas Encontradas",
                peliculas = stateBusqueda.peliculasBuscadas,
                navController = navController
            )
        }

        composable(ObjVistas.ListaSeriesBuscadas.route) {
            SerieTvListaView(
                titulo = "Series Encontradas",
                series = stateBusqueda.serieTvBuscadas,
                navController = navController
            )
        }

        composable(ObjVistas.ListaCelebridadesBuscadas.route) {
            CelebridadesListaView(
                titulo = "Celebridades Encontradas",
                personas = stateBusqueda.personasBuscadas,
                navController = navController
            )
        }

        composable(ObjVistas.SerieTv.route) {
            SerieTvView(
                seriesAlAire = stateSerie.seriesAlAire,
                seriesTendencia = stateSerie.seriesTendencia,
                seriesMejorValoradas = stateSerie.seriesMejorValoradas,
                seriesPopulares = stateSerie.seriesPopulares,
                estaCargando = stateSerie.estaCargando,
                navController = navController
            )
        }

        composable(ObjVistas.ListaSeriesAlAire.route) {
            SerieTvListaView(
                titulo = "Series al Aire",
                series = stateSerie.seriesAlAire,
                navController = navController
            )
        }

        composable(ObjVistas.ListaSeriesTendencia.route) {
            SerieTvListaView(
                titulo = "Series en Tendencia",
                series = stateSerie.seriesTendencia,
                navController = navController
            )
        }

        composable(ObjVistas.ListaSeriesMejorValoradas.route) {
            SerieTvListaView(
                titulo = "Series Mejor Valoradas",
                series = stateSerie.seriesMejorValoradas,
                navController = navController
            )
        }

        composable(ObjVistas.ListaSeriesPopulares.route) {
            SerieTvListaView(
                titulo = "Series Populares",
                series = stateSerie.seriesPopulares,
                navController = navController
            )
        }

        composable("${ObjVistas.DetallesSerie.route}/{serieId}") { backStackEntry ->
            val serieId = backStackEntry.arguments?.getString("serieId")?.toIntOrNull()
            serieId?.let {
                SerieTvDetalleView(
                    serieId = it,
                    navController = navController,
                    viewModel = viewModelSerie
                )
            }
        }

        composable("${ObjVistas.ListaSeriesRecomendadas.route}/{serieId}") { backStackEntry ->
            val serieId = backStackEntry.arguments?.getString("serieId")?.toIntOrNull()
            serieId?.let {
                SerieTvListaView(
                    titulo = "Series Recomendadas",
                    series = viewModelSerie.state.value.seriesRecomendadas,
                    navController = navController
                )
            }
        }

        composable("${ObjVistas.ListaSeriesSimilares.route}/{serieId}") { backStackEntry ->
            val serieId = backStackEntry.arguments?.getString("serieId")?.toIntOrNull()
            serieId?.let {
                SerieTvListaView(
                    titulo = "Series Similares",
                    series = viewModelSerie.state.value.seriesSimilares,
                    navController = navController
                )
            }
        }

        composable(ObjVistas.ListaCelebridades.route) {
            CelebridadesView(
                navController = navController,
                celebridades = stateCelebridades.personasPopulares
            )
        }

        composable("${ObjVistas.DetallesCelebridad.route}/{personId}") { backStackEntry ->
            val personId = backStackEntry.arguments?.getString("personId")?.toIntOrNull()
            personId?.let {
                PersonaDetalleView(
                    personId = it,
                    navController = navController,
                    viewModel = viewModelCelebridades
                )
            }
        }

        composable("${ObjVistas.ListaPeliculasCelebridad.route}/{personId}") { backStackEntry ->
            val personId = backStackEntry.arguments?.getString("personId")?.toIntOrNull()
            personId?.let { id ->
                PeliculaListaView(
                    titulo = "Películas de la Celebridad",
                    peliculas = viewModelCelebridades.state.value.peliculasCelebridad,
                    navController = navController
                )
            }
        }

        composable("${ObjVistas.ListaSeriesCelebridad.route}/{personId}") { backStackEntry ->
            val personId = backStackEntry.arguments?.getString("personId")?.toIntOrNull()
            personId?.let { id ->
                SerieTvListaView(
                    titulo = "Series de la Celebridad",
                    series = viewModelCelebridades.state.value.seriesCelebridad,
                    navController = navController
                )
            }
        }

        composable(
            route = "${ObjVistas.VistaImagenCompleta.route}/{tipoImagen}/{initialIndex}/{tipo}",
            arguments = listOf(
                navArgument("tipoImagen") { type = NavType.StringType },
                navArgument("initialIndex") { type = NavType.IntType },
                navArgument("tipo") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val tipoImagen = backStackEntry.arguments?.getString("tipoImagen")
            val initialIndex = backStackEntry.arguments?.getInt("initialIndex") ?: 0
            val tipo = backStackEntry.arguments?.getString("tipo")

            val imagenes = when(tipo) {
                "pelicula" -> when(tipoImagen) {
                    "poster" -> viewModel.state.value.imagenesPelicula?.posters ?: emptyList()
                    "backdrop" -> viewModel.state.value.imagenesPelicula?.backdrops ?: emptyList()
                    else -> emptyList()
                }
                "serie" -> when(tipoImagen) {
                    "poster" -> viewModelSerie.state.value.imagenesSerie?.posters ?: emptyList()
                    "backdrop" -> viewModelSerie.state.value.imagenesSerie?.backdrops ?: emptyList()
                    else -> emptyList()
                }
                else -> emptyList()
            }

            if (imagenes.isNotEmpty()) {
                VistaImagenCompleta(
                    imagenes = imagenes,
                    tipoImagen = tipoImagen!!,
                    navController = navController,
                    initialIndex = initialIndex
                )
            }
        }
    }
}