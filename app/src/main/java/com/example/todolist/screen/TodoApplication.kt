package com.example.todolist.screen

import android.app.Application
import androidx.room.Room
import com.example.todolist.model.TodoRoomDatabase

class TodoApplication : Application() {
    companion object {
        lateinit var database: TodoRoomDatabase
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            TodoRoomDatabase::class.java,
            "todo_database"
        ).build()
    }
}