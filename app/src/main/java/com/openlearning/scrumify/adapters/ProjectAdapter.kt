package com.openlearning.scrumify.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.openlearning.scrumify.R
import com.openlearning.scrumify.databinding.ItemProjectBinding
import com.openlearning.scrumify.models.Project
import com.openlearning.scrumify.models.ROLES
import com.openlearning.scrumify.utils.common.animateItemView
import com.openlearning.scrumify.utils.common.getMyRole

class ProjectAdapter(
    private val projects: List<Project>,
    private val myUid: String,
    private val itemClicked: (Project, ROLES) -> Unit
) :
    RecyclerView.Adapter<ProjectAdapter.ProjectVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectVH {

        val binding = ItemProjectBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            .also {
                animateItemView(it.root, R.anim.fade_in)
            }
        return ProjectVH(binding)

    }

    override fun onBindViewHolder(holder: ProjectVH, position: Int) {

        val project = projects[position]
        holder.binding.project = project
        val myRole = getMyRole(project, myUid)

        if (myRole != null) {
            holder.binding.apply {
                tvRole.text = myRole.name.replace("_", " ")
                cvRoot.setOnClickListener {
                    itemClicked(project, myRole)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return projects.size
    }

    inner class ProjectVH(val binding: ItemProjectBinding) : RecyclerView.ViewHolder(binding.root)

}