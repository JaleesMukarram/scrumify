package com.openlearning.scrumify.repo

import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.openlearning.scrumify.models.Task
import com.openlearning.scrumify.sealed.State
import kotlinx.coroutines.tasks.await
import java.lang.Exception

object TaskRepo {


    suspend fun addNewTask(
        projectId: String,
        task: Task,
        progressState: MutableLiveData<State>
    ) {

        try {

            Firebase.firestore.collection(PROJECT_COLLECTION)
                .document(projectId)
                .collection(TASK_COLLECTION)
                .document(task.id)
                .set(task)
                .await()

            progressState.value = State.Success("Added")

        } catch (ex: Exception) {

            progressState.value = State.Failure(ex)
        }
    }

    suspend fun updateThisTask(
        projectId: String,
        task: Task,
        progressState: MutableLiveData<State>
    ) {

        try {

            Firebase.firestore.collection(PROJECT_COLLECTION)
                .document(projectId)
                .collection(TASK_COLLECTION)
                .document(task.id)
                .set(task, SetOptions.merge())
                .await()

            progressState.value = State.Success("Updated")

        } catch (ex: Exception) {

            progressState.value = State.Failure(ex)
        }
    }

    suspend fun deleteThisTask(
        projectId: String,
        taskId: String,
        progressState: MutableLiveData<State>
    ) {

        try {

            Firebase.firestore.collection(PROJECT_COLLECTION)
                .document(projectId)
                .collection(TASK_COLLECTION)
                .document(taskId)
                .delete()
                .await()

            progressState.value = State.Success("Deleted")

        } catch (ex: Exception) {

            progressState.value = State.Failure(ex)
        }
    }

    suspend fun getProjectTasks(projectId: String): List<Task>? {

        try {

            val qss = Firebase.firestore.collection(PROJECT_COLLECTION)
                .document(projectId)
                .collection(TASK_COLLECTION)
                .get()
                .await()

            if (qss.isEmpty) {
                return null
            }

            return qss.toObjects(Task::class.java)

        } catch (ex: Exception) {
            return null
        }
    }

}