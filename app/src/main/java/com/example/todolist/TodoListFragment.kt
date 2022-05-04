package com.example.todolist

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.databinding.FragmentTodoListBinding
import com.example.todolist.model.ToDoModel
import com.example.todolist.uiparts.TodoListAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class TodoListFragment : Fragment() {
    private lateinit var binding: FragmentTodoListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_todo_list,
            container,
            false
        )
        binding.floatingActionButton.setOnClickListener { view: View ->
            // Todo作成画面に遷移する
            view.findNavController()
                .navigate(R.id.action_todoListFragment_to_todoRegistrationActivity)
            //TODO todoRegistrationFragmentの作成
//                .navigate(R.id.action_todoListFragment_to_todoRegistrationFragment)


        }


        binding.topAppBar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.trash -> {
                    // Todoを全件削除する
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle("全件削除しますか？")
                        .setPositiveButton(R.string.deleteButton) { _, _ ->
                            ToDoModel().allDelete(requireContext()) {
                                MaterialAlertDialogBuilder(requireContext())
                                    .setTitle("削除しました")
                                    .setPositiveButton(R.string.closeButton) { _, _ -> onResume() }
                                    .show()
                            }

                        }
                        .setNegativeButton(R.string.cancelButton) { _, _ -> }
                        .show()
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
        }

        return binding.root
    }


    override fun onResume() {
        super.onResume()

        var todoModel = ToDoModel().findAll(requireContext())
        val adapter = TodoListAdapter(todoModel) { todo -> onClick(todo) }
        val layoutManager = LinearLayoutManager(requireContext())
        // アダプターとレイアウトマネージャーをセット
        binding.todoRecyclerView.layoutManager = layoutManager
        binding.todoRecyclerView.adapter = adapter
        binding.todoRecyclerView.setHasFixedSize(true)
    }

    /**
     * 選択したTodoの詳細に遷移する
     */
    private fun onClick(todo: ToDoModel) {
//        val intent = Intent(this, TodoDetailActivity::class.java)
//        intent.putExtra("todo", todo.createTime)
//        startActivity(intent)
    }

}