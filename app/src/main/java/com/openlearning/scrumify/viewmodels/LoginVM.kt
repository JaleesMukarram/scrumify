package com.openlearning.scrumify.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openlearning.scrumify.models.User
import com.openlearning.scrumify.repo.UserRepo
import com.openlearning.scrumify.sealed.State
import com.openlearning.scrumify.utils.*
import kotlinx.coroutines.launch

class LoginVM : ViewModel() {

    // Fields
    val loginEmail: MutableLiveData<String> = MutableLiveData("")
    val loginPassword: MutableLiveData<String> = MutableLiveData("")
    val groupValid: MutableLiveData<MutableList<Boolean>> = MutableLiveData(arrayListOf(false, false))

    // States
    val userAuthState: MutableLiveData<State> = MutableLiveData(State.Idle)

    // Repo

    private val userRepo = UserRepo

    // Members
    var user = User()

    fun startLogin() {

        userAuthState.value = State.Idle

        val email = loginEmail.value!!
        val password = loginPassword.value!!

        user = User(email = email)

        signInAccount(password)

    }

    private fun signInAccount(password: String) {

        userAuthState.value = State.Loading("Please wait while we verify your credentials!!")
        viewModelScope.launch {

            userRepo.signInUser(user, password, userAuthState)
        }
    }

    // Validators
    fun getEmailValidator(): InputValidator {

        return InputValidatorBuilder()
                .addValidationScheme(EmailValidator(false))
                .build()
    }

    fun getPasswordValidator(): InputValidator {

        return InputValidatorBuilder()
                .addValidationScheme(MinLengthValidator(8, false))
                .addValidationScheme(MaxLengthValidator(24, true))
                .build()
    }
}