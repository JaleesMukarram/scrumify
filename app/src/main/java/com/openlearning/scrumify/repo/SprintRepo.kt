package com.openlearning.scrumify.repo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.openlearning.scrumify.models.Sprint
import com.openlearning.scrumify.models.Task
import com.openlearning.scrumify.sealed.State
import kotlinx.coroutines.tasks.await

object SprintRepo : ViewModel() {

    suspend fun addNewSprint(
        projectId: String,
        sprint: Sprint,
        progressState: MutableLiveData<State>
    ) {

        try {

            Firebase.firestore.collection(PROJECT_COLLECTION)
                .document(projectId)
                .collection(SPRINT_COLLECTION)
                .document(sprint.id)
                .set(sprint)
                .await()

            progressState.value = State.Success("Added")

        } catch (ex: Exception) {

            progressState.value = State.Failure(ex)
        }
    }

    suspend fun getProjectSprints(projectId: String): List<Sprint>? {

        try {

            val qss = Firebase.firestore.collection(PROJECT_COLLECTION)
                .document(projectId)
                .collection(SPRINT_COLLECTION)
                .get()
                .await()

            if (qss.isEmpty) {
                return null
            }

            return qss.toObjects(Sprint::class.java)

        } catch (ex: java.lang.Exception) {
            return null
        }
    }


}