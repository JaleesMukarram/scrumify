package com.openlearning.scrumify.dialogues

import android.app.Activity
import android.app.AlertDialog
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import com.openlearning.scrumify.R
import com.openlearning.scrumify.databinding.ViewLoadingDialogueBinding

class LoadingDialogue(
        private val activity: Activity,
        private val message: String,
        private val description: String
) {

    private lateinit var mDialog: AlertDialog

    fun show() {

        initViews()
    }

    private fun initViews() {

        val binding: ViewLoadingDialogueBinding = DataBindingUtil.inflate(
                LayoutInflater.from(activity),
                R.layout.view_loading_dialogue,
                null,
                false
        )

        binding.message = message
        binding.description = description

        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        builder.setView(binding.root)

        mDialog = builder.create()
        mDialog.setCanceledOnTouchOutside(false)
        mDialog.show()

    }

    fun cancel() {

        mDialog.cancel()
        mDialog.dismiss()

    }

}