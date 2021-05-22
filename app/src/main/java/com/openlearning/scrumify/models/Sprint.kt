package com.openlearning.scrumify.models

import com.openlearning.scrumify.utils.common.getRandomID
import java.util.*

data class Sprint(
    val id: String = getRandomID(),
    val name: String = "",
    val startDate: Date = Date(),
    val endDate: Date = Date(),
)

data class SprintTask(val taskId: String, val deadline: Date, val projectUser: ProjectUser)