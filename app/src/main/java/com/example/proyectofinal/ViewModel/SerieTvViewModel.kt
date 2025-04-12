package com.example.proyectofinal.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinal.Conexion.ConexionTmdb
import com.example.proyectofinal.Conexion.TmdbApi
import com.example.proyectofinal.Model.RespuestaSeriesTv
import com.example.proyectofinal.Model.SerieTv
import com.example.proyectofinal.Model.aSerieTv
import com.example.proyectofinal.State.SeriesTvState
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class SeriesTvViewModel(private val api: TmdbApi) : ViewModel() {
    private val _state = MutableStateFlow(SeriesTvState())
    val state: StateFlow<SeriesTvState> = _state.asStateFlow()

    init {
        cargarSeries()
    }

    private fun cargarSeries() {
        viewModelScope.launch {
            _state.value = _state.value.copy(estaCargando = true)

            val alAire = async {
                cargarPaginasSerie { api.obtenerSeriesAlAire(ConexionTmdb.API_KEY, "es-MX", it) }
            }
            val tendencia = async {
                cargarPaginasSerie { api.obtenerSeriesTendencia(ConexionTmdb.API_KEY, "es-MX", it) }
            }
            val mejorValoradas = async {
                cargarPaginasSerie { api.obtenerSeriesMejorValoradas(ConexionTmdb.API_KEY, "es-MX", it) }
            }
            val populares = async {
                cargarPaginasSerie { api.obtenerSeriesPopulares(ConexionTmdb.API_KEY, "es-MX", it) }
            }

            _state.value = _state.value.copy(
                seriesAlAire = alAire.await(),
                seriesTendencia = tendencia.await(),
                seriesMejorValoradas = mejorValoradas.await(),
                seriesPopulares = populares.await(),
                estaCargando = false
            )
        }
    }

    fun cargarDetalleSerie(serieId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(estaCargando = true) }

            try {
                val detalle = api.obtenerDetallesSerie(serieId, ConexionTmdb.API_KEY).aSerieTv()
                val recomendaciones = cargarPaginasSerie {
                    api.obtenerSeriesRecomendadas(serieId, ConexionTmdb.API_KEY, "es-MX", it)
                }
                val similares = cargarPaginasSerie {
                    api.obtenerSeriesSimilares(serieId, ConexionTmdb.API_KEY, "es-MX", it)
                }

                _state.update { currentState ->
                    currentState.copy(
                        detalle = detalle,
                        seriesRecomendadas = recomendaciones,
                        seriesSimilares = similares,
                        estaCargando = false
                    )
                }

                cargarImagenesSerie(serieId)
                cargarVideosSerie(serieId)
            } catch (e: Exception) {
                _state.update { it.copy(estaCargando = false) }
            }
        }
    }


    private suspend fun cargarPaginasSerie(
        obtenerSeries: suspend (Int) -> RespuestaSeriesTv
    ): List<SerieTv> {
        val maxPaginas = 10
        val todasLosSeries = mutableListOf<SerieTv>()

        for (pagina in 1..maxPaginas) {
            try {
                val respuesta = obtenerSeries(pagina)
                todasLosSeries.addAll(respuesta.resultados.map { it.aSerieTv() })

                if (pagina >= respuesta.totalPaginas) break
            } catch (e: Exception) {
                break
            }
        }

        return todasLosSeries
    }

    fun cargarImagenesSerie(serieId: Int) {
        viewModelScope.launch {
            try {
                val imagenes = api.obtenerImagenesSerie(serieId, ConexionTmdb.API_KEY)
                _state.update { currentState ->
                    currentState.copy(
                        imagenesSerie = imagenes,
                        estaCargando = false
                    )
                }
            } catch (e: Exception) {
                _state.update { it.copy(estaCargando = false) }
            }
        }
    }

    fun cargarVideosSerie(serieId: Int) {
        viewModelScope.launch {
            try {
                val videos = api.obtenerVideosSerie(serieId, ConexionTmdb.API_KEY)
                _state.update { currentState ->
                    currentState.copy(
                        videosSerie = videos.videos,
                        estaCargando = false
                    )
                }
            } catch (e: Exception) {
                _state.update { it.copy(estaCargando = false) }
            }
        }
    }

}