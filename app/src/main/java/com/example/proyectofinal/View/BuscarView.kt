package com.example.proyectofinal.View

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.proyectofinal.Model.Pelicula
import com.example.proyectofinal.Model.Persona
import com.example.proyectofinal.Model.SerieTv
import com.example.proyectofinal.Navigation.ObjVistas
import com.example.proyectofinal.R
import com.example.proyectofinal.ViewModel.BusquedaViewModel



//COMENTARIO DE PRUEBA hola holaaaa
@Composable
fun BuscarView(
    navController: NavController,
    viewModelBusqueda: BusquedaViewModel
) {
    val state by viewModelBusqueda.state.collectAsState()
    var nombreBuscar by rememberSaveable { mutableStateOf("") }
    val mostrarResultados by remember { derivedStateOf { viewModelBusqueda.mostrarResultados } }

    Scaffold(
        topBar = { miTopBar("Buscador") },
        bottomBar = { miBottomBar(navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = nombreBuscar,
                    onValueChange = { nombreBuscar = it },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    label = { Text(text = "Buscar") },
                    placeholder = { Text(text = "Ingrese nombre a buscar") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Buscar contenido"
                        )
                    },
                    trailingIcon = {
                        if (nombreBuscar.isNotEmpty()) {
                            IconButton(onClick = { nombreBuscar = "" }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Limpiar búsqueda"
                                )
                            }
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            if (nombreBuscar.isNotBlank()) {
                                viewModelBusqueda.buscarContenido(nombreBuscar)
                            }
                        }
                    ),
                    shape = ShapeDefaults.Medium,
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                        unfocusedIndicatorColor = Color(0xFF0D47A1),
                        focusedIndicatorColor = Color(0xFF0D47A1)
                    ),
                    singleLine = true
                )
                Button(
                    onClick = {
                        if (nombreBuscar.isNotBlank()) {
                            viewModelBusqueda.buscarContenido(nombreBuscar)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4DD0E1)
                    ),
                    shape = ShapeDefaults.Medium
                ) {
                    Text("Buscar")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (nombreBuscar.isBlank() && !mostrarResultados) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Icono de búsqueda",
                            modifier = Modifier.size(96.dp),
                            tint = Color(0xFF4DD0E1)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Empieza buscando contenido usando el campo superior.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else if (state.estaCargando) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                if (state.peliculasBuscadas.isEmpty() &&
                    state.serieTvBuscadas.isEmpty() &&
                    state.personasBuscadas.isEmpty() &&
                    state.ultimaBusqueda.isNotBlank()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No se encontraron resultados para \"${state.ultimaBusqueda}\"",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Gray
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (state.peliculasBuscadas.isNotEmpty()) {
                            item {
                                FilaBuscarPeliculas(
                                    titulo = "Películas",
                                    peliculas = state.peliculasBuscadas,
                                    navController = navController,
                                    rutaVerTodo = ObjVistas.Buscar.route,
                                    totalPeliculas = state.peliculasBuscadas.size
                                )
                            }
                        }

                        if (state.serieTvBuscadas.isNotEmpty()) {
                            item {
                                FilaBuscarSeries(
                                    titulo = "Series",
                                    series = state.serieTvBuscadas,
                                    navController = navController,
                                    rutaVerTodo = ObjVistas.Buscar.route,
                                    totalSeries = state.serieTvBuscadas.size
                                )
                            }
                        }

                        if (state.personasBuscadas.isNotEmpty()) {
                            item {
                                FilaBuscarPersonas(
                                    titulo = "Personas",
                                    personas = state.personasBuscadas,
                                    navController = navController,
                                    rutaVerTodo = ObjVistas.Buscar.route,
                                    totalPersonas = state.personasBuscadas.size
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FilaBuscarPeliculas(
    titulo: String,
    peliculas: List<Pelicula>,
    navController: NavController,
    rutaVerTodo: String,
    totalPeliculas: Int,
    movieId: Int? = null
) {
    var isExpanded by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(color = Color(0xFF548DB7))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isExpanded = !isExpanded },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = titulo,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Box(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF4CAF50)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = totalPeliculas.toString(),
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Icon(
                    painter = painterResource(id = R.drawable.arriba),
                    contentDescription = "Expandir/Contraer",
                    modifier = Modifier
                        .size(24.dp)
                        .padding(start = 8.dp)
                        .rotate(if (isExpanded) 0f else 180f),
                    tint = Color.Black
                )
            }

            TextButton(
                onClick = {
                    if (movieId != null) {
                        navController.navigate("$rutaVerTodo/$movieId")
                    } else {
                        navController.navigate(ObjVistas.ListaPeliculasBuscadas.route)
                    }
                }
            ) {
                Text(
                    text = "Ver Más",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Blue
                )
                Icon(
                    painter = painterResource(id = R.drawable.flecha),
                    contentDescription = "Flecha Ver Más",
                    modifier = Modifier
                        .size(12.dp)
                        .padding(start = 4.dp),
                    tint = Color.Blue
                )
            }
        }

        if (isExpanded) {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
            ) {
                items(peliculas.take(20)) { pelicula ->
                    TarjetaBuscarPeliculas(
                        pelicula = pelicula,
                        onClick = {
                            navController.navigate("${ObjVistas.DetallesPelicula.route}/${pelicula.id}")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TarjetaBuscarPeliculas(pelicula: Pelicula, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .width(90.dp)
            .padding(end = 8.dp)
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(145.dp)
                .clip(RoundedCornerShape(12.dp))
        ) {
            AsyncImage(
                model = pelicula.rutaPoster,
                contentDescription = pelicula.titulo,
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.error_movie),
                error = painterResource(id = R.drawable.error_movie),
                modifier = Modifier.fillMaxSize()
            )
        }

        Text(
            text = pelicula.titulo,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(top = 1.dp)
                .fillMaxWidth()
                .height(50.dp),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 1.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Color(0xFFFFD700),
                    modifier = Modifier.size(10.dp)
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = "%.1f".format(pelicula.puntuacion),
                    fontSize = 10.sp,
                    color = Color(0xFFFFD700)
                )
            }

            Text(
                text = pelicula.fechaLanzamiento.split("-")[0],
                fontSize = 10.sp,
                color = Color.White
            )
        }
    }
}

@Composable
fun FilaBuscarSeries(
    titulo: String,
    series: List<SerieTv>,
    navController: NavController,
    rutaVerTodo: String,
    totalSeries: Int,
    serieId: Int? = null
) {
    var isExpanded by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(color = Color(0xFF548DB7))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isExpanded = !isExpanded },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = titulo,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Box(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF4CAF50)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = totalSeries.toString(),
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Icon(
                    painter = painterResource(id = R.drawable.arriba),
                    contentDescription = "Expandir/Contraer",
                    modifier = Modifier
                        .size(24.dp)
                        .padding(start = 8.dp)
                        .rotate(if (isExpanded) 0f else 180f),
                    tint = Color.Black
                )
            }

            TextButton(
                onClick = {
                    if (serieId != null) {
                        navController.navigate("$rutaVerTodo/$serieId")
                    } else {
                        navController.navigate(ObjVistas.ListaSeriesBuscadas.route)
                    }
                }
            ) {
                Text(
                    text = "Ver Más",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Blue
                )
                Icon(
                    painter = painterResource(id = R.drawable.flecha),
                    contentDescription = "Flecha Ver Más",
                    modifier = Modifier
                        .size(12.dp)
                        .padding(start = 4.dp),
                    tint = Color.Blue
                )
            }
        }

        if (isExpanded) {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
            ) {
                items(series.take(20)) { serie ->
                    TarjetaBuscarSerie(
                        serie = serie,
                        onClick = {
                            navController.navigate("${ObjVistas.DetallesSerie.route}/${serie.id}")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TarjetaBuscarSerie(serie: SerieTv, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .width(90.dp)
            .padding(end = 8.dp)
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(145.dp)
                .clip(RoundedCornerShape(12.dp))
        ) {
            AsyncImage(
                model = serie.rutaPoster,
                contentDescription = serie.titulo,
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.error_movie),
                error = painterResource(id = R.drawable.error_movie),
                modifier = Modifier.fillMaxSize()
            )
        }

        Text(
            text = serie.titulo,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(top = 1.dp)
                .fillMaxWidth()
                .height(50.dp),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 1.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Color(0xFFFFD700),
                    modifier = Modifier.size(10.dp)
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = "%.1f".format(serie.puntuacion),
                    fontSize = 10.sp,
                    color = Color(0xFFFFD700)
                )
            }

            Text(
                text = serie.fechaEstreno.split("-")[0],
                fontSize = 10.sp,
                color = Color.White
            )
        }
    }
}

@Composable
fun FilaBuscarPersonas(
    titulo: String,
    personas: List<Persona>,
    navController: NavController,
    rutaVerTodo: String,
    totalPersonas: Int,
    personaId: Int? = null
) {
    var isExpanded by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(color = Color(0xFF548DB7))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isExpanded = !isExpanded },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = titulo,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Box(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF4CAF50)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = totalPersonas.toString(),
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Icon(
                    painter = painterResource(id = R.drawable.arriba),
                    contentDescription = "Expandir/Contraer",
                    modifier = Modifier
                        .size(24.dp)
                        .padding(start = 8.dp)
                        .rotate(if (isExpanded) 0f else 180f),
                    tint = Color.Black
                )
            }

            TextButton(
                onClick = {
                    if (personaId != null) {
                        navController.navigate("$rutaVerTodo/$personaId")
                    } else {
                        navController.navigate(ObjVistas.ListaCelebridadesBuscadas.route)
                    }
                }
            ) {
                Text(
                    text = "Ver Más",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Blue
                )
                Icon(
                    painter = painterResource(id = R.drawable.flecha),
                    contentDescription = "Flecha Ver Más",
                    modifier = Modifier
                        .size(12.dp)
                        .padding(start = 4.dp),
                    tint = Color.Blue
                )
            }
        }

        if (isExpanded) {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
            ) {
                items(personas.take(20)) { persona ->
                    TarjetaBuscarPersona(
                        persona = persona,
                        onClick = {
                            navController.navigate("${ObjVistas.DetallesCelebridad.route}/${persona.id}")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TarjetaBuscarPersona(persona: Persona, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .width(90.dp)
            .padding(end = 8.dp)
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(145.dp)
                .clip(RoundedCornerShape(12.dp))
        ) {
            AsyncImage(
                model = persona.rutaPerfil,
                contentDescription = persona.nombre,
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.error_movie),
                error = painterResource(id = R.drawable.error_movie),
                modifier = Modifier.fillMaxSize()
            )
        }

        Text(
            text = persona.nombre,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(top = 1.dp)
                .fillMaxWidth()
                .height(50.dp),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = persona.ocupacion,
            fontSize = 10.sp,
            color = Color.White,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
