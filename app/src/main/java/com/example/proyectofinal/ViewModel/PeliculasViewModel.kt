package com.example.proyectofinal.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinal.Conexion.ConexionTmdb
import com.example.proyectofinal.Conexion.TmdbApi
import com.example.proyectofinal.Model.Pelicula
import com.example.proyectofinal.Model.RespuestaPeliculas
import com.example.proyectofinal.Model.aPelicula
import com.example.proyectofinal.State.PeliculasState
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PeliculasViewModel(private val api: TmdbApi) : ViewModel() {
    private val _state = MutableStateFlow(PeliculasState())
    val state: StateFlow<PeliculasState> = _state.asStateFlow()

    init {
        cargarPeliculas()
    }

    private fun cargarPeliculas() {
        viewModelScope.launch {
            _state.value = _state.value.copy(estaCargando = true)

            val populares = async {
                cargarPaginasPelicula { api.obtenerPeliculasPopulares(ConexionTmdb.API_KEY, "es-MX", it) }
            }
            val tendencia = async {
                cargarPaginasPelicula { api.obtenerPeliculasTendencia(ConexionTmdb.API_KEY, "es-MX", it) }
            }
            val mejorValoradas = async {
                cargarPaginasPelicula { api.obtenerPeliculasMejorValoradas(ConexionTmdb.API_KEY, "es-MX", it) }
            }
            val proximas = async {
                cargarPaginasPelicula { api.obtenerPeliculasProximas(ConexionTmdb.API_KEY, "es-MX", it) }
            }

            _state.value = _state.value.copy(
                peliculasPopulares = populares.await(),
                peliculasTendencia = tendencia.await(),
                peliculasMejorValoradas = mejorValoradas.await(),
                peliculasProximas = proximas.await(),
                estaCargando = false
            )
        }
    }

    fun cargarDetallePelicula(movieId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(estaCargando = true) }

            try {
                val detalle = api.obtenerDetallesPelicula(movieId, ConexionTmdb.API_KEY).aPelicula()
                val recomendaciones = cargarPaginasPelicula {
                    api.obtenerPeliculasRecomendadas(movieId, ConexionTmdb.API_KEY,"es-MX", it)
                }
                val similares = cargarPaginasPelicula {
                    api.obtenerPeliculasSimilares(movieId, ConexionTmdb.API_KEY, "es-MX", it)
                }
                val videos = api.obtenerVideosPelicula(movieId, ConexionTmdb.API_KEY)

                _state.update { currentState ->
                    currentState.copy(
                        detalle = detalle,
                        peliculasRecomendadas = recomendaciones,
                        peliculasSimilares = similares,
                        videosPelicula = videos.videos,
                        estaCargando = false
                    )
                }
            } catch (e: Exception) {
                _state.update { it.copy(estaCargando = false) }
            }
            cargarVideosPelicula(movieId)
            cargarImagenesPelicula(movieId)
        }
    }

    suspend fun cargarPaginasPelicula(
        obtenerPeliculas: suspend (Int) -> RespuestaPeliculas
    ): List<Pelicula> {
        val maxPaginas = 10
        val todasLasPeliculas = mutableListOf<Pelicula>()

        for (pagina in 1..maxPaginas) {
            try {
                val respuesta = obtenerPeliculas(pagina)
                todasLasPeliculas.addAll(respuesta.resultados.map { it.aPelicula() })

                if (pagina >= respuesta.totalPaginas) break
            } catch (e: Exception) {
                break
            }
        }

        return todasLasPeliculas
    }
    fun cargarImagenesPelicula(movieId: Int) {
        viewModelScope.launch {
            try {
                val imagenes = api.obtenerImagenesPelicula(movieId, ConexionTmdb.API_KEY)
                _state.update { currentState ->
                    currentState.copy(
                        imagenesPelicula = imagenes,
                        estaCargando = false
                    )
                }
            } catch (e: Exception) {
                _state.update { it.copy(estaCargando = false) }
            }
        }
    }

    fun cargarVideosPelicula(movieId: Int) {
        viewModelScope.launch {
            try {
                val videos = api.obtenerVideosPelicula(movieId, ConexionTmdb.API_KEY)
                _state.update { currentState ->
                    currentState.copy(
                        videosPelicula = videos.videos,
                        estaCargando = false
                    )
                }
            } catch (e: Exception) {
                _state.update { it.copy(estaCargando = false) }
            }
        }
    }
}



