package com.example.proyectofinal.View

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.proyectofinal.ViewModel.SeriesTvViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.proyectofinal.Navigation.ObjVistas
import com.example.proyectofinal.R
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.proyectofinal.Model.SerieTv

@Composable
fun SerieTvDetalleView(
    serieId: Int,
    navController: NavController,
    viewModel: SeriesTvViewModel
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(key1 = serieId) {
        viewModel.cargarDetalleSerie(serieId)
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
                        DetalleEncabezadoSerie(state.detalle!!, navController)
                        DetalleDescripcionSerie(state.detalle!!)

                        state.imagenesSerie?.let { imagenes ->
                            if (imagenes.posters.isNotEmpty() ||
                                imagenes.backdrops.isNotEmpty() ||
                                state.videosSerie.isNotEmpty()
                            ) {
                                FilaImagenes(
                                    posters = imagenes.posters,
                                    backdrops = imagenes.backdrops,
                                    videos = state.videosSerie.filter { it.sitio == "YouTube" },
                                    navController = navController,
                                    tipo = "serie"
                                )
                            }
                        }

                        if (state.seriesRecomendadas.isNotEmpty()) {
                            FilaSeriesTv(
                                titulo = "Series Recomendadas",
                                series = state.seriesRecomendadas,
                                navController = navController,
                                rutaVerTodo = "${ObjVistas.ListaSeriesRecomendadas.route}/$serieId"
                            )
                        }

                        if (state.seriesSimilares.isNotEmpty()) {
                            FilaSeriesTv(
                                titulo = "Series Similares",
                                series = state.seriesSimilares,
                                navController = navController,
                                rutaVerTodo = "${ObjVistas.ListaSeriesSimilares.route}/$serieId"
                            )
                        }
                    }
                }
                else -> {
                    Text(
                        text = "No se encontraron detalles de la serie",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
fun DetalleEncabezadoSerie(
    detalle: SerieTv,
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
                contentDescription = "Póster de la serie",
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
                    CompactInfoCard(
                        text = detalle.fechaEstreno.formatDate(),
                        icon1 = R.drawable.calendario
                    )
                    CompactInfoCard(
                        text = "${detalle.numeroTemporadas} Temporadas",

                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CompactInfoCard(
                        text = "${"%.1f".format(detalle.puntuacion)} | ${detalle.cantidadVotos}",
                        icon1 = R.drawable.star,
                        icon2 = R.drawable.person
                    )
                }
            }
        }
    }
}

@Composable
fun DetalleDescripcionSerie(detalle: SerieTv) {
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