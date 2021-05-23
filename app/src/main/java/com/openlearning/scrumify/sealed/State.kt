package com.openlearning.scrumify.sealed


sealed class State {

    data class Success(val value: Any?) : State()
    data class Failure(val value: Any?) : State()
    data class Loading(val value: Any?) : State()
    data class Progress(val progress: Float, val total: Float, val value: Any?) : State()
    object Idle : State()

}
