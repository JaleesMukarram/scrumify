package com.openlearning.scrumify.dialogues

import android.app.Activity
import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import com.openlearning.scrumify.R
import com.openlearning.scrumify.databinding.ViewSprintTaskDialogueBinding
import com.openlearning.scrumify.databinding.ViewTaskAssignedToBinding
import com.openlearning.scrumify.databinding.ViewTaskIssueBinding
import com.openlearning.scrumify.models.*
import com.openlearning.scrumify.utils.common.animateItemView
import com.openlearning.scrumify.utils.common.getDateString
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList

class SprintTaskDialogue(
    private val activity: Activity,
    private val sprint: Sprint,
    val sprintTask: SprintTask,
    private val projectUsers: List<ProjectUserData>?,
    private val onSave: (SprintTask) -> Unit,
    private val onDelete: (SprintTask) -> Unit,
    private val forTeamMember: Boolean = false,
    private val alreadyAssignedUsers: MutableList<String> = ArrayList(sprintTask.assignedUsers),
    private var deadline: Date? = sprintTask.deadline

) {
    lateinit var mBinding: ViewSprintTaskDialogueBinding

    // Data
    val enumClass = TaskStatus::class.java


    // Fields
    val taskStatusIndex: MutableLiveData<Int> = MutableLiveData(0)


    private lateinit var mDialog: Dialog

    fun show() {

        initViews()
    }

    private fun initViews() {

        mBinding = DataBindingUtil.inflate(
            LayoutInflater.from(activity),
            R.layout.view_sprint_task_dialogue,
            null,
            false
        )

        mBinding.sprintTaskDialogue = this

        validateAssignedUsers()
        validateTaskIssue()
        initListeners()

        if (deadline != null) {
            mBinding.tvEndDate.text = getDateString(deadline!!)
        }

        if (forTeamMember) {

            makeForTeamMember()
        }

        mDialog = Dialog(activity, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen)
        mDialog.setContentView(mBinding.root)
        mDialog.show()

        GlobalScope.launch {
            delay(500)
            withContext(Dispatchers.Main) {
                mBinding.sprProjectStatus.setSelection(sprintTask.taskStatus.ordinal)
            }
        }
    }

    private fun makeForTeamMember() {

        mBinding.apply {

            // Date
            ivSelectEndDate.visibility = View.GONE


            alreadyAssignToUser.visibility = View.GONE
            llAssignedUserAppender.visibility = View.GONE

            assignToUser.visibility = View.GONE
            llFreeUserAppender.visibility = View.GONE

            btnDelete.visibility = View.GONE

        }


    }

    private fun initListeners() {

        mBinding.ivSelectEndDate.setOnClickListener {

            DatePicker(
                activity = activity,
                returnDate = {
                    deadline = it
                    mBinding.tvEndDate.text = getDateString(deadline!!)
                },
                minDate = sprint.startDate,
                maxDate = sprint.endDate
            )
                .show(
                    (activity as AppCompatActivity).supportFragmentManager,
                    "Select Deadline Date"
                )
        }

        mBinding.btnSave.setOnClickListener {

            sprintTask.deadline = deadline

            sprintTask.assignedUsers.clear()
            sprintTask.assignedUsers.addAll(alreadyAssignedUsers)

            sprintTask.taskStatus = TaskStatus.values()[taskStatusIndex.value!!]

            onSave(sprintTask)
            cancel()

        }

        mBinding.btnDelete.setOnClickListener {

            onDelete(sprintTask)
            cancel()

        }

        mBinding.tvAddIssue.setOnClickListener {

            TaskIssueDialogue(
                activity
            ) {

                sprintTask.taskIssues.add(it)
                validateTaskIssue()

            }.show()

        }
    }

    private fun validateTaskIssue() {

        mBinding.llTaskIssuesAppender.removeAllViews()

        for (taskIssue in sprintTask.taskIssues) {

            val binding = ViewTaskIssueBinding.inflate(
                LayoutInflater.from(activity),
                mBinding.llTaskIssuesAppender,
                false
            ).also {
                animateItemView(it.root, R.anim.fade_in)
            }

            binding.apply {

                this.taskIssue = taskIssue
                mcvMainContainer.setOnClickListener {

                    if (tvTaskIssueDescription.isVisible) {
                        tvTaskIssueDescription.visibility = View.GONE
                    } else {
                        tvTaskIssueDescription.visibility = View.VISIBLE
                    }
                }

                ivClose.setOnClickListener {

                    sprintTask.taskIssues.remove(taskIssue)
                    validateTaskIssue()
                }

            }

            mBinding.llTaskIssuesAppender.addView(binding.root)


        }


    }

    private fun validateAssignedUsers() {

        if (projectUsers == null) {
            return
        }

        mBinding.llFreeUserAppender.removeAllViews()
        mBinding.llAssignedUserAppender.removeAllViews()

        for (user in projectUsers) {

            val binding = ViewTaskAssignedToBinding.inflate(
                LayoutInflater.from(activity),
                mBinding.llFreeUserAppender,
                false
            ).also {
                animateItemView(it.root, R.anim.fade_in)
            }

            binding.userData = user

            // Already assigned to this user
            if (user.user.uid in alreadyAssignedUsers) {

                binding.apply {

                    ivRemove.visibility = View.VISIBLE
                    clRootContainer.backgroundTintList =
                        ContextCompat.getColorStateList(activity, R.color.colorPrimary)

                    root.setOnClickListener {
                        removeThisUserFromSprintTask(user.user.uid)
                    }

                }

                mBinding.llAssignedUserAppender.addView(binding.root)

            }
            // Not Assigned to this user
            else {

                binding.apply {
                    root.setOnClickListener {
                        addThisUserToSprintTask(user)
                    }

                }
                mBinding.llFreeUserAppender.addView(binding.root)
            }

        }
    }

    private fun addThisUserToSprintTask(user: ProjectUserData) {

        alreadyAssignedUsers.add(user.user.uid)
        validateAssignedUsers()
    }

    private fun removeThisUserFromSprintTask(userId: String) {

        alreadyAssignedUsers.remove(userId)
        validateAssignedUsers()
    }

    private fun cancel() {

        mDialog.cancel()
        mDialog.dismiss()

    }

}