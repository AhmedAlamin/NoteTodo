package com.ahmedalamin.notetodo.ui.tasks

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.ahmedalamin.notetodo.data.PreferencesManager
import com.ahmedalamin.notetodo.data.SortOrder
import com.ahmedalamin.notetodo.data.Task
import com.ahmedalamin.notetodo.data.TaskDao
import com.ahmedalamin.notetodo.ui.ADD_TASK_RESULT_OK
import com.ahmedalamin.notetodo.ui.EDIT_TASK_RESULT_OK
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
class TasksViewModel @ViewModelInject constructor (
    private val taskDao: TaskDao,
    private val preferencesManager: PreferencesManager,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    val searchQuery = state.getLiveData("searchQuery","")

    val preferencesFlow = preferencesManager.preferencesFlow

    private val taskEventChannel = Channel<TasksEvent>()
    val tasksEvent = taskEventChannel.receiveAsFlow()


    private val tasksFlow = combine(
        searchQuery.asFlow(),
        preferencesFlow
    ){query, filterPreferences ->
        Pair(query,filterPreferences)
    }.flatMapLatest { (query,filterPreferences) ->
        taskDao.getTasks(query,filterPreferences.sortOrder,filterPreferences.hideCompleted)
    }
    val tasks = tasksFlow.asLiveData()


    fun onSortOrderSelected(sortOrder: SortOrder) = viewModelScope.launch {
        preferencesManager.updatedSortOrder(sortOrder)
    }

    fun onHideCompletedClick(hideCompleted:Boolean) = viewModelScope.launch {
        preferencesManager.updatedHideCompleted(hideCompleted)
    }


    fun onTaskSelected(task:Task) = viewModelScope.launch {
        taskEventChannel.send(TasksEvent.NavigateToEditTaskScreen(task))
    }

    fun onTaskCheckedChanged(task:Task,isChecked:Boolean) = viewModelScope.launch {
        taskDao.update(task.copy(completed = isChecked))
    }

    fun onTaskSwiped(task: Task) = viewModelScope.launch {
        taskDao.delete(task)
        taskEventChannel.send(TasksEvent.ShowUndoDeleteTaskMessage(task))

    }

    fun onUndoDeleteClick(task: Task) = viewModelScope.launch {
        taskDao.insert(task)
    }

    fun onAddNewTaskClick() = viewModelScope.launch {
        taskEventChannel.send(TasksEvent.NavigateToAddTaskScreen)
    }

    fun onAddEditResult(result: Int){
        when (result){
            ADD_TASK_RESULT_OK -> showTaskSavedConfirmationMessage("Task Added")
            EDIT_TASK_RESULT_OK -> showTaskSavedConfirmationMessage("Task Updated")
        }
    }

    fun showTaskSavedConfirmationMessage(text: String) = viewModelScope.launch {
        taskEventChannel.send(TasksEvent.ShowTAskSavedConfirmationMessage(text))
    }

    fun onDeleteAllCompletedClick() = viewModelScope.launch {
            taskEventChannel.send(TasksEvent.NavigateToDeleteAllCompletedScreen)
    }



    sealed class TasksEvent{
        object NavigateToAddTaskScreen : TasksEvent()
        data class NavigateToEditTaskScreen(val task: Task) : TasksEvent()
        data class ShowUndoDeleteTaskMessage(val task:Task) : TasksEvent()
        data class ShowTAskSavedConfirmationMessage(val msg: String) : TasksEvent()
        object NavigateToDeleteAllCompletedScreen : TasksEvent()
    }

}



