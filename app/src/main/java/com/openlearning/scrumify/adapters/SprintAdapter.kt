package com.openlearning.scrumify.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.openlearning.scrumify.databinding.ItemSingleSprintBinding
import com.openlearning.scrumify.databinding.ViewSprintTaskBinding
import com.openlearning.scrumify.models.Sprint
import com.openlearning.scrumify.models.SprintTask

class SprintAdapter(
    val context: Context,
    var projectSprints: List<Sprint>,
    val sprintEdit: (Int) -> Unit,
    val onClicked: (Int) -> Unit,
    val onSprintTaskClicked: (Sprint, SprintTask) -> Unit,
    var sprintSelection: Boolean = false
) :
    RecyclerView.Adapter<SprintAdapter.TaskVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskVH {

        val binding =
            ItemSingleSprintBinding.inflate(LayoutInflater.from(context), parent, false)
        return TaskVH(binding)

    }

    override fun onBindViewHolder(holder: TaskVH, position: Int) {

        val sprint = projectSprints[position]
        holder.binding.sprint = sprint
        holder.binding.ivEdit.setOnClickListener {

            sprintEdit(position)
        }

        holder.binding.root.setOnClickListener {

            if (sprintSelection) {

                onClicked(position)
                sprintSelection = false
            }
        }

        if (sprint.sprintTasks != null && sprint.sprintTasks!!.isNotEmpty()) {

            for (sprintTask in sprint.sprintTasks!!) {

                if (sprintTask.task != null) {

                    val binding = ViewSprintTaskBinding.inflate(
                        LayoutInflater.from(context),
                        holder.binding.llTasksAppender,
                        false
                    )
                    binding.sprintTask = sprintTask

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

    inner class TaskVH(val binding: ItemSingleSprintBinding) :
        RecyclerView.ViewHolder(binding.root)

}

class SprintTaskAdapter(
    private val sprintTasks: List<SprintTask>
) : RecyclerView.Adapter<SprintTaskAdapter.SprintTaskVH>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SprintTaskVH {

        val binding =
            ViewSprintTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SprintTaskVH(binding)

    }

    override fun onBindViewHolder(holder: SprintTaskVH, position: Int) {

        holder.binding.llAssignedUsersAppender

    }

    override fun getItemCount(): Int {

        return sprintTasks.size
    }

    inner class SprintTaskVH(val binding: ViewSprintTaskBinding) :
        RecyclerView.ViewHolder(binding.root)

}