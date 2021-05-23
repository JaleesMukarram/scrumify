package com.openlearning.scrumify.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openlearning.scrumify.repo.UserRepo
import com.openlearning.scrumify.sealed.UserState
import kotlinx.coroutines.launch

class SplashVM : ViewModel() {

    // States
    val userState: MutableLiveData<UserState> = MutableLiveData()


    // Repo

    private val userRepo = UserRepo

    fun getUserStateFromServer() {

        viewModelScope.launch {

            userState.value = userRepo.getLoginStatus()

        }
    }


}