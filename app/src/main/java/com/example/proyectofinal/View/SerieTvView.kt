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
import com.example.proyectofinal.Model.SerieTv
import com.example.proyectofinal.Navigation.ObjVistas
import com.example.proyectofinal.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SerieTvView(
    seriesAlAire: List<SerieTv>,
    seriesTendencia: List<SerieTv>,
    seriesMejorValoradas: List<SerieTv>,
    seriesPopulares: List<SerieTv>,
    estaCargando: Boolean,
    navController: NavController
) {
    Scaffold(
        topBar = { miTopBar("Series TV") },
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
                if (seriesTendencia.isNotEmpty()) {
                    SeccionCarruselSerie(seriesTendencia, navController)
                    Spacer(modifier = Modifier.height(16.dp))
                }

                FilaSeriesTv(
                    titulo = "Populares",
                    series = seriesPopulares,
                    navController = navController,
                    rutaVerTodo = ObjVistas.ListaSeriesPopulares.route
                )
                SeccionSeriesMejorCalificadas(
                    titulo = "Mejor Calificadas",
                    series = seriesMejorValoradas,
                    navController = navController,
                    rutaVerTodo = ObjVistas.ListaSeriesMejorValoradas.route
                )
                FilaSeriesTv(
                    titulo = "Series al Aire",
                    series = seriesAlAire,
                    navController = navController,
                    rutaVerTodo = ObjVistas.ListaSeriesAlAire.route
                )
            }
        }
    }
}

@Composable
fun SeccionCarruselSerie(
    series: List<SerieTv>,
    navController: NavController
){
    val pagerState = rememberPagerState(pageCount = { series.size })
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        while (true) {
            delay(5000L)
            coroutineScope.launch {
                val nextPage = (pagerState.currentPage + 1) % series.size
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
            val serie = series[page]
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable {
                        navController.navigate("${ObjVistas.DetallesSerie.route}/${serie.id}")
                    }
            ) {
                AsyncImage(
                    model = serie.rutaBackdrop ?: serie.rutaPoster,
                    contentDescription = serie.titulo,
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
                        text = serie.titulo,
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
                            text = String.format("%.1f", serie.puntuacion),
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
fun FilaSeriesTv(
    titulo: String,
    series: List<SerieTv>,
    navController: NavController,
    rutaVerTodo: String,
    serieId: Int? = null
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
                        if (serieId != null) {
                            navController.navigate("$rutaVerTodo/$serieId")
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
            items(series.take(20)) { serie ->
                TarjetaSerieTv(
                    serie = serie,
                    onClick = {
                        navController.navigate("${ObjVistas.DetallesSerie.route}/${serie.id}")
                    }
                )
            }
        }
    }
}

@Composable
fun TarjetaSerieTv(serie: SerieTv, onClick: () -> Unit) {
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
                color = Color.Gray
            )
        }
    }
}

@Composable
fun SeccionSeriesMejorCalificadas(
    titulo: String,
    series: List<SerieTv>,
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
            items(series.chunked(4).take(5)) { filaSeries ->
                Column(
                    modifier = Modifier.padding(end = 50.dp)
                ) {
                    filaSeries.forEach { serie ->
                        TarjetaSerieMejorCalificada(serie) {
                            navController.navigate("${ObjVistas.DetallesSerie.route}/${serie.id}")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TarjetaSerieMejorCalificada(serie: SerieTv, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(4.dp)
            .width(220.dp)
            .height(100.dp)
            .clickable { onClick() }
    ) {
        AsyncImage(
            model = serie.rutaPoster,
            contentDescription = serie.titulo,
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
                text = serie.titulo,
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
                        text = "%.1f".format(serie.puntuacion),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = serie.fechaEstreno.split("-")[0],
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