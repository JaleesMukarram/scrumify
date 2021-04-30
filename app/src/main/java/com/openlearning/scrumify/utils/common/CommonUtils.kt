package com.openlearning.scrumify.utils.common

import android.app.Activity
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import com.openlearning.scrumify.dialogues.LoadingDialogue
import java.util.*


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

