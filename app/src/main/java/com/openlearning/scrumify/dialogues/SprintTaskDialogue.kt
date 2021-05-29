package com.openlearning.scrumify.dialogues

import android.app.Activity
import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import com.openlearning.scrumify.R
import com.openlearning.scrumify.databinding.ViewSprintTaskDialogueBinding
import com.openlearning.scrumify.databinding.ViewTaskAssignedToBinding
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

        addUserToFreeAndAssigned()
        initListeners()

        if (deadline != null) {
            mBinding.tvEndDate.text = getDateString(deadline!!)
        }

        mDialog = Dialog(activity, android.R.style.Theme_DeviceDefault_NoActionBar_Fullscreen)
        mDialog.setContentView(mBinding.root)
        mDialog.show()

        GlobalScope.launch {
            delay(500)
            withContext(Dispatchers.Main) {
                mBinding.sprProjectStatus.setSelection(sprintTask.taskStatus.ordinal)
            }
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
    }

    private fun addUserToFreeAndAssigned() {

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
        addUserToFreeAndAssigned()
    }

    private fun removeThisUserFromSprintTask(userId: String) {

        alreadyAssignedUsers.remove(userId)
        addUserToFreeAndAssigned()
    }

    private fun cancel() {

        mDialog.cancel()
        mDialog.dismiss()

    }

}