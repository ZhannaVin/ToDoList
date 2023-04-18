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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.todolist.R
import com.example.todolist.databinding.AddTodoFragmentBinding
import com.example.todolist.receiver.AlarmReceiver
import com.example.todolist.room_todo.Todo
import com.example.todolist.ui.todolist.TodoListFragmentDirections
import com.example.todolist.ui.todolist.TodoListViewModel

import java.text.SimpleDateFormat
import java.util.*

class AddTodoFragment : Fragment() {
    private lateinit var binding: AddTodoFragmentBinding
    private lateinit var addTodoViewModel: AddTodoViewModel
    private lateinit var alarmReceiver: AlarmReceiver
    private var dateAndTimeFormat = SimpleDateFormat("hh:mm, dd MMM YYYY", Locale.getDefault())
    private var todo: Todo? = null
    private var initialValue = ContentValues()

    override fun onResume() {
        super.onResume()
        activity?.actionBar?.title = "Add task"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AddTodoFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addTodoViewModel = ViewModelProvider(this).get(AddTodoViewModel::class.java)
        alarmReceiver = AlarmReceiver()

        //add_task.button settings

        binding.addTaskButton.setOnClickListener {

            val todoTitle = binding.addTodo.text.toString()
            val todoDescr = binding.addDescription.text.toString()
            // val to store timing for using by other components
            val due_date = initialValue.get("add_dueDate")
            val create_date = System.currentTimeMillis()
            val update_date = System.currentTimeMillis()

            if (todoTitle != "" && todoDescr != "" && due_date != null) {

                val add = Todo(
                    todo = todoTitle,
                    desc = todoDescr,
                    createDate = create_date,
                    updateDate = update_date,
                    dueDate = due_date as Long
                )



                if (binding.checkRemind.isChecked) context?.let { it1 ->
                    alarmReceiver.setReminder(
                        it1,
                        due_date - 3600 * 1000,
                        todoTitle
                    )
                }
                else context?.let { it1 -> alarmReceiver.setReminder(it1, due_date, todoTitle) }

                addTodoViewModel.insertTodo(add)

                findNavController().navigate(R.id.todoListFragment)


        } else Toast.makeText(context, "Please Enter Data Correctly!", Toast.LENGTH_SHORT)
                .show()


        }
        binding.btnDate.setOnClickListener {
            pickDateTime()
        }

    }




    private fun pickDateTime() {
        val dueDateTime = Calendar.getInstance()
        val startYear = dueDateTime.get(Calendar.YEAR)
        val startMonth = dueDateTime.get(Calendar.MONTH)
        val startDay = dueDateTime.get(Calendar.DAY_OF_MONTH)
        val startHour = dueDateTime.get(Calendar.HOUR_OF_DAY)
        val startMinute = dueDateTime.get(Calendar.MINUTE)

        DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { _, year, month, day ->
                TimePickerDialog(
                    requireContext(),
                    TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                        val selectedDateTime = Calendar.getInstance()
                        selectedDateTime.set(year, month, day, hour, minute)
                        initialValue.put("add_dueDate", selectedDateTime.timeInMillis)
                        val showDateTime = dateAndTimeFormat.format(selectedDateTime.time)
                        binding.addDuedate.setText(showDateTime)
                    },
                    startHour,
                    startMinute,
                    false
                ).show()
            },
            startYear,
            startMonth,
            startDay
        ).show()
    }

}