package com.example.todolist.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.todolist.room_todo.AppDatabase
import com.example.todolist.room_todo.Todo
import com.example.todolist.room_todo.TodoDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class TodoRepository(application: Application) {
    private val todoDao: TodoDao
    private var todoList: LiveData<List<Todo>>?
    private var sortDueDate: LiveData<List<Todo>>


    init {
        val db = AppDatabase.getDatabase((application.applicationContext))
        todoDao = db!!.todoDao()
        todoList = todoDao.loadAllTodo()
        sortDueDate = todoDao.sortDue()
    }

    fun getTodoList():LiveData<List<Todo>>?{
        return todoList
    }

    fun getSortedByDueDate():LiveData<List<Todo>>?{
        return sortDueDate
    }
    suspend fun getSingleTodo(id: Int): Todo{
        return todoDao.loadSingle(id)
    }
    suspend fun insert(todo: Todo)  {
        withContext(Dispatchers.IO){
            todoDao.insertTodo(todo)
        }
    }
    suspend fun delete(todo: Todo) {
            withContext(Dispatchers.IO) {
                todoDao.deleteTodo(todo)
            }
        }

    suspend fun update(todo: Todo) {
        withContext(Dispatchers.IO) {
            todoDao.updateTodo(todo)
        }
    }

}