package com.openlearning.scrumify.dialogues

import android.app.Activity
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import com.openlearning.scrumify.R
import com.openlearning.scrumify.databinding.ViewSprintDialogueBinding
import com.openlearning.scrumify.databinding.ViewTaskAssignBinding
import com.openlearning.scrumify.databinding.ViewTaskDialogueBinding
import com.openlearning.scrumify.models.Project
import com.openlearning.scrumify.models.Sprint
import com.openlearning.scrumify.models.Task
import com.openlearning.scrumify.models.TaskPriority
import com.openlearning.scrumify.utils.common.getDateString
import com.openlearning.scrumify.utils.extensions.value
import java.util.*

class TaskAssignDialogue(
    private val activity: Activity,

    ) {
    lateinit var mBinding: ViewTaskAssignBinding

    // Data
    val enumClass = TaskPriority::class.java
    private lateinit var mDialog: AlertDialog

    fun show() {

        initViews()
    }

    private fun initViews() {

        mBinding = DataBindingUtil.inflate(
            LayoutInflater.from(activity),
            R.layout.view_task_assign,
            null,
            false
        )

        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        builder.setView(mBinding.root)

        mDialog = builder.create()
        mDialog.show()

    }

    private fun cancel() {

        mDialog.cancel()
        mDialog.dismiss()

    }

}