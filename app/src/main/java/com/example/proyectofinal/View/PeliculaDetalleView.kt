package com.example.proyectofinal.View

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.proyectofinal.Model.ImagenDto
import com.example.proyectofinal.Model.Pelicula
import com.example.proyectofinal.Model.VideoDto
import com.example.proyectofinal.Navigation.ObjVistas
import com.example.proyectofinal.ViewModel.PeliculasViewModel
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import com.example.proyectofinal.R
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun PeliculaDetalleView(
    movieId: Int,
    navController: NavController,
    viewModel: PeliculasViewModel
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(key1 = movieId) {
        viewModel.cargarDetallePelicula(movieId)
    }

    Scaffold(
        topBar = {
            miTopBarDetalle("", navController as NavHostController)
        },
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
                state.detalle != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        DetalleEncabezado(state.detalle!!, navController)
                        DetalleDescripcion(state.detalle!!)

                        state.imagenesPelicula?.let { imagenes ->
                            if (imagenes.posters.isNotEmpty() ||
                                imagenes.backdrops.isNotEmpty() ||
                                state.videosPelicula.isNotEmpty()) {
                                FilaImagenes(
                                    posters = imagenes.posters,
                                    backdrops = imagenes.backdrops,
                                    videos = state.videosPelicula.filter { it.sitio == "YouTube" },
                                    navController = navController,
                                    tipo = "pelicula"
                                )
                            }
                        }

                        if (state.peliculasRecomendadas.isNotEmpty()) {
                            FilaPeliculas(
                                titulo = "Películas Recomendadas",
                                peliculas = state.peliculasRecomendadas,
                                navController = navController,
                                rutaVerTodo = "${ObjVistas.ListaPeliculasRecomendadas.route}/$movieId"
                            )
                        }

                        if (state.peliculasSimilares.isNotEmpty()) {
                            FilaPeliculas(
                                titulo = "Películas Similares",
                                peliculas = state.peliculasSimilares,
                                navController = navController,
                                rutaVerTodo = "${ObjVistas.ListaPeliculasSimilares.route}/$movieId"
                            )
                        }
                    }
                }
                else -> {
                    Text(
                        text = "No se encontraron detalles de la película",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
fun FilaImagenes(
    posters: List<ImagenDto>,
    backdrops: List<ImagenDto>,
    videos: List<VideoDto>,
    navController: NavController,
    alturaImagen: Dp = 200.dp,
    tipo: String = "pelicula"
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Posters", "Backdrops", "Videos")

    val imagesToShow = when (selectedTab) {
        0 -> posters
        1 -> backdrops
        else -> emptyList()
    }

    val videosToShow = if (selectedTab == 2) videos else emptyList()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color(0xF0A3BED3))
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))
    ) {
        TabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) }
                )
            }
        }

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            when (selectedTab) {
                0 -> {
                    itemsIndexed(imagesToShow) { index, imagen ->
                        AsyncImage(
                            model = "https://image.tmdb.org/t/p/w500${imagen.rutaArchivo}",
                            contentDescription = "Imagen de contenido",
                            modifier = Modifier
                                .height(alturaImagen)
                                .aspectRatio(2f / 3f)
                                .clip(RoundedCornerShape(8.dp))
                                .clickable {
                                    navController.navigate(
                                        "${ObjVistas.VistaImagenCompleta.route}/poster/$index/$tipo"
                                    )
                                },
                            contentScale = ContentScale.Crop
                        )
                    }
                }
                1 -> {
                    itemsIndexed(imagesToShow) { index, imagen ->
                        AsyncImage(
                            model = "https://image.tmdb.org/t/p/w780${imagen.rutaArchivo}",
                            contentDescription = "Imagen de contenido",
                            modifier = Modifier
                                .height(alturaImagen)
                                .aspectRatio(16f / 9f)
                                .clip(RoundedCornerShape(8.dp))
                                .clickable {
                                    navController.navigate(
                                        "${ObjVistas.VistaImagenCompleta.route}/backdrop/$index/$tipo"
                                    )
                                },
                            contentScale = ContentScale.Crop
                        )
                    }
                }
                2 -> {
                    items(videosToShow) { video ->
                        VideoThumbnail(
                            videoKey = video.clave,
                            titulo = video.nombre,
                            navController = navController,
                            modifier = Modifier
                                .height(alturaImagen)
                                .aspectRatio(16f / 9f)
                                .clip(RoundedCornerShape(8.dp))
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun VideoThumbnail(
    videoKey: String,
    titulo: String,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clickable {
                navController.navigate("${ObjVistas.ReproducirVideo.route}/$videoKey/$titulo")
            }
    ) {
        AsyncImage(
            model = "https://img.youtube.com/vi/$videoKey/0.jpg",
            contentDescription = titulo,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Icon(
            imageVector = Icons.Default.PlayArrow,
            contentDescription = "Reproducir video",
            tint = Color.White,
            modifier = Modifier
                .align(Alignment.Center)
                .size(50.dp)
                .background(Color.Black.copy(alpha = 0.5f), shape = CircleShape)
        )

        Text(
            text = titulo,
            style = MaterialTheme.typography.labelLarge,
            color = Color.White,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.7f))
                .padding(4.dp)
        )
    }
}

@Composable
fun DetalleEncabezado(
    detalle: Pelicula,
    navController: NavController
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp)
    ) {
        AsyncImage(
            model = detalle.rutaBackdrop ?: detalle.rutaPoster,
            contentDescription = "Imagen de fondo",
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer { alpha = 0.4f },
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.error_movie),
            error = painterResource(id = R.drawable.error_movie)
        )

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = detalle.rutaPoster,
                contentDescription = "Póster de la película",
                modifier = Modifier
                    .width(120.dp)
                    .aspectRatio(2 / 3f)
                    .clip(RoundedCornerShape(8.dp))
                    .border(2.dp, Color.White, RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.error_movie),
                error = painterResource(id = R.drawable.error_movie),
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = detalle.titulo,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CompactInfoCard(detalle.fechaLanzamiento.formatDate(), R.drawable.calendario)
                    CompactInfoCard("${detalle.duracion / 60}h ${detalle.duracion % 60}m")
                }

                Spacer(modifier = Modifier.height(8.dp))

                CompactInfoCard(
                    text = "${"%.1f".format(detalle.puntuacion)} | ${detalle.cantidadVotos}",
                    icon1 = R.drawable.star,
                    icon2 = R.drawable.person
                )
            }
        }
    }
}

@Composable
fun CompactInfoCard(text: String, icon1: Int? = null, icon2: Int? = null) {
    Row(
        modifier = Modifier
            .background(Color.LightGray, RoundedCornerShape(8.dp))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon1?.let {
            Icon(
                painter = painterResource(id = it),
                contentDescription = null,
                modifier = Modifier.size(13.dp),
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
        Text(text = text, fontSize = 10.sp)
        Spacer(modifier = Modifier.size(8.dp))
        icon2?.let {
            Icon(
                painter = painterResource(id = it),
                contentDescription = null,
                modifier = Modifier.size(13.dp),
                tint = Color.Unspecified
            )
        }
    }
}

fun String.formatDate(): String {
    val parts = this.split("-")
    return if (parts.size == 3) {
        val year = parts[0]
        val month = parts[1].toIntOrNull()?.let {
            listOf("Ene.", "Feb.", "Mar.", "Abr.", "May.", "Jun.", "Jul.", "Ago.", "Sep.", "Oct.", "Nov.", "Dic.")[it - 1]
        } ?: "Desconocido"
        val day = parts[2]
        "$day $month $year"
    } else {
        this
    }
}

@Composable
fun DetalleDescripcion(detalle: Pelicula) {
    var isDescripcionExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        Text(
            text = detalle.descripcion,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            color = Color.Black,
            maxLines = if (isDescripcionExpanded) Int.MAX_VALUE else 5,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFF94BBDA))
                .clickable { isDescripcionExpanded = !isDescripcionExpanded }
                .padding(top = 32.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
        )

        Text(
            text = "Sinopsis",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.TopStart)
                .wrapContentWidth()
                .offset(y = (-10).dp, x = 4.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Gray)
                .padding(8.dp)
        )
    }
}