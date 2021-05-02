package com.openlearning.scrumify.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.openlearning.scrumify.R
import com.openlearning.scrumify.databinding.ItemProjectBinding
import com.openlearning.scrumify.databinding.ItemUserSearchBinding
import com.openlearning.scrumify.models.Project
import com.openlearning.scrumify.models.ROLES
import com.openlearning.scrumify.models.User
import com.openlearning.scrumify.utils.common.getMyRole

class NewUserSearchAdapter(
    var users: List<User>,
    val onUserClick: (User) -> Unit
) :
    RecyclerView.Adapter<NewUserSearchAdapter.NewUserSearchVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewUserSearchVH {

        val binding =
            ItemUserSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewUserSearchVH(binding)

    }

    override fun onBindViewHolder(holder: NewUserSearchVH, position: Int) {

        val user = users[position]
        holder.binding.tvName.text = user.name
        holder.binding.root.setOnClickListener {
            onUserClick(user)

        }
    }

    override fun getItemCount(): Int {
        return users.size
    }

    inner class NewUserSearchVH(val binding: ItemUserSearchBinding) :
        RecyclerView.ViewHolder(binding.root)

}