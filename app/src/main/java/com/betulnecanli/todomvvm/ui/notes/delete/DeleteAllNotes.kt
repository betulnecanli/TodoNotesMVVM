package com.betulnecanli.todomvvm.ui.notes.delete

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeleteAllNotes :DialogFragment() {

    private val viewModel : DeleteAllNotesViewModel by viewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setTitle("Confirm deletion")
            .setMessage("Do you really want to delete all notes?")
            .setNegativeButton("Cancel", null)
            .setPositiveButton("Yes"){_,_ ->
                viewModel.confirmClick()
            }.create()

}