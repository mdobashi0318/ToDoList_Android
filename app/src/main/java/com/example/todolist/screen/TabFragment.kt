package com.example.todolist.screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.todolist.R
import com.example.todolist.databinding.FragmentTabBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.util.*

class TabFragment : Fragment() {
    private lateinit var tabAdapter: TabAdapter
    private lateinit var viewPager: ViewPager2


    private lateinit var binding: FragmentTabBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate<FragmentTabBinding>(
            inflater,
            R.layout.fragment_tab,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        tabAdapter = TabAdapter(this)
        viewPager = view.findViewById(R.id.pager)
        viewPager.adapter = tabAdapter

        val tabLayout = view.findViewById<TabLayout>(R.id.tab_layout)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            if (position == 0) tab.text = "未完了" else tab.text = "完了"
        }.attach()
    }

}


class TabAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        /// TODO TodoListFragment()に完了か未完了のTodoを呼び出すようにする
        return TodoListFragment()
    }
}