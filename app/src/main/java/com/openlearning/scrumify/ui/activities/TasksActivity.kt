package com.openlearning.scrumify.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.openlearning.scrumify.R
import com.openlearning.scrumify.databinding.ActivityTasksBinding
import com.openlearning.scrumify.interfaces.CustomHooks
import com.openlearning.scrumify.models.Project
import com.openlearning.scrumify.ui.fragments.SprintFragment
import com.openlearning.scrumify.ui.fragments.TaskFragment
import com.openlearning.scrumify.utils.PROJECT_INTENT
import com.openlearning.scrumify.viewmodels.SprintsVM
import com.openlearning.scrumify.viewmodels.TasksVM

class TasksActivity : AppCompatActivity(), CustomHooks {

    private lateinit var mBinding: ActivityTasksBinding
    private lateinit var viewModelTasks: TasksVM
    private lateinit var viewModelSprints: SprintsVM

    private var project: Project? = null

    private val taskFragment = TaskFragment()
    private val sprintFragment = SprintFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityTasksBinding.inflate(layoutInflater)
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
        viewModelTasks = ViewModelProvider(this).get(TasksVM::class.java)
        viewModelSprints = ViewModelProvider(this).get(SprintsVM::class.java)

        viewModelTasks.project = project!!
        viewModelSprints.project = project!!
        mBinding.viewModel = viewModelTasks

        attachFragment(taskFragment)
    }

    override fun initListeners() {

        mBinding.bnvBottomOptions.setOnNavigationItemSelectedListener {

            when (it.itemId) {

                R.id.task -> {
                    attachFragment(taskFragment)
                }
                R.id.sprint -> {
                    attachFragment(sprintFragment)
                }
            }
            true
        }

    }

    override fun observe() {}

    private fun attachFragment(fragment: Fragment) {

        supportFragmentManager.beginTransaction().replace(R.id.frameLayout, fragment).commit()

    }

    private val onNavEvent: (Fragment) -> Unit = { fragment ->

        attachFragment(fragment)

    }
}