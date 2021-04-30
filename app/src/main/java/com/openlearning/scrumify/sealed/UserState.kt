package com.openlearning.scrumify.sealed


sealed class UserState {

    object NoUserSignedIn : UserState()
    data class UserSignedIn(val value: Any?) : UserState()
    data class Error(val value: Any?) : UserState()

}