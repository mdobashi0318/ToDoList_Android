package com.example.todolist.screen

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.R
import com.example.todolist.databinding.FragmentTodoListBinding
import com.example.todolist.model.ToDoModel
import com.example.todolist.other.CompletionFlag
import com.example.todolist.uiparts.TodoListAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class TodoListFragment(private val flag: CompletionFlag) : Fragment() {
    private lateinit var binding: FragmentTodoListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title =
            resources.getString(R.string.app_name)

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_todo_list,
            container,
            false
        )
        binding.floatingActionButton.setOnClickListener { view: View ->
            // Todo作成画面に遷移する
            view.findNavController()
                .navigate(
                    TabFragmentDirections.actionTabFragmentToTodoRegistrationFragment(
                        null
                    )
                )
        }

        return binding.root
    }


    override fun onResume() {
        super.onResume()

        var todoModel = ToDoModel().findTodos(requireContext(), flag)
        val adapter = TodoListAdapter(todoModel) { todo -> onClick(todo) }
        val layoutManager = LinearLayoutManager(requireContext())

        if (todoModel.isEmpty()) {
            binding.emptyTextView.isVisible = true
            binding.todoRecyclerView.isVisible = false
            return
        } else {
            binding.emptyTextView.isVisible = false
            binding.todoRecyclerView.isVisible = true
            // アダプターとレイアウトマネージャーをセット
            binding.todoRecyclerView.layoutManager = layoutManager
            binding.todoRecyclerView.adapter = adapter
            binding.todoRecyclerView.setHasFixedSize(true)
        }


    }


    /**
     * 選択したTodoの詳細に遷移する
     */
    private fun onClick(todo: ToDoModel) {
        /// Todo詳細画面に遷移する
        view?.findNavController()
            ?.navigate(TabFragmentDirections.actionTabFragmentToTodoDetailFragment(todo.createTime))
    }

}