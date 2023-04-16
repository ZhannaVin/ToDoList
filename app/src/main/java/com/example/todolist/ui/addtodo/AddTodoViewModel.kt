package com.example.todolist.ui.addtodo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.todolist.repository.TodoRepository
import com.example.todolist.room_todo.Todo
import kotlinx.coroutines.launch

class AddTodoViewModel(application: Application): AndroidViewModel(application) {

    private val todoRepository = TodoRepository(application)

    fun insertTodo(todo: Todo) {
        viewModelScope.launch {
            try {
                todoRepository.insert(todo)
            } catch (e: Exception) {
            }
        }
    }
}
