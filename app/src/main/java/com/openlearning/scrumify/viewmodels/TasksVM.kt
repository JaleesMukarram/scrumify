package com.openlearning.scrumify.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openlearning.scrumify.models.Project
import com.openlearning.scrumify.models.Task
import com.openlearning.scrumify.repo.TaskRepo
import com.openlearning.scrumify.sealed.State
import kotlinx.coroutines.launch

class TasksVM : ViewModel() {


    lateinit var project: Project

    // Fields
    val allTasks: MutableLiveData<List<Task>> = MutableLiveData()

    // State
    val taskUploadProgress: MutableLiveData<State> = MutableLiveData(State.Idle)

    // Repo
    private val taskRepo = TaskRepo


    fun uploadTask(task: Task) {

        viewModelScope.launch {

            taskRepo.addNewTask(project.id, task, taskUploadProgress)

        }
    }

    fun updateTask(task: Task) {

        viewModelScope.launch {

            taskRepo.updateThisTask(project.id, task, taskUploadProgress)

        }
    }

    fun deleteTask(taskId: String) {

        viewModelScope.launch {

            taskRepo.deleteThisTask(project.id, taskId, taskUploadProgress)

        }
    }


    fun getAllTasks() {

        viewModelScope.launch {

            val tasks = taskRepo.getProjectTasks(project.id)
            allTasks.value = tasks

        }
    }
}