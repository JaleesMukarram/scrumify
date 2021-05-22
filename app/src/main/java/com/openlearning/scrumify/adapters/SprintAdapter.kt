package com.openlearning.scrumify.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.openlearning.scrumify.databinding.ItemSingleSprintBinding
import com.openlearning.scrumify.models.Sprint
import com.openlearning.scrumify.models.Task

class SprintAdapter(
    var projectSprints: List<Sprint>,
    val sprintEdit: (Task) -> Unit
) :
    RecyclerView.Adapter<SprintAdapter.TaskVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskVH {

        val binding =
            ItemSingleSprintBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskVH(binding)

    }

    override fun onBindViewHolder(holder: TaskVH, position: Int) {

        val sprint = projectSprints[position]
        holder.binding.sprint = sprint
        holder.binding.ivEdit.setOnClickListener {

        }
    }

    override fun getItemCount(): Int {
        return projectSprints.size
    }

    inner class TaskVH(val binding: ItemSingleSprintBinding) :
        RecyclerView.ViewHolder(binding.root)

}