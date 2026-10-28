package com.example.practica3.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.practica3.database.AppDatabase
import com.example.practica3.Videojuego
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class JuegoViewModel(app: Application) : AndroidViewModel(app) {

    private val dao = AppDatabase.getDatabase(app).juegoDao()

    val juegos = dao.obtenerTodos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    var titulo by mutableStateOf("")
    var precio by mutableStateOf("")
    var anio by mutableStateOf("")
    var plataforma by mutableStateOf("")
    var completado by mutableStateOf(false)
    var idBusqueda by mutableStateOf("")
    var resultado by mutableStateOf<Videojuego?>(null)
    var mensaje by mutableStateOf("")

    fun agregarJuego() {
        val p = precio.toDoubleOrNull()
        val a = anio.toIntOrNull()
        if (titulo.isBlank() || p == null || a == null) {
            mensaje = "Revisa los campos: título, precio y año son obligatorios"
            return
        }
        viewModelScope.launch {
            dao.insertar(
                Videojuego(
                    titulo = titulo, precio = p, anio = a,
                    plataforma = plataforma, completado = completado
                )
            )
            titulo = ""; precio = ""; anio = ""; plataforma = ""; completado = false
            mensaje = "Juego guardado"
        }
    }

    fun cargarCincoRegistros() {
        viewModelScope.launch {
            val iniciales = listOf(
                Videojuego(titulo = "Hollow Knight", precio = 15.0, anio = 2017, plataforma = "PC", completado = true),
                Videojuego(titulo = "Zelda: Breath of the Wild", precio = 60.0, anio = 2017, plataforma = "Switch", completado = false),
                Videojuego(titulo = "Celeste", precio = 20.0, anio = 2018, plataforma = "PC", completado = true),
                Videojuego(titulo = "God of War", precio = 40.0, anio = 2018, plataforma = "PS4", completado = false),
                Videojuego(titulo = "Stardew Valley", precio = 14.5, anio = 2016, plataforma = "PC", completado = false)
            )
            iniciales.forEach { dao.insertar(it) }
            mensaje = "5 registros cargados"
        }
    }

    fun modificarDosRegistros() {
        viewModelScope.launch {
            val lista = dao.obtenerLista()
            if (lista.size < 2) {
                mensaje = "Necesitas al menos 2 registros"
                return@launch
            }
            dao.actualizar(lista[0].copy(completado = !lista[0].completado))
            dao.actualizar(lista[1].copy(precio = lista[1].precio * 0.8))
            mensaje = "2 registros modificados"
        }
    }

    fun fetchJuego() {
        val id = idBusqueda.toLongOrNull()
        if (id == null) {
            mensaje = "Escribe un ID válido"
            return
        }
        viewModelScope.launch {
            resultado = dao.buscarPorId(id)
            mensaje = if (resultado == null) "No existe el ID $id" else "Registro encontrado"
        }
    }
    fun eliminarJuego(juego: Videojuego) {
        viewModelScope.launch { dao.eliminar(juego) }
    }
}