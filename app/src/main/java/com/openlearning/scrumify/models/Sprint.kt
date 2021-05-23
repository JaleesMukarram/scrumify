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
    val deadline: Date? = null,
    val assignedUsers: List<ProjectUser> = arrayListOf(),
    val taskStatus: TaskStatus = TaskStatus.PENDING,
    @Exclude
    var task: Task? = null
)

enum class TaskStatus {
    COMPLETE,
    PENDING,
    CLOSED
}