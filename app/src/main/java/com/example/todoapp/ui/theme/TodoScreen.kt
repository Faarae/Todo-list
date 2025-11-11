package com.example.todoapp.ui.theme

import android.widget.CheckBox
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todoapp.viewmodel.TodoViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.ui.Modifier
import androidx.compose.material.icons.filled.MoreVert

@Composable
fun TodoScreen(vm: TodoViewModel = viewModel()) {
    val todos by vm.todos.collectAsState()
    var text by rememberSaveable { mutableStateOf("") }
    var menuTerbuka by rememberSaveable { mutableStateOf(false) }
    Column(Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Tambah tugas...") },
            modifier = Modifier.fillMaxWidth()
        )
        Row(Modifier.padding(16.dp)) {
            Button(
                onClick = {
                    if (text.isNotBlank()) {
                        vm.addTask(text.trim())
                        text = ""
                    }
                },
                modifier = Modifier.padding(vertical = 8.dp)
            ) { Text("Tambah") }
            Box{
                IconButton(
                    onClick = { menuTerbuka = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Pilihan lainnya")
                }
                DropdownMenu(
                    expanded = menuTerbuka,
                    onDismissRequest = { menuTerbuka = false }
                ) {
                    Checkbox(checked = true, onCheckedChange = null)
                    Checkbox(checked = true, onCheckedChange = null)
                    Checkbox(checked = true, onCheckedChange = null)
                }
            }
        }
        HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
        LazyColumn {
            items(todos) { todo ->
                TodoItem(
                    todo = todo,
                    onToggle = { vm.toggleTask(todo.id) },
                    onDelete = { vm.deleteTask(todo.id) }
                )
            }
        }
    }
}