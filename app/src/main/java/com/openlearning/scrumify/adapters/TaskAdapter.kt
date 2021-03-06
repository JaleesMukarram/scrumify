package com.openlearning.scrumify.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.openlearning.scrumify.databinding.ItemProjectTeamMemberBinding
import com.openlearning.scrumify.databinding.ItemSingleTaskBinding
import com.openlearning.scrumify.models.Task
import com.openlearning.scrumify.models.User

class TaskAdapter(
    var projectTasks: List<Task>,
    val taskEdit: (Int) -> Unit,
    val taskOpen: (Int) -> Unit,
    val addToSprint: (Int) -> Unit,
    var sprintAvailable: Boolean = false
) :
    RecyclerView.Adapter<TaskAdapter.TaskVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskVH {

        val binding =
            ItemSingleTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskVH(binding)

    }

    override fun onBindViewHolder(holder: TaskVH, position: Int) {

        val task = projectTasks[position]
        holder.binding.task = task
        holder.binding.sprintAvailable = sprintAvailable

        holder.binding.root.setOnClickListener {
            taskOpen(position)
        }
        holder.binding.ivEdit.setOnClickListener {
            taskEdit(position)
        }
        holder.binding.btnAddToSprint.setOnClickListener {

            addToSprint(position)
        }


    }

    override fun getItemCount(): Int {
        return projectTasks.size
    }

    inner class TaskVH(val binding: ItemSingleTaskBinding) :
        RecyclerView.ViewHolder(binding.root)

}