package com.example.todolist.model

import android.content.Context
import androidx.room.*
import com.example.todolist.other.CompletionFlag
import com.example.todolist.other.Notification
import kotlinx.coroutines.flow.Flow

@Dao
interface ToDoModelDao {

    @Query("SELECT * FROM todo_table")
    suspend fun getAll(): List<ToDoModel1>

    @Query("SELECT * FROM todo_table WHERE completionFlag == :frag")
    suspend fun getTodos(frag: String): List<ToDoModel1>

    @Query("SELECT * FROM todo_table WHERE createTime == :createTime")
    suspend fun getTodo(createTime: String): ToDoModel1

    @Insert
    suspend fun add(todo: ToDoModel1)

    @Update
    suspend fun update(todo: ToDoModel1)

    @Delete
    fun delete(todo: ToDoModel1)

    @Query("Delete FROM todo_table")
    fun deleteAll()
}