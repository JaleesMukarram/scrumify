package com.openlearning.scrumify.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openlearning.scrumify.models.Project
import com.openlearning.scrumify.models.ProjectUser
import com.openlearning.scrumify.models.ROLES
import com.openlearning.scrumify.models.User
import com.openlearning.scrumify.repo.ProjectRepo
import com.openlearning.scrumify.repo.UserRepo
import com.openlearning.scrumify.sealed.State
import kotlinx.coroutines.launch
import java.util.*

class ProjectDetailsVM : ViewModel() {

    val allUsers: MutableLiveData<List<User>> = MutableLiveData()
    private val TAG = "ProjectDetailsTAG"

    // Data

    // State
    val newUserAddedState: MutableLiveData<State> = MutableLiveData(State.Idle)
    val projectState: MutableLiveData<Project> = MutableLiveData()

    // Repo
    private val userRepo = UserRepo
    private val projectRepo = ProjectRepo

    // Members

    fun getAllUserFromRepo() {

        viewModelScope.launch {
            val users = userRepo.getAllUsers()
            val myId = userRepo.currentDBUser.uid
            allUsers.value = users?.filter {
                it.uid != myId
            }
        }
    }

    fun getFilteredUsers(queryString: String): List<User>? {

        if (allUsers.value == null || allUsers.value!!.isEmpty()) {
            return null
        }

        val addedUserIdsArray: List<String> = getAlreadyAddedUserArray()

        Log.d(TAG, "getFilteredUsers: already array $addedUserIdsArray")

        return allUsers.value?.filter {
            (it.uid !in addedUserIdsArray && it.name.toLowerCase(Locale.getDefault())
                .contains(queryString.toLowerCase(Locale.getDefault())))
        }

    }

    private fun getAlreadyAddedUserArray(): List<String> {

        val array = arrayListOf<String>()

        if (projectState.value != null) {

            for (pUser in projectState.value!!.projectUsers) {
                array.add(pUser.userId)
            }
        }
        return array
    }

    private fun checkIfAlreadyHaveScrumMaster(): Boolean {

        for (pUser in projectState.value!!.projectUsers) {
            if (pUser.role == ROLES.SCRUM_MASTER) {
                return true
            }
        }
        return false
    }

    fun refreshProject() {

        viewModelScope.launch {
            val project = projectRepo.getThisProject(projectState.value!!.id)
            if (project != null) {
                projectState.value = project
            }
        }
    }

    fun getUserOfThisId(uid: String): User? {
        if (allUsers.value == null) {
            return null
        }

        for (user in allUsers.value!!) {
            if (user.uid == uid) {
                return user
            }
        }
        return null
    }


    fun onAddUserToProject(newUser: User, roleIndex: Int) {

        if (roleIndex == 0 && checkIfAlreadyHaveScrumMaster()) {

            newUserAddedState.value = State.Failure("Project Already has Scrum Master")

            return
        }

        newUserAddedState.value = State.Loading("Adding New User")

        viewModelScope.launch {

            val projectUser = ProjectUser(
                newUser.uid, when (roleIndex) {
                    0 -> {
                        ROLES.SCRUM_MASTER
                    }
                    else -> {
                        ROLES.TEAM_MEMBER
                    }
                }
            )
            projectRepo.addUserToProject(projectState.value!!.id, projectUser, newUserAddedState)
        }
    }

    fun onUpdateUserRole(oldUser: User, roleIndex: Int) {

        if (roleIndex == 0 && checkIfAlreadyHaveScrumMaster()) {

            newUserAddedState.value = State.Failure("Project Already has Scrum Master")

            return
        }

        newUserAddedState.value = State.Loading("Updating New User")

        val projectUser = ProjectUser(
            oldUser.uid, when (roleIndex) {
                0 -> {
                    ROLES.SCRUM_MASTER
                }
                else -> {
                    ROLES.TEAM_MEMBER
                }
            }
        )
        viewModelScope.launch {

            projectRepo.updateUserInProject(
                projectState.value!!.id,
                getOldProjectUser(oldUser.uid)!!,
                projectUser,
                newUserAddedState
            )
        }
    }

    fun deleteRoleOfThisUser(oldUser: User) {

        val projectUser = getOldProjectUser(oldUser.uid)!!

        Log.d(TAG, "deleteRoleOfThisUser: deleting $projectUser")

        viewModelScope.launch {

            projectRepo.removeUserFromProject(
                projectState.value!!.id,
                projectUser,
                newUserAddedState
            )
        }
    }

    private fun getOldProjectUser(uid: String): ProjectUser? {

        for (pUser in projectState.value!!.projectUsers!!) {

            if (pUser.userId == uid) {
                return pUser
            }
        }

        return null
    }


}