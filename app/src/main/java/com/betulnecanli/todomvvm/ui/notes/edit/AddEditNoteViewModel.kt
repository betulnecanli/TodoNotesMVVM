package com.betulnecanli.todomvvm.ui.notes.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.betulnecanli.todomvvm.ADD_RESULT_OK
import com.betulnecanli.todomvvm.EDIT_RESULT_OK
import com.betulnecanli.todomvvm.data.NoteDao
import com.betulnecanli.todomvvm.data.Notes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val dao: NoteDao,
    private val state : SavedStateHandle
): ViewModel() {

    private val addEditNoteEventChannel = Channel<AddEditNoteEvent>()
    val addEditNoteEvent = addEditNoteEventChannel.receiveAsFlow()

    val note = state.get<Notes>("Note")
    var noteTitle = state.get<String>("noteTitle") ?: note?.title ?: ""
        set(value){
            field = value
            state.set("noteTitle",value)
        }

    var noteContent = state.get<String>("noteContent") ?: note?.content ?: ""
        set(value){
            field = value
            state.set("noteContent", value)
        }

    fun onSaveClick(){

        if(noteTitle.isBlank()){
            ShowInvalidInputMessage("Title cannot be empty")
            return
        }
        if(note != null){
        val updatedNote = note.copy(noteTitle,noteContent)
            updateNote(updatedNote)
        }
        else{
            val newNote = Notes(noteTitle, noteContent)
            createNote(newNote)
        }
    }


    fun ShowInvalidInputMessage(text :String) = viewModelScope.launch {
        addEditNoteEventChannel.send(AddEditNoteEvent.ShowInvalidInputMessage(text))

    }

    fun updateNote(note: Notes) = viewModelScope.launch {
            dao.updateNote(note)
            addEditNoteEventChannel.send(AddEditNoteEvent.NavigateWithResult(EDIT_RESULT_OK))
    }

    fun createNote(note: Notes) = viewModelScope.launch {
            dao.insertNote(note)
            addEditNoteEventChannel.send(AddEditNoteEvent.NavigateWithResult(ADD_RESULT_OK))
    }



    sealed class AddEditNoteEvent{
        data class ShowInvalidInputMessage(val msg : String): AddEditNoteEvent()
        data class NavigateWithResult(val result: Int) : AddEditNoteEvent()
    }


}