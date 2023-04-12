package com.example.todolist.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.todolist.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupNavigation()
    }

    private fun setupNavigation() {
        val appBarConfiguration = AppBarConfiguration
            .Builder(
                R.id.todoListFragment,
                R.id.addTodoFragment,
                R.id.editTodoFragment
            )
            .build()
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)

        setupActionBarWithNavController(navController, appBarConfiguration)
    }


}