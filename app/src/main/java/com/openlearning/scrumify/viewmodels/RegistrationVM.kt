package com.openlearning.scrumify.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openlearning.scrumify.models.User
import com.openlearning.scrumify.repo.UserRepo
import com.openlearning.scrumify.sealed.State
import com.openlearning.scrumify.utils.*
import kotlinx.coroutines.launch

class RegistrationVM : ViewModel() {

    // Fields
    val registrationName: MutableLiveData<String> = MutableLiveData("")
    val registrationEmail: MutableLiveData<String> = MutableLiveData("")
    val registrationPhone: MutableLiveData<String> = MutableLiveData("")
    val registrationPassword: MutableLiveData<String> = MutableLiveData("")
    val groupValid: MutableLiveData<MutableList<Boolean>> = MutableLiveData(arrayListOf(false, false, false, false))


    // States

    val userAuthState: MutableLiveData<State> = MutableLiveData(State.Idle)

    // Repo
    private val userRepo = UserRepo

    // Members
    var user = User()

    fun startRegistration() {

        userAuthState.value = State.Idle

        val name = registrationName.value!!
        val email = registrationEmail.value!!
        val phone = registrationPhone.value!!
        val password = registrationPassword.value!!

        user = User(
                name = name,
                email = email,
                phone = phone
        )

        createNewAccount(password)

    }

    private fun createNewAccount(password: String) {

        userAuthState.value = State.Loading("Creating New Account Please Wait!!")
        viewModelScope.launch {

            userRepo.createNewUser(user, password, userAuthState)

        }
    }

    // Validators
    fun getMinMaxValidator(): InputValidator {

        return InputValidatorBuilder()
                .addValidationScheme(MinLengthValidator(4, false))
                .addValidationScheme(MaxLengthValidator(24, true))
                .build()
    }

    fun getEmailValidator(): InputValidator {

        return InputValidatorBuilder()
                .addValidationScheme(EmailValidator(false))
                .build()
    }

    fun getPhoneValidator(): InputValidator {

        return InputValidatorBuilder()
                .addValidationScheme(MinLengthValidator(13, false))
                .build()
    }

    fun getPasswordValidator(): InputValidator {

        return InputValidatorBuilder()
                .addValidationScheme(MinLengthValidator(8, false))
                .addValidationScheme(MaxLengthValidator(24, true))
                .build()
    }
}

