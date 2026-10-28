package com.example.practica3.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.practica3.Videojuego
import com.example.practica3.dao.JuegoDao

@Database(entities = [Videojuego::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun juegoDao(): JuegoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "gamevault_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}