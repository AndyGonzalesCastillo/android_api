package com.example.proyectofinal.View

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.proyectofinal.Model.Persona
import com.example.proyectofinal.Navigation.ObjVistas
import com.example.proyectofinal.R
import com.example.proyectofinal.ViewModel.PersonasViewModel

@Composable
fun PersonaDetalleView(
    personId: Int,
    navController: NavController,
    viewModel: PersonasViewModel
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(key1 = personId) {
        viewModel.cargarDatosPersona(personId)
    }

    Scaffold(
        topBar = { miTopBarDetalle("", navController as NavHostController) },
        bottomBar = { miBottomBar(navController as NavHostController) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                state.estaCargando -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(50.dp)
                            .align(Alignment.Center)
                    )
                }
                state.detallePersona != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        DetalleEncabezadoPersona(state.detallePersona!!, navController)
                        DetalleDescripcionPersona(
                            detalle = state.detallePersona!!,
                            navController = navController,
                            viewModel = viewModel
                        )
                        if (state.peliculasCelebridad.isNotEmpty()) {
                            FilaPeliculas(
                                titulo = "Películas",
                                peliculas = state.peliculasCelebridad,
                                navController = navController,
                                rutaVerTodo = "${ObjVistas.ListaPeliculasCelebridad.route}/$personId"
                            )
                        }

                        if (state.seriesCelebridad.isNotEmpty()) {
                            FilaSeriesTv(
                                titulo = "Series de TV",
                                series = state.seriesCelebridad,
                                navController = navController,
                                rutaVerTodo = "${ObjVistas.ListaSeriesCelebridad.route}/$personId"
                            )
                        }
                    }
                }
                else -> {
                    Text(
                        text = "No se encontraron detalles de la celebridad",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
fun DetalleEncabezadoPersona(
    detalle: Persona,
    navController: NavController
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(IntrinsicSize.Min)
    ) {
        AsyncImage(
            model = detalle.rutaPerfil,
            contentDescription = "Imagen de perfil",
            modifier = Modifier
                .width(150.dp)
                .fillMaxHeight()
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.error_movie),
            error = painterResource(id = R.drawable.error_movie)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = detalle.nombre,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            detalle.genero?.let {
                val generoTexto = when (it) {
                    1 -> "Femenino"
                    2 -> "Masculino"
                    else -> "No especificado"
                }
                Text(
                    text = "Género: $generoTexto",
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }

            detalle.ocupacion?.let {
                Text(
                    text = "Ocupación: $it",
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }

            detalle.fechaNacimiento?.let {
                Text(
                    text = "Fecha de nacimiento: ${it.formatDate()}",
                    fontSize = 14.sp,
                    color = Color.Black
                )
            }

            detalle.lugarNacimiento?.let {
                Text(
                    text = "Lugar de nacimiento: $it",
                    fontSize = 14.sp,
                    color = Color.Black
                )
            }

            detalle.fechaMuerte?.let {
                Text(
                    text = "Fallecido: ${it.formatDate()}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun DetalleDescripcionPersona(
    detalle: Persona,
    navController: NavController,
    viewModel: PersonasViewModel
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Biografía",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = detalle.biografia ?: "Biografía no disponible",
            fontSize = 16.sp,
            lineHeight = 24.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (state.imagenesPersona.isNotEmpty()) {
            Text(
                text = "Imágenes",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.imagenesPersona) { imagenUrl ->
                    AsyncImage(
                        model = imagenUrl,
                        contentDescription = "Imagen de ${detalle.nombre}",
                        modifier = Modifier
                            .width(150.dp)
                            .height(200.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(id = R.drawable.error_movie),
                        error = painterResource(id = R.drawable.error_movie)
                    )
                }
            }
        }
    }
}