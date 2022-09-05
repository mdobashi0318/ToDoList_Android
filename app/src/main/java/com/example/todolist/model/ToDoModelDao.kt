package com.example.todolist.model

import android.content.Context
import androidx.room.*
import com.example.todolist.other.CompletionFlag
import com.example.todolist.other.Notification
import kotlinx.coroutines.flow.Flow

@Dao
interface ToDoModelDao {

    @Query("SELECT * FROM todo_table")
    suspend fun getAll(): List<ToDoModel>

    @Query("SELECT * FROM todo_table WHERE completionFlag == :frag")
    suspend fun getTodos(frag: String): List<ToDoModel>

    @Query("SELECT * FROM todo_table WHERE createTime == :createTime")
    suspend fun getTodo(createTime: String): ToDoModel

    @Insert
    suspend fun add(todo: ToDoModel)

    @Update
    suspend fun update(todo: ToDoModel)

    @Delete
    fun delete(todo: ToDoModel)

    @Query("Delete FROM todo_table")
    fun deleteAll()
}