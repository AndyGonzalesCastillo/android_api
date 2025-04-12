package com.example.proyectofinal.View

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.proyectofinal.Navigation.ObjVistas
import com.example.proyectofinal.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun miTopBar(titulo: String) {
    TopAppBar(
        title = { Text(text = titulo, color = Color.White) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF0D47A1)
        )
    )
}

@Composable
fun miBottomBar(navController: NavController) {
    BottomAppBar(
        modifier = Modifier.fillMaxWidth(),
        containerColor = Color(0xFF0D47A1),
        contentColor = Color.White
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconoTexto(painterResource(id = R.drawable.movies), "Películas") {
                navController.navigate(ObjVistas.Pelicula.route)
            }
            IconoTexto(painterResource(id = R.drawable.tv), "TV") {
                navController.navigate(ObjVistas.SerieTv.route)
            }
            IconoTexto(Icons.Default.Person, "Gente", {
                navController.navigate(ObjVistas.ListaCelebridades.route)
            })
            IconoTexto(Icons.Default.Favorite, "Favoritos", {

            })
            IconoTexto(Icons.Default.Search, "Buscar", {
                navController.navigate(ObjVistas.Buscar.route)
            })
        }
    }
}

@Composable
fun IconoTexto(icon: Any, label: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp).clickable { onClick() }
    ) {
        when (icon) {
            is ImageVector -> Icon(
                imageVector = icon,
                contentDescription = label,
                tint = Color.White,
                modifier = Modifier.size(30.dp)
            )
            is Painter -> Icon(
                painter = icon,
                contentDescription = label,
                tint = Color.White,
                modifier = Modifier.size(30.dp)
            )
        }
        Text(
            text = label,
            color = Color.White,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun miTopBarDetalle(titulo: String, navController: NavHostController) {
    TopAppBar(
        title = {
            Text(
                text = titulo,
                color = Color.White,
                style = TextStyle(fontSize = 16.sp)
            )
        },
        navigationIcon = {
            IconButton(onClick = {
                if (navController.previousBackStackEntry != null) {
                    navController.popBackStack()
                }
            }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Atrás", tint = Color.White)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF0D47A1)
        )
    )
}