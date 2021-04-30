package com.openlearning.scrumify.models

import android.os.Parcelable
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class User(
        val uid: String = "",
        val name: String = "",
        val email: String = "",
        val phone: String = "",
        @ServerTimestamp
        val data: Date? = null
) : Parcelable