package com.example.todoapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.todoapp.model.Todo
import kotlinx.coroutines.flow.asStateFlow

enum class FilterType { ALL, ACTIVE, DONE }

class TodoViewModel : ViewModel() {

    private val _todos = MutableStateFlow<List<Todo>>(emptyList())
    val todos = _todos.asStateFlow()

    private val _filter = MutableStateFlow(FilterType.ALL)
    val filter: StateFlow<FilterType> = _filter

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    fun addTask(title: String) {
        val nextId = (_todos.value.maxOfOrNull { it.id } ?: 0) + 1
        val newTask = Todo(id = nextId, title = title)
        _todos.value = _todos.value + newTask
    }

    fun toggleTask(id: Int) {
        _todos.value = _todos.value.map { t ->
            if (t.id == id) t.copy(isDone = !t.isDone) else t
        }
    }

    fun deleteTask(id: Int) {
        _todos.value = _todos.value.filterNot { it.id == id }
    }

    fun setFilter(type: FilterType) {
        _filter.value = type
    }
    // Ubah query pencarian
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    // Dapatkan daftar sesuai filter + pencarian
    fun getFilteredTodos(): List<Todo> {
        val query = _searchQuery.value.lowercase()
        val filtered = when (_filter.value) {
            FilterType.ALL -> _todos.value
            FilterType.ACTIVE -> _todos.value.filter { !it.isDone }
            FilterType.DONE -> _todos.value.filter { it.isDone }
        }
        return if (query.isBlank()) filtered
        else filtered.filter { it.title.lowercase().contains(query) }
    }

    // Hitung jumlah aktif & selesai
    fun getActiveCount(): Int = _todos.value.count { !it.isDone }
    fun getDoneCount(): Int = _todos.value.count { it.isDone }
}
