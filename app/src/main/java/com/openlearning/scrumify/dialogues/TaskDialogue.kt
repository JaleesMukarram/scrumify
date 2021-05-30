package com.openlearning.scrumify.dialogues

import android.app.Activity
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import com.openlearning.scrumify.R
import com.openlearning.scrumify.databinding.ViewTaskDialogueBinding
import com.openlearning.scrumify.models.Task
import com.openlearning.scrumify.models.TaskPriority
import com.openlearning.scrumify.utils.extensions.value

class TaskDialogue(
    private val activity: Activity,
    private val taskReady: (Task, Boolean) -> Unit,
    private val updateTask: Task? = null,
    private val taskDelete: ((String) -> Unit)? = null,

    ) {
    lateinit var mBinding: ViewTaskDialogueBinding

    // Data
    val enumClass = TaskPriority::class.java


    // Fields
    val propertyName: MutableLiveData<String> = MutableLiveData("")
    val taskPriorityIndex: MutableLiveData<Int> = MutableLiveData(0)


    private lateinit var mDialog: AlertDialog

    fun show() {

        initViews()
    }

    private fun initViews() {

        mBinding = DataBindingUtil.inflate(
            LayoutInflater.from(activity),
            R.layout.view_task_dialogue,
            null,
            false
        )

        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        builder.setView(mBinding.root)
        mBinding.taskDialogue = this


        if (updateTask != null) {
            mBinding.apply {
                btnAddTask.text = activity.getString(R.string.update_task)
                btnDelete.visibility = View.VISIBLE

            }
            propertyName.value = updateTask.name

        }


        mDialog = builder.create()
        mDialog.show()

    }

    fun validateTask() {

        val name = mBinding.etProjectName.value
        if (name.length < 4 || name.length > 24) {
            mBinding.tvError.text = activity.getString(R.string.task_name)
            return
        }

        val task = updateTask?.copy(
            name = name, priority = TaskPriority.values()[taskPriorityIndex.value!!]
        )
            ?: Task(
                name = name,
                priority = TaskPriority.values().get(taskPriorityIndex.value!!)
            )

        taskReady(task, updateTask != null)
        cancel()

    }

    fun deleteTask() {

        taskDelete!!(updateTask!!.id)
        cancel()

    }

    private fun cancel() {

        mDialog.cancel()
        mDialog.dismiss()

    }

}