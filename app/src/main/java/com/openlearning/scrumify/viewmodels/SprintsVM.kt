package com.openlearning.scrumify.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openlearning.scrumify.models.Project
import com.openlearning.scrumify.models.Sprint
import com.openlearning.scrumify.models.SprintTask
import com.openlearning.scrumify.models.Task
import com.openlearning.scrumify.repo.SprintRepo
import com.openlearning.scrumify.repo.TaskRepo
import com.openlearning.scrumify.sealed.State
import kotlinx.coroutines.launch

class SprintsVM : ViewModel() {

    private val TAG = "SprintsVMTAG"

    lateinit var project: Project

    // Fields
    val allSprints: MutableLiveData<List<Sprint>> = MutableLiveData()

    // State
    val sprintUploadProgress: MutableLiveData<State> = MutableLiveData(State.Idle)
    val sprintTaskUploadProgress: MutableLiveData<State> = MutableLiveData(State.Idle)

    val enableSprintSelection: MutableLiveData<Boolean> = MutableLiveData(false)
    val sprintSelected: MutableLiveData<Sprint> = MutableLiveData()

    // Repo
    private val sprintRepo = SprintRepo
    private val taskRepo = TaskRepo

    fun addNewSprint(sprint: Sprint) {

        viewModelScope.launch {

            sprintRepo.addNewSprint(project.id, sprint, sprintUploadProgress)

        }
    }

    fun getAllSprints() {

        viewModelScope.launch {

            val sprints = sprintRepo.getProjectSprints(project.id)
            getAllSprintTasks(sprints)

        }
    }

    fun getAllSprintTasks(sprints: List<Sprint>?) {

        if (sprints == null) {
            allSprints.value = sprints
            return
        }

        viewModelScope.launch {

            for (sprint in sprints) {

                sprint.sprintTasks = sprintRepo.getSprintTask(project.id, sprint)

                if (sprint.sprintTasks != null && sprint.sprintTasks!!.isNotEmpty()) {
                    for (sprintTask in sprint.sprintTasks!!) {
                        sprintTask.task = taskRepo.getTaskAtReference(sprintTask.taskReference)
                        Log.d(TAG, "getAllSprintTasks: task ${sprintTask.task}")
                    }
                }
            }
        }

        allSprints.value = sprints
    }

    fun addSprintTask(sprintTasK: SprintTask) {

        viewModelScope.launch {

            sprintRepo.addSprintTask(project.id, sprintTasK, sprintTaskUploadProgress)

        }
    }


}