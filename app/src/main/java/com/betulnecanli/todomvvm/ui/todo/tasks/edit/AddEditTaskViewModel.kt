package com.betulnecanli.todomvvm.ui.todo.tasks.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.betulnecanli.todomvvm.ADD_RESULT_OK
import com.betulnecanli.todomvvm.EDIT_RESULT_OK
import com.betulnecanli.todomvvm.data.Task
import com.betulnecanli.todomvvm.data.TaskDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditTaskViewModel @Inject constructor(
    private val dao: TaskDao,
     private val  state : SavedStateHandle
    ): ViewModel() {

        private val addEditTaskEventChannel = Channel<AddEditTaskEvent>()
        val addEditTaskEvent = addEditTaskEventChannel.receiveAsFlow()

        val task = state.get<Task>("task")
        var taskName = state.get<String>("taskName") ?: task?.name ?: ""
            set(value){
                field = value
                state.set("taskName", value)
            }

        var taskImportance = state.get<Boolean>("taskImportance") ?: task?.important ?: false
            set(value){
                field = value
                state.set("taskImportance", value)
            }


    fun onSaveClick(){
        if(taskName.isBlank()){
            showInvalidInputMessage("Name cannot be empty")
            return
        }
        if(task != null){
            val updatedTask = task.copy(taskName,taskImportance)
            updateTask(updatedTask)
        }
        else{
            val newTask = Task(taskName,taskImportance)
            createTask(newTask)
        }
    }


    fun showInvalidInputMessage(text : String) = viewModelScope.launch {
        addEditTaskEventChannel.send(AddEditTaskEvent.ShowInvalidInputMessage(text))
    }
    fun updateTask(task : Task) = viewModelScope.launch {
        dao.update(task)
        addEditTaskEventChannel.send(AddEditTaskEvent.NavigateBackWithResult(EDIT_RESULT_OK))
    }
    fun createTask(task : Task) = viewModelScope.launch {
        dao.insert(task)
        addEditTaskEventChannel.send(AddEditTaskEvent.NavigateBackWithResult(ADD_RESULT_OK))
    }

    sealed class AddEditTaskEvent{
        data class ShowInvalidInputMessage(val msg :String): AddEditTaskEvent()
        data class NavigateBackWithResult (val result: Int): AddEditTaskEvent()
    }

}