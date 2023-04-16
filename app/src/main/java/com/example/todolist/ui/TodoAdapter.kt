package com.example.todolist.ui

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.room_todo.Todo
import com.example.todolist.ui.todolist.TodoListViewModel

import java.text.SimpleDateFormat
import java.util.*



class TodoAdapter(private val clickListener: (todo: Todo) -> Unit, private val viewModel: TodoListViewModel): RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {


    private var dateFormat = SimpleDateFormat("hh:mm, dd MMM YYYY", Locale.getDefault())
    private var todoList = listOf<Todo>()
    private var todoFilteredList: List<Todo> = arrayListOf()

    init {
        todoFilteredList = todoList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val itemContainer = LayoutInflater.from(parent.context)
            .inflate(R.layout.todo_item, parent, false)
        return TodoViewHolder(itemContainer)
    }

    override fun getItemCount() = todoFilteredList.size

    private fun converter(dueDate: Long?): Date? {
        return dueDate?.let { Date(it) }
    }



    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {

        val current = todoFilteredList[position]
        val dueDate = dateFormat.format(converter(current.dueDate))
        holder.tvTodo.text = current.todo
        holder.tvDesc.text = current.desc
        holder.tvDueDate.text = dueDate
        holder.itemView.setOnClickListener(){
            clickListener(todoFilteredList[position])
        }
    }

    internal fun setTodo(todo: List<Todo>){
        this.todoList = todo
        this.todoFilteredList = todo
        notifyDataSetChanged()
    }


    fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraints: CharSequence?): FilterResults {
                val charSearch = constraints.toString().toLowerCase(Locale.ROOT).trim()
                todoFilteredList = if (charSearch.isEmpty()){
                    todoList
                } else {
                    val resultList = arrayListOf<Todo>()
                    for (item in todoList) {
                        if (item.todo?.toLowerCase(Locale.ROOT)?.contains(charSearch)!!) {
                            resultList.add(item)
                        }
                    }
                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = todoFilteredList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraints: CharSequence?, results: FilterResults) {
                todoFilteredList = results.values as ArrayList<Todo>
                notifyDataSetChanged()
            }
        }
    }

    fun getTodoAt(position: Int): Todo{
        return todoList[position]
    }


    inner class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTodo: TextView = itemView.findViewById(R.id.todo_name_text_view)
        val tvDesc: TextView = itemView.findViewById(R.id.todo_description)
        val tvDueDate: TextView = itemView.findViewById(R.id.dueDate_text_view)
        val deleteIcon: ImageView = itemView.findViewById(R.id.delete_icon)

        fun bind(todo: Todo){
            deleteIcon.setOnClickListener{
                viewModel.deleteTodo(todo)
            }
        }

    }
}
