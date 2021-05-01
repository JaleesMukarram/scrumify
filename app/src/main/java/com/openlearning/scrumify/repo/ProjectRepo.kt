package com.openlearning.scrumify.repo

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.openlearning.scrumify.models.Project
import com.openlearning.scrumify.models.ProjectUser
import com.openlearning.scrumify.sealed.State
import kotlinx.coroutines.tasks.await
import java.lang.Exception

object ProjectRepo {

    private const val TAG = "ProjectRepo"

    private val db = Firebase.firestore.collection(PROJECT_COLLECTION)

    // d621e4b0-5be


    suspend fun addProject(
            project: Project,
            progressState: MutableLiveData<State>,
    ) {

        try {
            db.document(project.id).set(project).await()
            progressState.value = State.Success("Project Added Successfully")
            Log.d(TAG, "addProject: project added")


        } catch (ex: Exception) {

            progressState.value = State.Failure("Failed to add project ${ex.localizedMessage}")
            db.document(project.id).delete()
            Log.d(TAG, "addProject: failed")
        }
    }

    suspend fun addUserToProject(projectId: String, projectUser: ProjectUser, progressState: MutableLiveData<State>) {

        try {

            db.document(projectId).update("projectUsers", FieldValue.arrayUnion(projectUser)).await()

            Log.d(TAG, "addUserToProject: role added")

        } catch (ex: Exception) {

            progressState.value = State.Failure("Failed to add project ${ex.localizedMessage}")
            Log.d(TAG, "addProject: failed")
        }


    }

    suspend fun removeUserFromProject(projectId: String, projectUser: ProjectUser, progressState: MutableLiveData<State>) {

        try {

            db.document(projectId).update("projectUsers", FieldValue.arrayRemove(projectUser)).await()

            Log.d(TAG, "removeUserFromProject: user removed added")

        } catch (ex: Exception) {

            progressState.value = State.Failure("Failed to add project ${ex.localizedMessage}")
            Log.d(TAG, "addProject: failed")
        }


    }

    suspend fun updateUserInProject(projectId: String, projectUserOld: ProjectUser, projectUserNew: ProjectUser, progressState: MutableLiveData<State>) {

        try {

            db.document(projectId).update("projectUsers", FieldValue.arrayRemove(projectUserOld)).await()
            Log.d(TAG, "removeUserFromProject: user removed added")
            db.document(projectId).update("projectUsers", FieldValue.arrayUnion(projectUserNew)).await()
            Log.d(TAG, "addUserToProject: role added")


        } catch (ex: Exception) {

            progressState.value = State.Failure("Failed to add project ${ex.localizedMessage}")
            Log.d(TAG, "addProject: failed")
        }


    }


}