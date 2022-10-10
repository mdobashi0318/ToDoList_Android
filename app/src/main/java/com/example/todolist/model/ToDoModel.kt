package com.example.todolist.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "todo_table")
data class ToDoModel(
    @PrimaryKey val createTime: String,
    @ColumnInfo var toDoName: String,
    @ColumnInfo var todoDate: String,
    @ColumnInfo var todoTime: String,
    @ColumnInfo var toDoDetail: String,
    @ColumnInfo var completionFlag: String,
)