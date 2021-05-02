package com.openlearning.scrumify.utils.common

import android.app.Activity
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.openlearning.scrumify.dialogues.LoadingDialogue
import com.openlearning.scrumify.models.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


fun showLoading(activity: Activity, message: String, description: String): LoadingDialogue {

    val dialogue = LoadingDialogue(activity, message, description)
    dialogue.show()
    return dialogue
}
//
//fun showWorking(activity: Activity, message: String, description: String): WorkingDialogue {
//
//    val dialogue = WorkingDialogue(activity, message, description)
//    dialogue.show()
//    return dialogue
//}

fun getRandomID(char: Int = 12): String {
    return UUID.randomUUID().toString().substring(0, char)
}

fun startIntent(oldActivity: Activity, intent: Intent) {
    oldActivity.startActivity(intent)
}


fun changeActivity(oldActivity: Activity, newActivity: Class<*>, finishOld: Boolean) {
    val intent = Intent(oldActivity, newActivity)
    startIntent(oldActivity, intent)
    if (finishOld) oldActivity.finish()
}

fun changeActivity(oldActivity: Activity, intent: Intent?, finishOld: Boolean) {
    startIntent(oldActivity, intent!!)
    if (finishOld) oldActivity.finish()
}


//fun getTaskStatus(dsValue: String?): TaskStatus {
//
//
//    return when (dsValue) {
//        "COMPLETE" -> {
//            TaskStatus.COMPLETE
//        }
//        "PENDING" -> {
//            TaskStatus.PENDING
//        }
//        "LOCKED" -> {
//            TaskStatus.LOCKED
//        }
//        else -> TaskStatus.PENDING
//    }
//
//}


open class TextWatcherImpl : TextWatcher {

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun afterTextChanged(s: Editable?) {
    }

}

fun getDateString(date: Date): String {
    return SimpleDateFormat.getDateInstance().format(date)
}

fun getProjectStatusActiveArray(): ArrayList<String> {

    val array = ArrayList<String>()
    array.add(ProjectStatus.STARTED.name)
    array.add(ProjectStatus.PENDING.name)

    return array
}

fun getRolesArray(): ArrayList<String> {

    val array = ArrayList<String>()
    array.add(ROLES.SCRUM_MASTER.name.replace("_", " "))
    array.add(ROLES.TEAM_MEMBER.name.replace("_", " "))

    return array
}

fun getProjectStatusFullArray(): ArrayList<String> {

    val array = ArrayList<String>()
    array.add(ProjectStatus.STARTED.name)
    array.add(ProjectStatus.ENDED.name)
    array.add(ProjectStatus.PENDING.name)
    array.add(ProjectStatus.CANCELED.name)

    return array
}

fun getMyContainsArray(myId: String): ArrayList<ProjectUser> {

    val array = ArrayList<ProjectUser>()

    array.add(ProjectUser(myId, ROLES.ADMINISTRATOR))
    array.add(ProjectUser(myId, ROLES.SCRUM_MASTER))
    array.add(ProjectUser(myId, ROLES.TEAM_MEMBER))

    return array
}

fun getMyRole(project: Project, myId: String): ROLES? {

    for (pUser in project.projectUsers) {
        if (pUser.userId == myId) {
            return pUser.role
        }
    }
    return null
}

fun View.hideKeyboard() {
    val inputMethodManager = context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}

