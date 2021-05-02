package com.openlearning.scrumify.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.openlearning.scrumify.adapters.ProjectAdapter
import com.openlearning.scrumify.databinding.ActivityShowProjectsBinding
import com.openlearning.scrumify.interfaces.CustomHooks
import com.openlearning.scrumify.models.Project
import com.openlearning.scrumify.utils.PROJECT_INTENT
import com.openlearning.scrumify.utils.SPLASH_SKIP_ANIMATION
import com.openlearning.scrumify.utils.common.changeActivity
import com.openlearning.scrumify.viewmodels.ShowProjectVM

class ShowProjectsActivity : AppCompatActivity(), CustomHooks {

    private lateinit var mBinding: ActivityShowProjectsBinding
    private lateinit var viewModel: ShowProjectVM
    private lateinit var projectAdapter: ProjectAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityShowProjectsBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        callHooks()
    }

    override fun callHooks() {

        initViews()
        initListeners()
        observe()

    }

    override fun initViews() {

        viewModel = ViewModelProvider(this).get(ShowProjectVM::class.java)
        mBinding.rvAllProjects.setHasFixedSize(true)
    }

    override fun initListeners() {

    }

    override fun observe() {

        viewModel.myAllProjects.observe(this, {

            if (it != null) {
                onProjectsFound(it)
            }
        })

        viewModel.fetchMyProjects()
    }

    private fun onProjectsFound(projects: List<Project>) {

        projectAdapter = ProjectAdapter(
            projects,
            viewModel.getMyUid()
        ) { project, role ->

            val intent = Intent(this, ProjectDetailsActivity::class.java)
            intent.putExtra(PROJECT_INTENT, project)
            changeActivity(this, intent, false)

        }
        mBinding.rvAllProjects.adapter = projectAdapter
    }


}