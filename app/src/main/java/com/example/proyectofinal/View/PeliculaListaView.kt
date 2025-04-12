package com.example.proyectofinal.View

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.proyectofinal.Model.Pelicula
import com.example.proyectofinal.Navigation.ObjVistas

@Composable
fun PeliculaListaView( titulo: String, peliculas: List<Pelicula>, navController: NavHostController ) {
    Scaffold(
        topBar = {
            miTopBarDetalle("$titulo (${peliculas.size} películas)", navController)
                 },
        bottomBar = { miBottomBar(navController) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = paddingValues
            ) {
                items(peliculas) { pelicula ->
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
}