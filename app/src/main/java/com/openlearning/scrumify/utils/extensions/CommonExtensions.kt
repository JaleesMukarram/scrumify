package com.openlearning.scrumify.utils.extensions

import android.widget.EditText

var EditText.value
    get() = this.text.toString()
    set(value) {
        this.setText(value)
    }

fun MutableList<Boolean>.ifAllTrue(): Boolean {

    for (value in this) {
        if (!value) return false
    }
    return true
}

//inline fun <reified T> Task<T>.await(): T = Tasks.await(this)
