package com.openlearning.scrumify.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.openlearning.scrumify.R
import com.openlearning.scrumify.databinding.ItemSingleSprintBinding
import com.openlearning.scrumify.databinding.ViewSprintTaskBinding
import com.openlearning.scrumify.databinding.ViewTaskAssignedToBinding
import com.openlearning.scrumify.models.ProjectUserData
import com.openlearning.scrumify.models.Sprint
import com.openlearning.scrumify.models.SprintTask
import com.openlearning.scrumify.models.TaskStatus
import com.openlearning.scrumify.utils.common.animateItemView
import com.openlearning.scrumify.utils.common.showLoading

class SprintAdapter(
    val context: Context,
    var projectSprints: List<Sprint>,
    var projectUsers: List<ProjectUserData>?,
    val sprintEdit: (Int) -> Unit,
    val onClicked: (Int) -> Unit,
    val onSprintTaskClicked: (Sprint, SprintTask) -> Unit,
    var sprintSelection: Boolean = false
) :
    RecyclerView.Adapter<SprintAdapter.TaskVH>() {

    private val TAG = "SprintAdapterTAG"
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskVH {

        val binding =
            ItemSingleSprintBinding.inflate(LayoutInflater.from(context), parent, false)
        return TaskVH(binding)

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: TaskVH, position: Int) {

        val sprint = projectSprints[position]
        val progress = (getProgress(sprint) * 100).toInt()

        holder.binding.apply {
            this.sprint = sprint

            tvSprintProgress.text = "$progress%"
            pbrSprintProgress.progress = progress
            ivEdit.setOnClickListener {

                sprintEdit(position)
            }
            root.setOnClickListener {

                if (sprintSelection) {

                    onClicked(position)
                    sprintSelection = false
                }
            }
            llTasksAppender.removeAllViews()

        }

        if (sprint.sprintTasks != null && sprint.sprintTasks!!.isNotEmpty()) {

            for (sprintTask in sprint.sprintTasks!!) {

                if (sprintTask.task != null) {

                    val binding = ViewSprintTaskBinding.inflate(
                        LayoutInflater.from(context),
                        holder.binding.llTasksAppender,
                        false
                    ).also {
                        animateItemView(it.root, R.anim.fade_in)
                    }

                    addUserToFreeAndAssigned(sprintTask.assignedUsers, binding)

                    binding.sprintTask = sprintTask

                    if (sprintTask.taskIssues.size > 0) {
                        binding.tvTaskIssueCount.text = "${sprintTask.taskIssues.size} issue(s)"
                    }

                    binding.root.setOnClickListener {

                        onSprintTaskClicked(sprint, sprintTask)

                    }
                    holder.binding.llTasksAppender.addView(binding.root)
                }
            }

        }
    }

    override fun getItemCount(): Int {
        return projectSprints.size
    }

    private fun addUserToFreeAndAssigned(
        assignedUserIds: List<String>,
        topBinding: ViewSprintTaskBinding
    ) {

        if (projectUsers == null) {
            return
        }

        topBinding.llAssignedUsersAppender.removeAllViews()

        for (user in projectUsers!!) {

            if (user.user.uid in assignedUserIds) {

                val binding = ViewTaskAssignedToBinding.inflate(
                    LayoutInflater.from(context),
                    topBinding.llAssignedUsersAppender,
                    false
                )
                binding.userData = user
                binding.apply {
                    clRootContainer.backgroundTintList =
                        ContextCompat.getColorStateList(context, R.color.colorPrimary)
                }
                topBinding.llAssignedUsersAppender.addView(binding.root)
            }
        }

    }

    private fun getProgress(sprint: Sprint): Float {

        if (sprint.sprintTasks == null || sprint.sprintTasks!!.isEmpty()) {
            return 0f;
        }

        val total = sprint.sprintTasks!!.size.toFloat()
        var completed = 0
        for (task in sprint.sprintTasks!!) {
            if (task.taskStatus == TaskStatus.COMPLETE) {
                completed++
            }
        }

        return completed / total

    }

    inner class TaskVH(val binding: ItemSingleSprintBinding) :
        RecyclerView.ViewHolder(binding.root)

}