package com.example.proyectofinal.View

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.proyectofinal.Model.ImagenDto
import com.example.proyectofinal.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VistaImagenCompleta(
    imagenes: List<ImagenDto>,
    tipoImagen: String,
    navController: NavController,
    initialIndex: Int = 0
) {
    var currentIndex by remember { mutableStateOf(initialIndex) }

    val pagerState = rememberPagerState(initialPage = initialIndex) { imagenes.size }

    LaunchedEffect(pagerState.currentPage) {
        currentIndex = pagerState.currentPage
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Imagen Completa") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                val fullImageUrl = when(tipoImagen) {
                    "poster" -> "https://image.tmdb.org/t/p/original${imagenes[page].rutaArchivo}"
                    "backdrop" -> "https://image.tmdb.org/t/p/original${imagenes[page].rutaArchivo}"
                    else -> ""
                }

                Crossfade(
                    targetState = fullImageUrl,
                    animationSpec = tween(durationMillis = 300),
                    label = "Image Crossfade"
                ) { imageUrl ->
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "Imagen completa",
                        placeholder = painterResource(id = R.drawable.error_movie),
                        error = painterResource(id = R.drawable.error_movie),
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer {
                                scaleX = 0.9f
                                scaleY = 0.9f
                                alpha = 0.99f
                            }
                            .scale(0.95f)
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            Text(
                text = "${currentIndex + 1} de ${imagenes.size}",
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .background(Color.Black.copy(alpha = 0.5f))
                    .padding(8.dp)
            )
        }
    }
}

