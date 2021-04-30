package com.openlearning.scrumify.repo

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.openlearning.scrumify.models.User
import com.openlearning.scrumify.sealed.State
import kotlinx.coroutines.tasks.await

object UserRepo {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: CollectionReference = Firebase.firestore.collection(USER_COLLECTION)

    suspend fun createNewUser(user: User,
                              password: String,
                              progressState: MutableLiveData<State>) {

        try {

            auth.createUserWithEmailAndPassword(user.email, password).await()

            val newUser = user.copy(uid = auth.currentUser!!.uid)

            progressState.value = State.Loading("Saving Info Please Wait!")

            db.document(newUser.uid)
                    .set(newUser).await()

            progressState.value = State.Success("Account Created Successfully")


        } catch (ex: Exception) {
            progressState.value = State.Failure(ex)
            auth.currentUser?.delete()
        }
    }
}