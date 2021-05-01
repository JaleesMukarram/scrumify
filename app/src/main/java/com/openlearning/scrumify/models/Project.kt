package com.openlearning.scrumify.models

import com.google.firebase.firestore.ServerTimestamp
import com.openlearning.scrumify.utils.common.getRandomID
import java.util.*

data class Project(
        val id: String = getRandomID(),
        val name: String = "",
        val description: String = "",
        val status: ProjectStatus = ProjectStatus.PENDING,
        val projectUsers: List<ProjectUser> = arrayListOf(),
        @ServerTimestamp
        val creationDate: Date? = null,
        val startDate: Date? = null,
        val endDate: Date? = null
)

data class ProjectUser(
        val userId: String,
        val role: ROLES
)


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