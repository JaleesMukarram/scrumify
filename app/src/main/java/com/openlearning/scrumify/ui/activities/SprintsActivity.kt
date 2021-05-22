package com.openlearning.scrumify.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.openlearning.scrumify.R
import com.openlearning.scrumify.adapters.SprintAdapter
import com.openlearning.scrumify.adapters.TaskAdapter
import com.openlearning.scrumify.databinding.ActivitySprintsBinding
import com.openlearning.scrumify.dialogues.SprintDialogue
import com.openlearning.scrumify.dialogues.TaskDialogue
import com.openlearning.scrumify.interfaces.CustomHooks
import com.openlearning.scrumify.models.Project
import com.openlearning.scrumify.models.Sprint
import com.openlearning.scrumify.sealed.State
import com.openlearning.scrumify.utils.PROJECT_INTENT
import com.openlearning.scrumify.viewmodels.SplashVM
import com.openlearning.scrumify.viewmodels.SprintsVM

class SprintsActivity : AppCompatActivity(), CustomHooks {

    private lateinit var mBinding: ActivitySprintsBinding
    private lateinit var viewModel: SprintsVM


    private var project: Project? = null

    private lateinit var sprintAdapter: SprintAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySprintsBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        callHooks()
    }

    override fun callHooks() {

        handleIntent()
        initViews()
        initListeners()
        observe()

    }

    override fun handleIntent() {
        project = intent.getParcelableExtra(PROJECT_INTENT)
        if (project == null) {
            finish()
            return
        }
    }

    override fun initViews() {

        viewModel = ViewModelProvider(this).get(SprintsVM::class.java)
        viewModel.project = project!!
        mBinding.viewModel = viewModel

    }

    override fun initListeners() {


    }

    override fun observe() {

        viewModel.sprintUploadProgress.observe(this, {

            when (it) {
                is State.Success -> {

//                    viewModel.getAllTasks()

                }

                is State.Failure -> {

                    Toast.makeText(
                        this,
                        "`Filed to upload task ${(it.value as Exception).localizedMessage}`",
                        Toast.LENGTH_SHORT
                    ).show()

                }
                else -> Unit
            }

        })

        viewModel.allSprints.observe(this, {
            if (it != null) {

                sprintAdapter = SprintAdapter(it) { editTask ->

                }

                mBinding.rvAllSprints.adapter = sprintAdapter
            }
        })
        viewModel.getAllSprints()
    }
}