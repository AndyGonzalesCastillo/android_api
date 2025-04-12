package com.example.proyectofinal.Retrofit

import com.example.proyectofinal.Conexion.ConexionTmdb
import com.example.proyectofinal.Conexion.TmdbApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitTmdb {
    val api: TmdbApi by lazy {
        Retrofit
            .Builder()
            .baseUrl(ConexionTmdb.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TmdbApi::class.java)
    }
}