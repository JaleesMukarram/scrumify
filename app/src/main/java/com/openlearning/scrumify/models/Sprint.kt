package com.openlearning.scrumify.models

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.openlearning.scrumify.utils.common.getRandomID
import java.util.*

data class Sprint(
    val id: String = getRandomID(),
    val name: String = "",
    val startDate: Date = Date(),
    val endDate: Date = Date(),

    @Exclude
    var sprintTasks: List<SprintTask>? = null
)

data class SprintTask(
    val id: String = getRandomID(),
    val taskReference: DocumentReference = Firebase.firestore.collection("a").document("a"),
    val sprintId: String = "",
    var deadline: Date? = null,
    val assignedUsers: MutableList<String> = arrayListOf(),
    var taskStatus: TaskStatus = TaskStatus.PENDING,
    val taskIssues: MutableList<TaskIssue> = arrayListOf(),
    @Exclude
    var task: Task? = null
)

data class TaskIssue(
    val issue: String = "",
    val description: String = "",
    val priority: TaskPriority = TaskPriority.NORMAL
)

enum class TaskStatus {
    COMPLETE,
    PENDING,
    CLOSED
}