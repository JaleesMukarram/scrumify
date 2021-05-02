package com.openlearning.scrumify.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openlearning.scrumify.models.Project
import com.openlearning.scrumify.models.ProjectStatus
import com.openlearning.scrumify.models.ProjectUser
import com.openlearning.scrumify.models.ROLES
import com.openlearning.scrumify.repo.ProjectRepo
import com.openlearning.scrumify.repo.UserRepo
import com.openlearning.scrumify.sealed.State
import com.openlearning.scrumify.utils.InputValidator
import com.openlearning.scrumify.utils.InputValidatorBuilder
import com.openlearning.scrumify.utils.MaxLengthValidator
import com.openlearning.scrumify.utils.MinLengthValidator
import com.openlearning.scrumify.utils.common.getProjectStatusActiveArray
import com.openlearning.scrumify.utils.common.getProjectStatusFullArray
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class AddNewProjectVM : ViewModel() {
    private val TAG = "AddNewProjectTAG"

    // Data
    val projectStatusArray: ArrayList<String> = getProjectStatusActiveArray()


    // Fields
    val projectName: MutableLiveData<String> = MutableLiveData("")
    val projectStatusIndex: MutableLiveData<Int> = MutableLiveData(0)
    val projectStartDate: MutableLiveData<Date> = MutableLiveData()
    val projectEndDate: MutableLiveData<Date> = MutableLiveData()
    val groupValid: MutableLiveData<MutableList<Boolean>> =
        MutableLiveData(arrayListOf(false, true, false, false))

    // State
    val projectAddedState: MutableLiveData<State> = MutableLiveData(State.Idle)


    // Repo
    private val projectRepo = ProjectRepo


    fun addNewProject() {

        projectAddedState.value = State.Idle

        val name = projectName.value!!
        val status = projectStatusArray[projectStatusIndex.value!!]
        val startDate = projectStartDate.value!!
        val endDate = projectEndDate.value!!

        val project = Project(
            name = name,
            status = ProjectStatus.valueOf(status),
            startDate = startDate,
            endDate = endDate,
            projectUsers = arrayListOf(ProjectUser(UserRepo.currentDBUser.uid, ROLES.ADMINISTRATOR))
        )

        projectAddedState.value = State.Loading("Creating Project $name")

        viewModelScope.launch {
            projectRepo.addProject(project, projectAddedState)


        }
    }

    // Group ID
    fun onStartDateAdded() {
        groupValid.value!![2] = true
        groupValid.value = groupValid.value!!
    }

    fun onEndDateAdded() {
        groupValid.value!![3] = true
        groupValid.value = groupValid.value!!
    }

    fun onEndDateRemoved() {
        groupValid.value!![3] = false
        groupValid.value = groupValid.value!!
    }

    // Validators
    fun getMinMaxValidator(): InputValidator {

        return InputValidatorBuilder()
            .addValidationScheme(MinLengthValidator(4, false))
            .addValidationScheme(MaxLengthValidator(24, true))
            .build()
    }


}