package com.openlearning.scrumify.dialogues

import android.app.Activity
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import com.openlearning.scrumify.R
import com.openlearning.scrumify.databinding.ViewProjectUserNewDialogueBinding
import com.openlearning.scrumify.models.ROLES
import com.openlearning.scrumify.models.User
import com.openlearning.scrumify.utils.common.getRolesArray

class ProjectUserNewDialogue(
    private val activity: Activity,
    val user: User,
    val added: Boolean,
    private val onAdded: (Int) -> Unit,
    private val onRemove: (User) -> Unit
) {

    val roleIndex: MutableLiveData<Int> = MutableLiveData(0)

    val rolesArray: ArrayList<String> = getRolesArray()

    private lateinit var mDialog: AlertDialog

    fun show() {

        initViews()
    }

    private fun initViews() {

        val binding: ViewProjectUserNewDialogueBinding = DataBindingUtil.inflate(
            LayoutInflater.from(activity),
            R.layout.view_project_user_new_dialogue,
            null,
            false
        )

        if (added) {
            binding.apply {
                btnDelete.visibility = View.VISIBLE
                btnAdd.text = "Change Role"
            }
        }

        binding.dialogue = this
        binding.btnAdd.setOnClickListener {
            onAdded(roleIndex.value!!)
            cancel()
        }
        binding.btnDelete.setOnClickListener {
            onRemove(user)
            cancel()

        }


        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        builder.setView(binding.root)

        mDialog = builder.create()
        mDialog.setCanceledOnTouchOutside(false)
        mDialog.show()

    }

    private fun cancel() {

        mDialog.cancel()
        mDialog.dismiss()

    }

}