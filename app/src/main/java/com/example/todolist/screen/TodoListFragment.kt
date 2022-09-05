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
import com.example.todolist.model.*
import com.example.todolist.other.CompletionFlag
import com.example.todolist.uiparts.TodoListAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TodoListFragment(private val flag: CompletionFlag) : Fragment() {

    private lateinit var binding: FragmentTodoListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as AppCompatActivity).supportActionBar?.title =
            resources.getString(R.string.app_name)

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_todo_list,
            container,
            false
        )

        binding.emptyTextView.isVisible = true
        binding.todoRecyclerView.isVisible = false

        CoroutineScope(Dispatchers.Main).launch {
            var todoModel = TodoApplication.database.todoDao().getTodos(flag.value)
            val adapter = TodoListAdapter(todoModel) { todo -> onClick(todo) }
            val layoutManager = LinearLayoutManager(requireContext())

            if (todoModel.isNotEmpty()) {
                binding.emptyTextView.isVisible = false
                binding.todoRecyclerView.isVisible = true
                // アダプターとレイアウトマネージャーをセット
                binding.todoRecyclerView.layoutManager = layoutManager
                binding.todoRecyclerView.adapter = adapter
                binding.todoRecyclerView.setHasFixedSize(true)
            }
        }

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

    /**
     * 選択したTodoの詳細に遷移する
     */
    private fun onClick(todo: ToDoModel) {
        /// Todo詳細画面に遷移する
        view?.findNavController()
            ?.navigate(TabFragmentDirections.actionTabFragmentToTodoDetailFragment(todo.createTime))
    }

}