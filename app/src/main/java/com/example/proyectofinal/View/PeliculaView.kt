package com.example.proyectofinal.View


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import androidx.navigation.NavHostController
import com.example.proyectofinal.Model.Pelicula
import com.example.proyectofinal.Navigation.ObjVistas
import com.example.proyectofinal.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PeliculaView(
    peliculasPopulares: List<Pelicula>,
    peliculasTendencia: List<Pelicula>,
    peliculasMejorValoradas: List<Pelicula>,
    peliculasProximas: List<Pelicula>,
    estaCargando: Boolean,
    navController: NavController
) {
    Scaffold(
        topBar = { miTopBar("Películas") },
        bottomBar = { miBottomBar(navController as NavHostController) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (estaCargando) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(50.dp)
                        .align(Alignment.Center)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                if (peliculasTendencia.isNotEmpty()) {
                    SeccionCarrusel(peliculasTendencia, navController)
                    Spacer(modifier = Modifier.height(16.dp))
                }

                FilaPeliculas(
                    titulo = "Populares",
                    peliculas = peliculasPopulares,
                    navController = navController,
                    rutaVerTodo = ObjVistas.ListaPeliculasPopulares.route
                )
                SeccionPeliculasMejorCalificadas(
                    titulo = "Mejor Calificadas",
                    peliculas = peliculasMejorValoradas,
                    navController = navController,
                    rutaVerTodo = ObjVistas.ListaPeliculasMejorValoradas.route
                )
                FilaPeliculas(
                    titulo = "Próximas extrenos",
                    peliculas = peliculasProximas,
                    navController = navController,
                    rutaVerTodo = ObjVistas.ListaPeliculasProximas.route
                )
            }
        }
    }
}

@Composable
fun SeccionCarrusel(
    peliculas: List<Pelicula>,
    navController: NavController
){
    val pagerState = rememberPagerState(pageCount = { peliculas.size })
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        while (true) {
            delay(5000L)
            coroutineScope.launch {
                val nextPage = (pagerState.currentPage + 1) % peliculas.size
                pagerState.animateScrollToPage(nextPage)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) { page ->
            val pelicula = peliculas[page]
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable {
                        navController.navigate("${ObjVistas.DetallesPelicula.route}/${pelicula.id}")
                    }
            ) {
                AsyncImage(
                    model = pelicula.rutaBackdrop ?: pelicula.rutaPoster,
                    contentDescription = pelicula.titulo,
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.error_movie),
                    error = painterResource(id = R.drawable.error_movie),
                    modifier = Modifier.fillMaxSize()
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.1f),
                                    Color.Black.copy(alpha = 0.8f)
                                ),
                                startY = 0f,
                                endY = Float.POSITIVE_INFINITY
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Text(
                        text = pelicula.titulo,
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color.Yellow,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = String.format("%.1f", pelicula.puntuacion),
                            color = Color.White,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        Row(
            Modifier
                .height(20.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val color = if (pagerState.currentPage == iteration)
                    Color.White
                else
                    Color.White.copy(alpha = 0.5f)
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(8.dp)
                )
            }
        }
    }
}

@Composable
fun FilaPeliculas(
    titulo: String,
    peliculas: List<Pelicula>,
    navController: NavController,
    rutaVerTodo: String,
    movieId: Int? = null
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = titulo,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            if (rutaVerTodo.isNotEmpty()) {
                TextButton(
                    onClick = {
                        if (movieId != null) {
                            navController.navigate("$rutaVerTodo/$movieId")
                        } else {
                            navController.navigate(rutaVerTodo)
                        }
                    }
                ) {
                    Text(
                        text = "Ver Todo",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Blue
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.flecha),
                        contentDescription = "Flecha Ver Todo",
                        modifier = Modifier
                            .size(12.dp)
                            .padding(start = 4.dp),
                        tint = Color.Blue
                    )
                }
            }
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(peliculas.take(20)) { pelicula ->
                TarjetaPelicula(
                    pelicula = pelicula,
                    onClick = {
                        navController.navigate("${ObjVistas.DetallesPelicula.route}/${pelicula.id}")
                    }
                )
            }
        }
    }
}


@Composable
fun TarjetaPelicula(pelicula: Pelicula, onClick: () -> Unit) {
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
                .clip(RoundedCornerShape(8.dp))
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
                color = Color.Gray
            )
        }
    }
}

@Composable
fun SeccionPeliculasMejorCalificadas(
    titulo: String,
    peliculas: List<Pelicula>,
    navController: NavController,
    rutaVerTodo: String
) {
    Column(modifier = Modifier.padding(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = titulo,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            TextButton(
                onClick = {
                    navController.navigate(rutaVerTodo)
                }
            ) {
                Text(
                    text = "Ver Todo",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Blue
                )
                Icon(
                    painter = painterResource(id = R.drawable.flecha),
                    contentDescription = "Flecha Ver Todo",
                    modifier = Modifier
                        .size(12.dp)
                        .padding(start = 4.dp),
                    tint = Color.Blue
                )
            }
        }

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            items(peliculas.chunked(4).take(5)) { filaPeliculas ->
                Column(
                    modifier = Modifier.padding(end = 50.dp)
                ) {
                    filaPeliculas.forEach { pelicula ->
                        TarjetaPeliculaMejorCalificada(pelicula) {
                            navController.navigate("${ObjVistas.DetallesPelicula.route}/${pelicula.id}")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TarjetaPeliculaMejorCalificada(pelicula: Pelicula, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(4.dp)
            .width(220.dp)
            .height(100.dp)
            .clickable { onClick() }
    ) {
        AsyncImage(
            model = pelicula.rutaPoster,
            contentDescription = pelicula.titulo,
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.error_movie),
            error = painterResource(id = R.drawable.error_movie),
            modifier = Modifier
                .width(90.dp)
                .height(100.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = pelicula.titulo,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "%.1f".format(pelicula.puntuacion),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = pelicula.fechaLanzamiento.split("-")[0],
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.width(10.dp))

        Icon(
            painter = painterResource(id = R.drawable.flecha),
            contentDescription = "Flecha",
            modifier = Modifier
                .size(16.dp)
                .align(Alignment.CenterVertically),
            tint = Color.Gray
        )
    }
}