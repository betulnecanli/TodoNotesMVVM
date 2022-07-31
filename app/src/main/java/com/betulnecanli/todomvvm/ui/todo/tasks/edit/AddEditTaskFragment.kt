package com.betulnecanli.todomvvm.ui.todo.tasks.edit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.betulnecanli.todomvvm.R
import com.betulnecanli.todomvvm.databinding.FragmentAddEditTaskBinding
import com.betulnecanli.todomvvm.util.exhaustive
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddEditTaskFragment : Fragment(R.layout.fragment_add_edit_task) {

    private val viewModel : AddEditTaskViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentAddEditTaskBinding.bind(view)
        binding.apply {
            taskEditText.setText(viewModel.taskName)
            editTaskCheck.isChecked = viewModel.taskImportance
            editTaskCheck.jumpDrawablesToCurrentState()
            dateCreatedTextView.isVisible = viewModel.task != null
            dateCreatedTextView.text = "Created: ${viewModel.task?.createdDateFormatted}"

            taskEditText.addTextChangedListener{
                viewModel.taskName = it.toString()
            }

            editTaskCheck.setOnCheckedChangeListener{_, isChecked ->
                viewModel.taskImportance = isChecked
            }

            floatingActionButtonEditTask.setOnClickListener{
                viewModel.onSaveClick()
            }

        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.addEditTaskEvent.collect{event ->
                when(event){
                    is AddEditTaskViewModel.AddEditTaskEvent.ShowInvalidInputMessage -> {
                            Snackbar.make(requireView(),event.msg,Snackbar.LENGTH_LONG).show()
                    }


                    is AddEditTaskViewModel.AddEditTaskEvent.NavigateBackWithResult -> {
                            binding.taskEditText.clearFocus()
                        setFragmentResult("add_edit_request",
                            bundleOf("add_edit_result" to event.result)
                                )
                        findNavController().popBackStack()
                    }
                }.exhaustive
            }
        }

    }


}