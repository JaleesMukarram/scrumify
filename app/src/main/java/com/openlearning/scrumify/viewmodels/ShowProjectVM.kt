package com.openlearning.scrumify.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openlearning.scrumify.models.Project
import com.openlearning.scrumify.repo.ProjectRepo
import com.openlearning.scrumify.repo.UserRepo
import kotlinx.coroutines.launch

class ShowProjectVM : ViewModel() {

    val myAllProjects: MutableLiveData<List<Project>> = MutableLiveData()

    // Repo
    private val projectRepo = ProjectRepo

    fun fetchMyProjects() {

        viewModelScope.launch {

            val uid = UserRepo.currentDBUser.uid
            val myProject = projectRepo.getMyProjects(uid)
            myAllProjects.value = myProject

        }

    }

    fun getMyUid(): String {
        return UserRepo.currentDBUser.uid
    }


}