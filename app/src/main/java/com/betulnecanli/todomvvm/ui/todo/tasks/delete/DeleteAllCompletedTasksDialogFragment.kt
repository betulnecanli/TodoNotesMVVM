package com.betulnecanli.todomvvm.ui.todo.tasks.delete

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeleteAllCompletedTasksDialogFragment: DialogFragment() {

    private val viewModel: DeleteAllCompletedViewModel by viewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
       AlertDialog.Builder(requireContext())
           .setTitle("Confirm deletion")
           .setMessage("Do you really want to delete all completed tasks?")
           .setNegativeButton("Cancel",null)
           .setPositiveButton("Yes"){_,_ ->
                    viewModel.confirmClick()
           }.create()



}