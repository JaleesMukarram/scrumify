package com.openlearning.scrumify.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.openlearning.scrumify.databinding.ItemAddedUserBinding
import com.openlearning.scrumify.models.ProjectUserData
import com.openlearning.scrumify.models.User

class AddedUserAdapter(
    var projectUserDatas: List<ProjectUserData>,
    val onUserClick: (User) -> Unit
) :
    RecyclerView.Adapter<AddedUserAdapter.AddedUserVM>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddedUserVM {

        val binding =
            ItemAddedUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddedUserVM(binding)

    }

    override fun onBindViewHolder(holder: AddedUserVM, position: Int) {

        val projectUser = projectUserDatas[position]
        holder.binding.projectUserData = projectUser
        holder.binding.root.setOnClickListener {
            onUserClick(projectUser.user)
        }
    }

    override fun getItemCount(): Int {
        return projectUserDatas.size
    }

    inner class AddedUserVM(val binding: ItemAddedUserBinding) :
        RecyclerView.ViewHolder(binding.root)

}