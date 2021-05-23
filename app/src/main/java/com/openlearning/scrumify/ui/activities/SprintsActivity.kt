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

    }

    override fun initViews() {


    }

    override fun initListeners() {


    }

    override fun observe() {


    }
}