package com.betulnecanli.todomvvm.ui.todo.tasks

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.betulnecanli.todomvvm.R
import com.betulnecanli.todomvvm.data.SortOrder
import com.betulnecanli.todomvvm.data.Task
import com.betulnecanli.todomvvm.databinding.FragmentTasksBinding
import com.betulnecanli.todomvvm.util.exhaustive
import com.betulnecanli.todomvvm.util.onQueryTextChanged
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TasksFragment : Fragment(R.layout.fragment_tasks), TaskAdapter.OnItemClickListener {

    private val viewModel : TasksViewModel by viewModels()
    private lateinit var searhView: SearchView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentTasksBinding.bind(view)
        val taskAdapter = TaskAdapter(this)


        binding.apply {
            //recyclerview setup
            recyc.apply {
                adapter = taskAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }


            floatAddButton.setOnClickListener{
                viewModel.onAddNewTaskClick()
            }


            ItemTouchHelper(object: ItemTouchHelper.SimpleCallback(
                0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val task = taskAdapter.currentList[viewHolder.adapterPosition]
                    viewModel.onTaskSwiped(task)
                }

            }).attachToRecyclerView(recyc)
        }

        viewModel.tasks.observe(viewLifecycleOwner){
            taskAdapter.submitList(it)
        }

        setFragmentResultListener("add_edit_request"){ _,bundle->
            val result = bundle.getInt("add_edit_result")
            viewModel.onAddEditResult(result)

        }


        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.taskEvent.collect{event ->
                when(event){
                    is TasksViewModel.TasksEvent.ShowUndoDeleteTaskMessage ->
                    {
                        Snackbar.make(requireView(), "Task deleted", Snackbar.LENGTH_SHORT)
                            .setAction("UNDO"){
                                viewModel.onUndoDeleteClick(event.task)
                            }.show()
                    }
                    is TasksViewModel.TasksEvent.NavigateToAddScreen -> {

                        val action = TasksFragmentDirections.actionTasksFragmentToAddEditTaskFragment("New Task",null )
                        findNavController().navigate(action)
                    }
                    is TasksViewModel.TasksEvent.NavigateToEditTaskScreen -> {
                        val action = TasksFragmentDirections.actionTasksFragmentToAddEditTaskFragment("Edit Task", event.task)
                        findNavController().navigate(action)

                    }
                    is TasksViewModel.TasksEvent.ShowTaskSavedConfirmationMessage -> {
                        Snackbar.make(requireView(),event.msg,Snackbar.LENGTH_SHORT).show()
                    }
                    is TasksViewModel.TasksEvent.NavigateToDeleteAllCompletedTasksScreen -> {
                        val action = TasksFragmentDirections.actionGlobalDeleteAllCompletedTasksDialogFragment()
                        findNavController().navigate(action)
                    }
                }.exhaustive
            }
        }



        setHasOptionsMenu(true)

    }

    override fun onItemClick(task: Task) {
        viewModel.onTaskSelected(task)
    }

    override fun onChecBoxClick(task: Task, isChecked: Boolean) {
        viewModel.onTaskCheckedChanged(task, isChecked)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment_tasks, menu)

        val searchItem = menu.findItem(R.id.action_search)
         searhView = searchItem.actionView as SearchView

        searhView.onQueryTextChanged {
            viewModel.searchQuery.value = it
        }

        val pendingQuery = viewModel.searchQuery.value
        if(pendingQuery != null && pendingQuery.isNotEmpty()){
            searchItem.expandActionView()
            searhView.setQuery(pendingQuery, false)
        }

        viewLifecycleOwner.lifecycleScope.launch{
            menu.findItem(R.id.action_hide_cpmpleted_items).isChecked =
                viewModel.preferencesFlow.first().hideCompleted
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        searhView.setOnQueryTextListener(null)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_sort_byname -> {
                viewModel.onSortOrderSelected(SortOrder.BY_NAME)

                true
            }

            R.id.action_sort_bydatecreated -> {
                viewModel.onSortOrderSelected(SortOrder.BY_DATE)

                true
            }

            R.id.action_hide_cpmpleted_items -> {
                item.isChecked = !item.isChecked
                viewModel.onHideCompletedClick(item.isChecked)
                true
            }

            R.id.action_delete_all_comp_tasks -> {
                    viewModel.deleteAllCompletedTasksClick()


                true
            }

            else -> super.onOptionsItemSelected(item)


        }
        }



}


