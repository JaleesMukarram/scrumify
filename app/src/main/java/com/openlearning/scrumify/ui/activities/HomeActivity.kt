package com.openlearning.scrumify.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.openlearning.scrumify.databinding.ActivityHomeBinding
import com.openlearning.scrumify.interfaces.CustomHooks
import com.openlearning.scrumify.utils.common.changeActivity
import com.openlearning.scrumify.viewmodels.HomeVM

class HomeActivity : AppCompatActivity(), CustomHooks {

    private lateinit var mBinding: ActivityHomeBinding
    private lateinit var viewModel: HomeVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        callHooks()

    }

    override fun callHooks() {

        initViews()
        initListeners()
        observe()

    }

    override fun initViews() {

    }

    override fun initListeners() {

        mBinding.cvCreateProject.setOnClickListener {

            changeActivity(this, AddNewProject::class.java, false)

        }

        mBinding.cvViewProject.setOnClickListener {

            changeActivity(this, ShowProjectsActivity::class.java, false)

        }
    }
}