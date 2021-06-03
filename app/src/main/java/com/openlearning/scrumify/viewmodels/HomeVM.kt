package com.openlearning.scrumify.viewmodels

import androidx.lifecycle.ViewModel
import com.openlearning.scrumify.repo.UserRepo

class HomeVM : ViewModel() {

    private val userRepo = UserRepo


    fun signOut() {

        userRepo.auth.signOut()

    }

}