package com.openlearning.scrumify.models

import android.os.Parcelable
import com.google.firebase.firestore.ServerTimestamp
import com.openlearning.scrumify.utils.common.getRandomID
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Project(
    val id: String = getRandomID(),
    val name: String = "",
    val status: ProjectStatus = ProjectStatus.PENDING,
    val projectUsers: List<ProjectUser> = arrayListOf(),
    @ServerTimestamp
    val creationDate: Date? = null,
    val startDate: Date? = null,
    val endDate: Date? = null
) : Parcelable

@Parcelize
data class ProjectUser(
    val userId: String = "",
    val role: ROLES = ROLES.TEAM_MEMBER
) : Parcelable

data class ProjectUserData(val user: User, val role: ROLES)


enum class ProjectStatus {
    STARTED,
    ENDED,
    PENDING,
    CANCELED,
}

enum class ROLES {
    ADMINISTRATOR,
    SCRUM_MASTER,
    TEAM_MEMBER
}