package com.example.todolist.ui.edittodo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.todolist.repository.TodoRepository
import com.example.todolist.room_todo.Todo
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class EditTodoViewModel(application: Application): AndroidViewModel(application) {

    private val todoRepository = TodoRepository(application)




    fun getSingleById(id: Int): Deferred<Todo> {
        return viewModelScope.async{
            todoRepository.getSingleTodo(id)
        }
    }

    fun updateTodo(todo: Todo) {
        viewModelScope.launch {
            try {
                todoRepository.update(todo)
            } catch (e: Exception) {
            }
        }
    }
}

