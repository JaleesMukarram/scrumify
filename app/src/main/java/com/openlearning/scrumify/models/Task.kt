package com.openlearning.scrumify.models

import com.openlearning.scrumify.utils.common.getRandomID

data class Task(
    val id: String = getRandomID(),
    val name: String = "",
    val priority: TaskPriority = TaskPriority.NORMAL
)

enum class TaskPriority {
    LOW,
    NORMAL,
    URGENT
}