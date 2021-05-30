package com.openlearning.scrumify.dialogues

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import com.openlearning.scrumify.R
import com.openlearning.scrumify.databinding.ViewTaskIssueDialogueBinding
import com.openlearning.scrumify.models.Task
import com.openlearning.scrumify.models.TaskIssue
import com.openlearning.scrumify.models.TaskPriority
import com.openlearning.scrumify.utils.extensions.value

class TaskIssueDialogue(
    private val activity: Activity,
    private val onAddIssue: (TaskIssue) -> Unit
) {
    lateinit var mBinding: ViewTaskIssueDialogueBinding

    // Data
    val enumClass = TaskPriority::class.java


    // Fields
    val issueTitle: MutableLiveData<String> = MutableLiveData("")
    val issueDescription: MutableLiveData<String> = MutableLiveData("")
    val issuePriorityIndex: MutableLiveData<Int> = MutableLiveData(0)


    private lateinit var mDialog: AlertDialog

    fun show() {

        initViews()
    }

    private fun initViews() {

        mBinding = DataBindingUtil.inflate(
            LayoutInflater.from(activity),
            R.layout.view_task_issue_dialogue,
            null,
            false
        )

        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        builder.setView(mBinding.root)
        mBinding.taskIssueDialogue = this


        mDialog = builder.create()
        mDialog.show()

    }

    @SuppressLint("SetTextI18n")
    fun validateIssue() {

        val issueTitle = mBinding.etProjectName.value
        val priority = TaskPriority.values().get(issuePriorityIndex.value!!)

        if (issueTitle.length < 3 || issueTitle.length > 50) {
            mBinding.tvError.text = "Add Proper Issue title"
            return
        }

        val task = TaskIssue(
            issue = issueTitle,
            description = issueDescription.value!!,
            priority = priority
        )

        onAddIssue(task)


        cancel()
    }


    private fun cancel() {

        mDialog.cancel()
        mDialog.dismiss()

    }

}