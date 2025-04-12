package com.example.proyectofinal.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofinal.Conexion.ConexionTmdb
import com.example.proyectofinal.Conexion.TmdbApi
import com.example.proyectofinal.Model.Persona
import com.example.proyectofinal.Model.RespuestaPersonas
import com.example.proyectofinal.Model.aPelicula
import com.example.proyectofinal.Model.aPersona
import com.example.proyectofinal.Model.aSerieTv
import com.example.proyectofinal.State.PersonasState
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PersonasViewModel(private val api: TmdbApi) : ViewModel() {
    private val _state = MutableStateFlow(PersonasState())
    val state: StateFlow<PersonasState> = _state.asStateFlow()

    init {
        cargarPersonas()
    }

    private fun cargarPersonas() {
        viewModelScope.launch {
            _state.value = _state.value.copy(estaCargando = true)
            try {
                val populares = async {
                    cargarPaginasPersona { api.obtenerPersonasPopulares(ConexionTmdb.API_KEY, "es-MX", it) }
                }
                _state.value = _state.value.copy(
                    personasPopulares = populares.await(),
                    estaCargando = false
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(estaCargando = false)
            }
        }
    }

    fun cargarDatosPersona(personId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(estaCargando = true) }
            try {
                val detalle = async { api.obtenerDetallePersona(personId, ConexionTmdb.API_KEY, "es-MX").aPersona() }
                val peliculas = async { api.obtenerPeliculasCelebridad(personId, ConexionTmdb.API_KEY).elenco.map { it.aPelicula() } }
                val series = async { api.obtenerSeriesCelebridad(personId, ConexionTmdb.API_KEY).elenco.map { it.aSerieTv() } }
                val imagenes = async {
                    api.obtenerImagenesPersona(personId, ConexionTmdb.API_KEY).perfiles.map {
                        "https://image.tmdb.org/t/p/w500${it.rutaArchivo}"
                    }
                }

                _state.update { currentState ->
                    currentState.copy(
                        detallePersona = detalle.await(),
                        peliculasCelebridad = peliculas.await(),
                        seriesCelebridad = series.await(),
                        imagenesPersona = imagenes.await(),
                        estaCargando = false
                    )
                }
            } catch (e: Exception) {
                _state.update { it.copy(estaCargando = false) }
            }
        }
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
}
