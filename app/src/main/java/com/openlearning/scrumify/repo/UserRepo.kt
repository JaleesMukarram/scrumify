package com.openlearning.scrumify.repo

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.auth.FirebaseAuthCredentialsProvider
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.openlearning.scrumify.models.Project
import com.openlearning.scrumify.models.User
import com.openlearning.scrumify.sealed.State
import com.openlearning.scrumify.sealed.UserState
import com.openlearning.scrumify.utils.common.getMyContainsArray
import kotlinx.coroutines.tasks.await

object UserRepo {

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: CollectionReference = Firebase.firestore.collection(USER_COLLECTION)

     val auth:FirebaseAuth =  FirebaseAuth.getInstance()

    lateinit var currentDBUser: User

    suspend fun createNewUser(
        user: User,
        password: String,
        progressState: MutableLiveData<State>
    ) {

        try {

            mAuth.createUserWithEmailAndPassword(user.email, password).await()

            val newUser = user.copy(uid = mAuth.currentUser!!.uid)

            progressState.value = State.Loading("Saving Info Please Wait!")

            db.document(newUser.uid)
                .set(newUser).await()

            progressState.value = State.Success("Account Created Successfully")


        } catch (ex: Exception) {
            progressState.value = State.Failure(ex)
            mAuth.currentUser?.delete()
        }
    }

    suspend fun signInUser(
        user: User,
        password: String,
        progressState: MutableLiveData<State>
    ) {

        try {

            mAuth.signInWithEmailAndPassword(user.email, password)
                .await()

            progressState.value = State.Success("Sign In Successful")

        } catch (ex: Exception) {

            if (ex is FirebaseAuthInvalidUserException || ex is FirebaseAuthInvalidCredentialsException) {

                progressState.value = State.Failure("Email or password incorrect")
                return
            }

            progressState.value = State.Failure("Failed to Sign In  ${ex.localizedMessage}")
        }


    }

    suspend fun getLoginStatus(): UserState {

        if (mAuth.currentUser == null) {
            return UserState.NoUserSignedIn
        }

        try {

            val id = mAuth.currentUser!!.uid
            val dbResult = db.document(id)
                .get().await()

            if (dbResult.exists()) {

                val user = dbResult.toObject(User::class.java)
                currentDBUser = user!!
                return UserState.UserSignedIn(user)

            } else {

                return UserState.Error("Something went wrong. Contact support for more info")

            }
        } catch (ex: Exception) {

            return UserState.Error(ex)
        }

    }

    fun signOut() {
        mAuth.signOut()

    }

    suspend fun getAllUsers(): List<User>? {

        try {

            val qss = db.get().await()

            if (qss.isEmpty) {
                return null
            }

            return qss.toObjects(User::class.java)

        } catch (ex: java.lang.Exception) {
            return null
        }
    }
}