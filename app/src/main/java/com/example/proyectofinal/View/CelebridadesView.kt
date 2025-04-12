package com.example.proyectofinal.View

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.navigation.NavHostController
import com.example.proyectofinal.Model.Persona
import com.example.proyectofinal.R

@Composable
fun CelebridadesView(navController: NavHostController, celebridades: List<Persona>) {
    Scaffold(
        topBar = { miTopBar("Celebridades") },
        bottomBar = { miBottomBar(navController) },
        content = { paddingValues ->
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(celebridades.size) { index ->
                    val celebridad = celebridades[index]
                    CelebridadCard(celebridad = celebridad) {
                        navController.navigate("detalles_celebridad/${celebridad.id}")
                    }
                }
            }
        }
    )
}
@Composable
fun CelebridadCard(celebridad: Persona, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .width(140.dp)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = "https://image.tmdb.org/t/p/w500${celebridad.rutaPerfil}",
            contentDescription = celebridad.nombre,
            modifier = Modifier
                .size(120.dp, 160.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(2.dp, Color.Gray, RoundedCornerShape(16.dp)),
            placeholder = painterResource(id = R.drawable.error_movie),
            error = painterResource(id = R.drawable.error_movie),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = celebridad.nombre,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = celebridad.ocupacion,
            fontSize = 12.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
