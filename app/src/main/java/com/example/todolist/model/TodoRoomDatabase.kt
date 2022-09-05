package com.example.todolist.model

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ToDoModel::class], version = 1, exportSchema = false)
abstract class TodoRoomDatabase : RoomDatabase() {
    abstract fun todoDao(): ToDoModelDao
}