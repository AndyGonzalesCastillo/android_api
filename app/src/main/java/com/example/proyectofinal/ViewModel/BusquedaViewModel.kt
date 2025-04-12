package com.example.proyectofinal.ViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinal.Conexion.ConexionTmdb
import com.example.proyectofinal.Conexion.TmdbApi
import com.example.proyectofinal.Model.Pelicula
import com.example.proyectofinal.Model.Persona
import com.example.proyectofinal.Model.RespuestaPeliculas
import com.example.proyectofinal.Model.RespuestaPersonas
import com.example.proyectofinal.Model.RespuestaSeriesTv
import com.example.proyectofinal.Model.SerieTv
import com.example.proyectofinal.Model.aPelicula
import com.example.proyectofinal.Model.aPersona
import com.example.proyectofinal.Model.aSerieTv
import com.example.proyectofinal.State.BusquedaState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BusquedaViewModel(private val api: TmdbApi) : ViewModel() {
    private val _state = MutableStateFlow(BusquedaState())
    val state: StateFlow<BusquedaState> = _state.asStateFlow()

    var mostrarResultados by mutableStateOf(false)

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

    private suspend fun cargarPaginasPersona(
        obtenerPersonas: suspend (Int) -> RespuestaPersonas
    ): List<Persona> {
        val maxPaginas = 10
        val todasLasCelebridades = mutableListOf<Persona>()

        for (pagina in 1..maxPaginas) {
            try {
                val respuesta = obtenerPersonas(pagina)
                todasLasCelebridades.addAll(respuesta.resultados.map { it.aPersona() })

                if (pagina >= respuesta.totalPaginas) break
            } catch (e: Exception) {
                break
            }
        }

        return todasLasCelebridades
    }

    fun buscarContenido(consulta: String) {
        viewModelScope.launch {
            mostrarResultados = true
            _state.update {
                it.copy(
                    estaCargando = true,
                    ultimaBusqueda = consulta,
                    mostrarAmbos = true
                )
            }
            try {
                val resultadosPeliculas = cargarPaginasPelicula {
                    api.buscarPeliculas(ConexionTmdb.API_KEY, consulta)
                }

                val resultadosSeries = cargarPaginasSerie {
                    api.buscarSeriesTv(ConexionTmdb.API_KEY, consulta)
                }

                val resultadosPersonas = cargarPaginasPersona {
                    api.buscarPersonas(ConexionTmdb.API_KEY, consulta)
                }

                _state.update {
                    it.copy(
                        peliculasBuscadas = resultadosPeliculas,
                        serieTvBuscadas = resultadosSeries,
                        personasBuscadas = resultadosPersonas,
                        estaCargando = false
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        estaCargando = false,
                        peliculasBuscadas = emptyList(),
                        serieTvBuscadas = emptyList(),
                        personasBuscadas = emptyList()
                    )
                }
            }
        }
    }
}