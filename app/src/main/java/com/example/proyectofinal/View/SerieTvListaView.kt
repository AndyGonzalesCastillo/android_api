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
import com.example.proyectofinal.Model.SerieTv
import com.example.proyectofinal.Navigation.ObjVistas

@Composable
fun SerieTvListaView(
    titulo: String,
    series: List<SerieTv>,
    navController: NavHostController
) {
    Scaffold(
        topBar = {
            miTopBarDetalle("$titulo (${series.size} series)", navController)
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
                items(series) { serie ->
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
}