package com.example.todoapp.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todoapp.viewmodel.TodoViewModel
import com.example.todoapp.viewmodel.FilterType
import com.example.todoapp.ui.theme.TodoItem

@Composable
fun TodoScreen(vm: TodoViewModel = viewModel()) {
    val filter by vm.filter.collectAsState()
    val searchQuery by vm.searchQuery.collectAsState()
    val filteredTodos = vm.getFilteredTodos()

    var text by rememberSaveable { mutableStateOf("") }
    var menuTerbuka by rememberSaveable { mutableStateOf(false) }

    Column(Modifier.padding(16.dp)) {

        Column(Modifier.padding(16.dp)) {
            // Input untuk menambah todo baru
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Tambah tugas...") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        if (text.isNotBlank()) {
                            vm.addTask(text.trim())
                            text = ""
                        }
                    }
                ) { Text("Tambah") }

                Box {
                    IconButton(onClick = { menuTerbuka = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Filter")
                    }

                    DropdownMenu(
                        expanded = menuTerbuka,
                        onDismissRequest = { menuTerbuka = false }
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    "Semua",
                                    color = if (filter == FilterType.ALL) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                )
                            },
                            onClick = {
                                vm.setFilter(FilterType.ALL)
                                menuTerbuka = false
                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Text(
                                    "Aktif",
                                    color = if (filter == FilterType.ACTIVE) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                )
                            },
                            onClick = {
                                vm.setFilter(FilterType.ACTIVE)
                                menuTerbuka = false
                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Text(
                                    "Selesai",
                                    color = if (filter == FilterType.DONE) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                )
                            },
                            onClick = {
                                vm.setFilter(FilterType.DONE)
                                menuTerbuka = false
                            }
                        )
                    }
                }
            }

            // 🔍 Pencarian realtime
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { vm.setSearchQuery(it) },
                label = { Text("Cari tugas...") },
                modifier = Modifier.fillMaxWidth()
            )

            // 📊 Statistik jumlah tugas
            Text(
                text = "Aktif: ${vm.getActiveCount()} | Selesai: ${vm.getDoneCount()}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Divider(Modifier.padding(vertical = 8.dp))

            Divider(Modifier.padding(vertical = 8.dp))

            // Daftar todo ditampilkan sesuai filter aktif
            LazyColumn {
                items(filteredTodos) { todo ->
                    TodoItem(
                        todo = todo,
                        onToggle = { vm.toggleTask(todo.id) },
                        onDelete = { vm.deleteTask(todo.id) }
                    )
                }
            }
        }
    }
}
