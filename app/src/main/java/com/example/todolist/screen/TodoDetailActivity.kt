package com.example.todolist.screen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import com.example.todolist.R
import kotlinx.android.synthetic.main.activity_todo_detail.*

class TodoDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_detail)

        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        val args = Bundle()

        args.putString("createTime", intent.getStringExtra("createTime"))
        val fragment = TodoDetailFragment()
        fragment.arguments = args
        transaction.add(R.id.container, fragment)
        transaction.commit()
    }



}