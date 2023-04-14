package com.example.todolist.ui.addtodo

import android.app.*
import android.content.ContentValues
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.todolist.R
import com.example.todolist.databinding.AddTodoFragmentBinding
import com.example.todolist.databinding.TodoListFragmentBinding

import java.text.SimpleDateFormat
import java.util.*

class AddTodoFragment : Fragment() {
    lateinit var binding: AddTodoFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AddTodoFragmentBinding.inflate(inflater, container, false)
        return inflater.inflate(R.layout.add_todo_fragment, container, false)
    }

}