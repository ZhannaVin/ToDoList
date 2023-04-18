package com.example.todolist.ui.edittodo



import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.BroadcastReceiver
import android.content.ContentValues
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todolist.R
import com.example.todolist.databinding.EditFragmentBinding
import com.example.todolist.databinding.TodoListFragmentBinding
import com.example.todolist.receiver.AlarmReceiver
import com.example.todolist.room_todo.Todo
import com.example.todolist.ui.todolist.TodoListViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class EditTodoFragment:  Fragment() {
    private lateinit var todo: Todo
    private lateinit var binding: EditFragmentBinding
    private lateinit var viewmodel: EditTodoViewModel
    private lateinit var alarmReceiver: AlarmReceiver
    private val args by navArgs<EditTodoFragmentArgs>()
    private var initialValue = ContentValues()
    private var dateAndTimeFormat = SimpleDateFormat("hh:mm, dd MMM YYYY", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = EditFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewmodel = ViewModelProvider(this).get(EditTodoViewModel::class.java)
        //getting id from todolistfragment
        val currentId = args.todoId


        viewLifecycleOwner.lifecycleScope.launch {
            todo = viewmodel.getSingleById(currentId).await()
            //to set earlier entered data to fields ti see it in ui
            binding.updateTitle.setText(todo.todo)
            binding.updateDescription.setText(todo.desc)
            binding.updateDueDate
        }




        alarmReceiver = AlarmReceiver()



        binding.updateButton.setOnClickListener {

            val title = binding.updateTitle.text.toString()
            val descr = binding.updateDescription.text.toString()
            val create = System.currentTimeMillis()
            val update = System.currentTimeMillis()
            val dueDate = initialValue.get("update_dueDate") as Long?

            if (title != "" && descr != "" && dueDate != null) {
                val update = todo.copy(
                    todo = title,
                    desc = descr,
                    createDate = create as Long,
                    updateDate = update,
                    dueDate = dueDate as Long
                )
                viewmodel.updateTodo(update)

                if (binding.checkRemindUpdate.isChecked) context?.let { it1 ->
                    alarmReceiver.setReminder(
                        it1,
                        dueDate - 3600 * 1000,
                        title
                    )
                }
                else context?.let { it1 -> alarmReceiver.setReminder(it1, dueDate, title) }

                findNavController().navigate(R.id.todoListFragment)

            } else Toast.makeText(context, "Please Enter Data Correctly!", Toast.LENGTH_SHORT)
                .show()
        }

        binding.btnUpdateDate.setOnClickListener {pickDateTime()}

        }




        private fun pickDateTime() {
            val dueDateTime = Calendar.getInstance()
            val startYear = dueDateTime.get(Calendar.YEAR)
            val startMonth = dueDateTime.get(Calendar.MONTH)
            val startDay = dueDateTime.get(Calendar.DAY_OF_MONTH)
            val startHour = dueDateTime.get(Calendar.HOUR_OF_DAY)
            val startMinute = dueDateTime.get(Calendar.MINUTE)

            DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { _, year, month, day ->
                TimePickerDialog(requireContext(), TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                    val selectedDateTime = Calendar.getInstance()
                    selectedDateTime.set(year, month, day, hour, minute)
                    initialValue.put("update_dueDate", selectedDateTime.timeInMillis)
                    val showDateTime = dateAndTimeFormat.format(selectedDateTime.time)
                    binding.updateDueDate.setText(showDateTime)
                }, startHour, startMinute, false).show()
            }, startYear, startMonth, startDay).show()
        }
    }
