package com.example.practica3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.practica3.viewmodel.JuegoViewModel

class MainActivity : ComponentActivity() {

    private val vm: JuegoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    PantallaPrincipal(vm)
                }
            }
        }
    }
}

@Composable
fun PantallaPrincipal(vm: JuegoViewModel) {
    val juegos by vm.juegos.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp).statusBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item { Text("Videojuegos", style = MaterialTheme.typography.headlineMedium) }

        item {
            OutlinedTextField(vm.titulo, { vm.titulo = it },
                label = { Text("Título") }, modifier = Modifier.fillMaxWidth())
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(vm.precio, { vm.precio = it },
                    label = { Text("Precio") }, modifier = Modifier.weight(1f))
                OutlinedTextField(vm.anio, { vm.anio = it },
                    label = { Text("Año") }, modifier = Modifier.weight(1f))
            }
        }
        item {
            OutlinedTextField(vm.plataforma, { vm.plataforma = it },
                label = { Text("Plataforma") }, modifier = Modifier.fillMaxWidth())
        }
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("¿Completado?")
                Checkbox(vm.completado, { vm.completado = it })
            }
        }
        item {
            Button(onClick = { vm.agregarJuego() }, modifier = Modifier.fillMaxWidth()) {
                Text("Guardar juego")
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = { vm.cargarCincoRegistros() }) { Text("Cargar 5") }
                OutlinedButton(onClick = { vm.modificarDosRegistros() }) { Text("Modificar 2") }
            }
        }


        item { HorizontalDivider() }
        item { Text("Consultar por #ID", style = MaterialTheme.typography.titleMedium) }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(vm.idBusqueda, { vm.idBusqueda = it },
                    label = { Text("ID") }, modifier = Modifier.weight(1f))
                Button(onClick = { vm.fetchJuego() }) { Text("Buscar") }
            }
        }
        item {
            vm.resultado?.let { j ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(12.dp)) {
                        Text("Título: ${j.titulo}")
                        Text("Precio: $${j.precio}")
                        Text("Completado: ${if (j.completado) "Sí" else "No"}")
                    }
                }
            }
        }
        item { if (vm.mensaje.isNotEmpty()) Text(vm.mensaje, color = MaterialTheme.colorScheme.primary) }

        item { HorizontalDivider() }
        item { Text("Mi colección:", style = MaterialTheme.typography.titleMedium) }
        items(juegos, key = { it.id }) { j ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text("#${j.id} ${j.titulo} (${j.anio})")
                        Text("${j.plataforma} · $${"%.2f".format(j.precio)}")
                        Text(if (j.completado) "Completado" else " Pendiente")
                    }
                    TextButton(onClick = { vm.eliminarJuego(j) }) { Text("Borrar") }
                }
            }
        }
    }
}