package com.example.practica3

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "videojuegos")
data class Videojuego(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val titulo: String,
    val precio: Double,
    val anio: Int,
    val plataforma: String,
    val completado: Boolean
)