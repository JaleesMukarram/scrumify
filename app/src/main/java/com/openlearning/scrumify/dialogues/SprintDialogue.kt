package com.openlearning.scrumify.dialogues

import android.app.Activity
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import com.openlearning.scrumify.R
import com.openlearning.scrumify.databinding.ViewSprintDialogueBinding
import com.openlearning.scrumify.databinding.ViewTaskDialogueBinding
import com.openlearning.scrumify.models.Project
import com.openlearning.scrumify.models.Sprint
import com.openlearning.scrumify.models.Task
import com.openlearning.scrumify.models.TaskPriority
import com.openlearning.scrumify.utils.common.getDateString
import com.openlearning.scrumify.utils.extensions.value
import java.util.*

class SprintDialogue(
    private val activity: Activity,
    private val project: Project,
    private val sprintReady: (Sprint) -> Unit

) {
    lateinit var mBinding: ViewSprintDialogueBinding

    // Data
    val enumClass = TaskPriority::class.java


    // Fields
    val sprintName: MutableLiveData<String> = MutableLiveData("")
    private val sprintStartDate: MutableLiveData<Date> = MutableLiveData()
    private val sprintEndDate: MutableLiveData<Date> = MutableLiveData()


    private lateinit var mDialog: AlertDialog

    fun show() {

        initViews()
    }

    private fun initViews() {

        mBinding = DataBindingUtil.inflate(
            LayoutInflater.from(activity),
            R.layout.view_sprint_dialogue,
            null,
            false
        )

        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        builder.setView(mBinding.root)

        mBinding.apply {

            sprintDialogue = this@SprintDialogue
            ivSelectEndDate.visibility = View.INVISIBLE

            ivSelectStartDate.setOnClickListener {

                DatePicker(
                    activity = activity,
                    returnDate = {
                        sprintStartDate.value = it
                        onStartDateAdded()
                    },
                    minDate = project.startDate,
                    maxDate = project.endDate
                )
                    .show(
                        (activity as AppCompatActivity).supportFragmentManager,
                        "Select Ending Date"
                    )

            }
        }


        mDialog = builder.create()
        mDialog.show()

    }

    private fun onStartDateAdded() {

        mBinding.apply {
            tvStartDate.text = getDateString(sprintStartDate.value!!)
            tvEndDate.text = ""

            sprintEndDate.value = null

            ivSelectEndDate.visibility = View.VISIBLE
            ivSelectEndDate.setOnClickListener {


                DatePicker(
                    activity = activity,
                    returnDate = {
                        sprintEndDate.value = it
                        tvEndDate.text = getDateString(sprintEndDate.value!!)

                    },
                    minDate = sprintStartDate.value,
                    maxDate = project.endDate
                )
                    .show(
                        (activity as AppCompatActivity).supportFragmentManager,
                        "Select Ending Date"
                    )
            }

        }
    }

    fun validateSprint() {

        mBinding.tvError.text = null

        val name = mBinding.etSprintName.value
        if (name.length < 4 || name.length > 24) {
            mBinding.tvError.text = activity.getString(R.string.sprint_name_check)
            return
        }

        if (sprintStartDate.value == null || sprintEndDate.value == null) {
            mBinding.tvError.text = activity.getString(R.string.sprint_date_check)
            return
        }

        val sprint = Sprint(
            name = name,
            startDate = sprintStartDate.value!!,
            endDate = sprintEndDate.value!!
        )

        sprintReady(sprint)
        cancel()


    }


    private fun cancel() {

        mDialog.cancel()
        mDialog.dismiss()

    }

}