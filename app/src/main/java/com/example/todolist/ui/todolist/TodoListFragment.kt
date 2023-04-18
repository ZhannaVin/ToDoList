package com.example.todolist.ui.todolist

import androidx.fragment.app.Fragment
import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.databinding.TodoListFragmentBinding


import com.example.todolist.room_todo.Todo
import com.example.todolist.ui.TodoAdapter



class TodoListFragment : Fragment () {

    private lateinit var binding: TodoListFragmentBinding
    private lateinit var rvAdapter: TodoAdapter
    private lateinit var todoListViewModel: TodoListViewModel
    private lateinit var todoRecyclerView: RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TodoListFragmentBinding.inflate(inflater, container, false)
        todoListViewModel = ViewModelProvider(this).get(TodoListViewModel::class.java)
        rvAdapter = TodoAdapter({todo: Todo -> handleDeleteClick(todo)}, { todo: Todo -> handleTodoClick(todo) }, todoListViewModel)
        todoRecyclerView = binding.todoRecyclerView
        todoRecyclerView.layoutManager = LinearLayoutManager(context)

        todoRecyclerView.adapter = rvAdapter
        todoRecyclerView.setHasFixedSize(true)
        return binding.root
    }

    private fun handleDeleteClick(todo: Todo) {
        todoListViewModel.deleteTodo(todo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }


    private fun handleTodoClick(todo: Todo) {
        val navController = requireActivity().findNavController(R.id.fragment_container)
        val action = todo.id?.let {
            TodoListFragmentDirections.actionTodoListFragmentToEditTodoFragment(
                it
            )
        }
        action?.let { findNavController().navigate(it) }
        }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        val serarchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView =
            menu.findItem(R.id.search)?.actionView as androidx.appcompat.widget.SearchView
        searchView.setSearchableInfo(serarchManager.getSearchableInfo(requireActivity().componentName))
        searchView.maxWidth = Integer.MAX_VALUE
        searchView.queryHint = "Search ToDo List"
        searchView.imeOptions = EditorInfo.IME_ACTION_DONE
        searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                rvAdapter.getFilter().filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                rvAdapter.getFilter().filter(newText)
                return false
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        binding.fabAddtodo.setOnClickListener {
            val action = TodoListFragmentDirections.actionTodoListFragmentToAddTodoFragment()
            findNavController().navigate(action)
        }
    }

    override fun onResume() {
        super.onResume()
        observeData()
    }

    private fun observeData() {
        todoListViewModel.getTodo()?.observe(viewLifecycleOwner, Observer { todo ->
            todo?.let { render(todo) }
        })
    }

    private fun render(todoList: List<Todo>) {
        rvAdapter.setTodo(todoList)
        if (todoList.isEmpty()) {
            todoRecyclerView.visibility = View.GONE
            binding.emptyListMessage.visibility = View.VISIBLE
        } else {
            todoRecyclerView.visibility= View.VISIBLE
            binding.emptyListMessage.visibility = View.GONE
        }
    }

}


