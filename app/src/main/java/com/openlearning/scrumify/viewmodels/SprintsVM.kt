package com.openlearning.scrumify.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openlearning.scrumify.models.*
import com.openlearning.scrumify.repo.SprintRepo
import com.openlearning.scrumify.repo.TaskRepo
import com.openlearning.scrumify.repo.UserRepo
import com.openlearning.scrumify.sealed.State
import kotlinx.coroutines.launch

class SprintsVM : ViewModel() {

    private val TAG = "SprintsVMTAG"

    lateinit var project: Project

    // Fields
    val allSprints: MutableLiveData<List<Sprint>> = MutableLiveData()
    val allProjectUsers: MutableLiveData<List<ProjectUserData>> = MutableLiveData()


    // State
    val sprintUploadProgress: MutableLiveData<State> = MutableLiveData(State.Idle)
    val sprintTaskUploadProgress: MutableLiveData<State> = MutableLiveData(State.Idle)

    val refreshAdapters: MutableLiveData<Boolean> = MutableLiveData()

    val enableSprintSelection: MutableLiveData<Boolean> = MutableLiveData(false)
    val sprintSelected: MutableLiveData<Sprint> = MutableLiveData()

    // Repo
    private val sprintRepo = SprintRepo
    private val taskRepo = TaskRepo
    private val userRepo = UserRepo

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
            allSprints.value = sprints
        }
    }

    fun addSprintTask(sprintTasK: SprintTask) {

        viewModelScope.launch {

            sprintRepo.addSprintTask(project.id, sprintTasK, sprintTaskUploadProgress)

        }
    }

    fun updateSprintTask(sprintTasK: SprintTask) {

        viewModelScope.launch {

            sprintRepo.updateSprintTask(project.id, sprintTasK, sprintTaskUploadProgress)

        }
    }

    fun deleteSprintTask(sprintTasK: SprintTask) {

        viewModelScope.launch {

            sprintRepo.deleteSprintTask(project.id, sprintTasK, sprintTaskUploadProgress)

        }
    }

    fun getAllUserFromRepo() {

        viewModelScope.launch {

            val projectUsersIds = getProjectUserIds()

            val projectDBUsers = userRepo.getAllUsers()!!.filter {
                it.uid in projectUsersIds
            }

            val userData: MutableList<ProjectUserData> = arrayListOf()

            for (pUser in project.projectUsers) {

                val user = getUserOfThisId(projectDBUsers, pUser.userId)
                if (user != null) {
                    userData.add(ProjectUserData(user, pUser.role))
                }
            }

            allProjectUsers.value = userData

        }
    }

    private fun getProjectUserIds(): List<String> {

        val list: MutableList<String> = arrayListOf()

        for (projectUser in project.projectUsers) {
            list.add(projectUser.userId)
        }
        return list
    }

    private fun getUserOfThisId(allUsers: List<User>, uid: String): User? {

        for (user in allUsers) {
            if (user.uid == uid) {
                return user
            }
        }
        return null
    }

}