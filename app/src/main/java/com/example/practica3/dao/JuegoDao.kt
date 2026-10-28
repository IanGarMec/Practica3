package com.example.practica3.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.practica3.Videojuego
import kotlinx.coroutines.flow.Flow

@Dao
interface JuegoDao {

    @Query("SELECT * FROM videojuegos ORDER BY id DESC")
    fun obtenerTodos(): Flow<List<Videojuego>>
    @Query("SELECT * FROM videojuegos ORDER BY id ASC")
    suspend fun obtenerLista(): List<Videojuego>
    @Query("SELECT * FROM videojuegos WHERE id = :id")
    suspend fun buscarPorId(id: Long): Videojuego?
    @Insert
    suspend fun insertar(juego: Videojuego)
    @Update
    suspend fun actualizar(juego: Videojuego)
    @Delete
    suspend fun eliminar(juego: Videojuego)
}