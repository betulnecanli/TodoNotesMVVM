package com.betulnecanli.todomvvm.ui.notes.edit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.betulnecanli.todomvvm.R
import com.betulnecanli.todomvvm.databinding.FragmentAddEditNoteBinding
import com.betulnecanli.todomvvm.databinding.FragmentAddEditTaskBinding
import com.betulnecanli.todomvvm.util.exhaustive
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class AddEditNoteFragment : Fragment(R.layout.fragment_add_edit_note) {

    private val viewModel : AddEditNoteViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentAddEditNoteBinding.bind(view)
        binding.apply {
            noteTitleEditText.setText(viewModel.noteTitle)
            noteContentEditText.setText(viewModel.noteContent)
            currentDateNote.isVisible = viewModel.note != null
            currentDateNote.text = "Created:  ${viewModel.note?.createdDateFormatted}"



            noteTitleEditText.addTextChangedListener{
                viewModel.noteTitle = it.toString()

            }
            noteContentEditText.addTextChangedListener {
                viewModel.noteContent = it.toString()
            }

            noteAddEditFloatBttn.setOnClickListener {
                viewModel.onSaveClick()
            }

        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {

            viewModel.addEditNoteEvent.collect{ event ->
                when(event){
                    is AddEditNoteViewModel.AddEditNoteEvent.ShowInvalidInputMessage -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_SHORT).show()
                    }
                    is AddEditNoteViewModel.AddEditNoteEvent.NavigateWithResult -> {
                        binding.noteTitleEditText.clearFocus()
                        setFragmentResult("note_add_edit_request",
                            bundleOf("note_add_edit_request" to event.result)
                            )
                        findNavController().popBackStack()
                    }
                }.exhaustive


            }

        }

    }



}