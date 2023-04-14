package com.example.todolist.ui.todolist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.todolist.repository.TodoRepository
import com.example.todolist.room_todo.Todo
import kotlinx.coroutines.launch




class TodoListViewModel(application: Application): AndroidViewModel(application)  {

    private val todoRepository = TodoRepository(application)
    fun getTodo(): LiveData<List<Todo>>?{
        return todoRepository.getTodoList()
    }
    fun deleteTodo(todo: Todo){
        viewModelScope.launch {
        todoRepository.delete(todo)
    }

}}
