package com.openlearning.scrumify.repo

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.openlearning.scrumify.models.Sprint
import com.openlearning.scrumify.models.SprintTask
import com.openlearning.scrumify.sealed.State
import kotlinx.coroutines.tasks.await

object SprintRepo : ViewModel() {

    private const val TAG = "SprintRepoTAG"

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

    suspend fun getProjectSprints(
        projectId: String
    ): List<Sprint>? {

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

    suspend fun addSprintTask(
        projectId: String,
        sprintTask: SprintTask,
        progressState: MutableLiveData<State>
    ) {

        try {

            Firebase.firestore.collection(PROJECT_COLLECTION)
                .document(projectId)
                .collection(SPRINT_COLLECTION)
                .document(sprintTask.sprintId)
                .collection(SPRINT_TASK_COLLECTION)
                .document(sprintTask.id)
                .set(sprintTask)
                .await()

            progressState.value = State.Success("Added")

        } catch (ex: Exception) {

            progressState.value = State.Failure(ex)
        }
    }

    suspend fun updateSprintTask(
        projectId: String,
        sprintTask: SprintTask,
        progressState: MutableLiveData<State>
    ) {

        try {

            Firebase.firestore.collection(PROJECT_COLLECTION)
                .document(projectId)
                .collection(SPRINT_COLLECTION)
                .document(sprintTask.sprintId)
                .collection(SPRINT_TASK_COLLECTION)
                .document(sprintTask.id)
                .set(sprintTask, SetOptions.merge())
                .await()

            Log.d(TAG, "updateSprintTask: done")
            progressState.value = State.Success("Done")

        } catch (ex: Exception) {
            Log.e(TAG, "updateSprintTask: error", ex)
            progressState.value = State.Failure(ex)
        }
    }

    suspend fun deleteSprintTask(
        projectId: String,
        sprintTask: SprintTask,
        progressState: MutableLiveData<State>
    ) {

        try {

            Firebase.firestore.collection(PROJECT_COLLECTION)
                .document(projectId)
                .collection(SPRINT_COLLECTION)
                .document(sprintTask.sprintId)
                .collection(SPRINT_TASK_COLLECTION)
                .document(sprintTask.id)
                .delete()
                .await()

            Log.d(TAG, "deleteSprintTask: done")
            progressState.value = State.Success("Done")

        } catch (ex: Exception) {
            Log.e(TAG, "deleteSprintTask: error", ex)
            progressState.value = State.Failure(ex)
        }
    }

    suspend fun getSprintTask(
        projectId: String,
        sprint: Sprint
    ): List<SprintTask>? {

        try {

            val qss = Firebase.firestore.collection(PROJECT_COLLECTION)
                .document(projectId)
                .collection(SPRINT_COLLECTION)
                .document(sprint.id)
                .collection(SPRINT_TASK_COLLECTION)
                .get()
                .await()

            if (qss.isEmpty) {
                return null
            }

            return qss.toObjects(SprintTask::class.java)

        } catch (ex: java.lang.Exception) {
            return null
        }

    }

}