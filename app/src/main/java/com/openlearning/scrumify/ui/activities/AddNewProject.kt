package com.openlearning.scrumify.ui.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.openlearning.scrumify.databinding.ActivityAddNewProjectBinding
import com.openlearning.scrumify.dialogues.DatePicker
import com.openlearning.scrumify.dialogues.LoadingDialogue
import com.openlearning.scrumify.interfaces.CustomHooks
import com.openlearning.scrumify.sealed.State
import com.openlearning.scrumify.utils.common.getDateString
import com.openlearning.scrumify.utils.common.showLoading
import com.openlearning.scrumify.utils.extensions.ifAllTrue
import com.openlearning.scrumify.viewmodels.AddNewProjectVM

class AddNewProject : AppCompatActivity(), CustomHooks {

    private val TAG = "AddNewProjectTAG"

    private lateinit var mBinding: ActivityAddNewProjectBinding
    private lateinit var viewModel: AddNewProjectVM
    private var loader: LoadingDialogue? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityAddNewProjectBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        callHooks()
    }

    override fun callHooks() {

        initViews()
        initListeners()
        observe()

    }

    override fun initViews() {

        viewModel = ViewModelProvider(this).get(AddNewProjectVM::class.java)
        mBinding.viewModel = viewModel

        mBinding.ivSelectEndDate.visibility = View.GONE

    }

    override fun initListeners() {

        mBinding.ivSelectStartDate.setOnClickListener {

            DatePicker(
                activity = this,
                returnDate = {
                    viewModel.projectStartDate.value = it
                    viewModel.projectEndDate.value = null

                }
            )
                .show(supportFragmentManager, "Select Starting Date")
        }

        mBinding.ivSelectEndDate.setOnClickListener {
            Log.d(TAG, "initListeners: min date: ${viewModel.projectStartDate.value}")
            DatePicker(
                activity = this,
                returnDate = {
                    viewModel.projectEndDate.value = it
                },
                minDate = viewModel.projectStartDate.value
            )
                .show(supportFragmentManager, "Select Ending Date")
        }

    }

    override fun observe() {

        viewModel.projectAddedState.observe(this, {

            when (it) {


                is State.Idle -> {
                    loader?.cancel()
                    mBinding.tvError.text = "";
                }
                is State.Failure -> {
                    loader!!.cancel()
                    mBinding.tvError.text = it.value as? String
                }
                is State.Loading -> {
                    loader = showLoading(
                        this,
                        "Please wait !!",
                        it.value as String
                    )
                }
                is State.Success -> {
                    loader?.cancel()
                    onProjectAdded()
                }
            }
        })


        viewModel.projectStartDate.observe(this, {
            viewModel.onStartDateAdded()
            mBinding.apply {
                tvStartDate.text = getDateString(it)
                ivSelectEndDate.visibility = View.VISIBLE
            }
        })

        viewModel.projectEndDate.observe(this, {
            if (it != null) {
                viewModel.onEndDateAdded()
                mBinding.tvEndDate.text = getDateString(it)
            } else {
                mBinding.tvEndDate.text = ""
                viewModel.onEndDateRemoved()
            }
        })

        viewModel.groupValid.observe(this, {
            mBinding.ready = it.ifAllTrue()
        })
    }

    private fun onProjectAdded() {

        Toast.makeText(this, "Project Added", Toast.LENGTH_SHORT).show()
    }
}