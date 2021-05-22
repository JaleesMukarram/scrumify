package com.openlearning.scrumify.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openlearning.scrumify.models.Project
import com.openlearning.scrumify.models.Sprint
import com.openlearning.scrumify.repo.SprintRepo
import com.openlearning.scrumify.sealed.State
import kotlinx.coroutines.launch

class SprintsVM : ViewModel() {

    lateinit var project: Project

    // Fields
    val allSprints: MutableLiveData<List<Sprint>> = MutableLiveData()

    // State
    val sprintUploadProgress: MutableLiveData<State> = MutableLiveData(State.Idle)

    // Repo
    val sprintRepo = SprintRepo

    fun addNewSprint(sprint: Sprint) {

        viewModelScope.launch {

            sprintRepo.addNewSprint(project.id, sprint, sprintUploadProgress)

        }
    }

    fun getAllSprints() {

        viewModelScope.launch {

            val sprints = sprintRepo.getProjectSprints(project.id)
            allSprints.value = sprints

        }
    }


}