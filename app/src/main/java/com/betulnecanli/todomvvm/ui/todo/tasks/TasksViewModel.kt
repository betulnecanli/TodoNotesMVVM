package com.betulnecanli.todomvvm.ui.todo.tasks

import androidx.lifecycle.*
import com.betulnecanli.todomvvm.ADD_RESULT_OK
import com.betulnecanli.todomvvm.EDIT_RESULT_OK
import com.betulnecanli.todomvvm.data.PreferencesManager
import com.betulnecanli.todomvvm.data.SortOrder
import com.betulnecanli.todomvvm.data.Task
import com.betulnecanli.todomvvm.data.TaskDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val taskDao: TaskDao,
    private val preferecesManager : PreferencesManager,
     private val state : SavedStateHandle
    ) : ViewModel() {

      val searchQuery = state.getLiveData("searchQuery","")

      val preferencesFlow = preferecesManager.preferencesFlow

      private val taskEventChannel  = Channel<TasksEvent>()
      val taskEvent = taskEventChannel.receiveAsFlow()

      private val tasksFlow = combine(
            searchQuery.asFlow(),
            preferencesFlow

        ){
            query, filterPreferences ->
            Pair(query,filterPreferences)
        }.flatMapLatest {(query, filterPreferences) ->
                taskDao.getTasks(query, filterPreferences.sortOrder, filterPreferences.hideCompleted)
        }

    fun onSortOrderSelected(sortOrder: SortOrder) = viewModelScope.launch {

        preferecesManager.updateSortOrder(sortOrder)
    }

    fun onHideCompletedClick(hideCompleted: Boolean) = viewModelScope.launch {
        preferecesManager.updateHideCompleted(hideCompleted)
    }

        val tasks = tasksFlow.asLiveData()


    fun onTaskSelected(task: Task) = viewModelScope.launch {
        taskEventChannel.send(TasksEvent.NavigateToEditTaskScreen(task))
    }

    fun onTaskCheckedChanged(task : Task, isChecked : Boolean) = viewModelScope.launch {
        taskDao.update(task.copy(completed = isChecked))
    }

    fun onTaskSwiped(task :Task) = viewModelScope.launch {
        taskDao.delete(task)
        taskEventChannel.send(TasksEvent.ShowUndoDeleteTaskMessage(task))
    }
    fun onUndoDeleteClick(task : Task) = viewModelScope.launch {
        taskDao.insert(task)
    }

    fun onAddNewTaskClick() = viewModelScope.launch {

        taskEventChannel.send(TasksEvent.NavigateToAddScreen)
    }

    fun onAddEditResult(result : Int){
        when(result){
            ADD_RESULT_OK -> showTaskSavedConfirmationMessage("Task Added")
                EDIT_RESULT_OK -> showTaskSavedConfirmationMessage("Task updated")
        }
    }

    fun showTaskSavedConfirmationMessage(text: String) = viewModelScope.launch {
        taskEventChannel.send(TasksEvent.ShowTaskSavedConfirmationMessage(text))
    }

    fun deleteAllCompletedTasksClick() = viewModelScope.launch {
        taskEventChannel.send(TasksEvent.NavigateToDeleteAllCompletedTasksScreen)
    }

    sealed class TasksEvent{

        object NavigateToAddScreen: TasksEvent()
        data class NavigateToEditTaskScreen(val task : Task) : TasksEvent()
        data class ShowUndoDeleteTaskMessage(val task : Task): TasksEvent()
        data class ShowTaskSavedConfirmationMessage(val msg : String): TasksEvent()
        object NavigateToDeleteAllCompletedTasksScreen: TasksEvent()
    }


    }
