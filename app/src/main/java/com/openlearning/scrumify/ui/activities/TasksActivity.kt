package com.openlearning.scrumify.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.openlearning.scrumify.adapters.TaskAdapter
import com.openlearning.scrumify.databinding.ActivityTasksBinding
import com.openlearning.scrumify.dialogues.TaskDialogue
import com.openlearning.scrumify.interfaces.CustomHooks
import com.openlearning.scrumify.models.Project
import com.openlearning.scrumify.models.Task
import com.openlearning.scrumify.sealed.State
import com.openlearning.scrumify.utils.PROJECT_INTENT
import com.openlearning.scrumify.viewmodels.TasksVM

class TasksActivity : AppCompatActivity(), CustomHooks {

    private lateinit var mBinding: ActivityTasksBinding
    private lateinit var viewModel: TasksVM

    private var project: Project? = null

    private lateinit var taskAdapter: TaskAdapter


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
        viewModel = ViewModelProvider(this).get(TasksVM::class.java)
        viewModel.project = project!!
        mBinding.viewModel = viewModel

    }

    override fun initListeners() {

        mBinding.ivAddTask.setOnClickListener {

            val taskDialogue = TaskDialogue(this, taskReady)
            taskDialogue.show()

        }
    }

    override fun observe() {

        viewModel.allTasks.observe(this, {

            if (it != null) {

                taskAdapter = TaskAdapter(it) { editTask ->

                    TaskDialogue(this, taskReady, editTask, { viewModel.deleteTask(it) }).show()

                }

                mBinding.rvAllTasks.adapter = taskAdapter
            }
        })

        viewModel.taskUploadProgress.observe(this, {

            when (it) {
                is State.Success -> {

                    viewModel.getAllTasks()

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

        viewModel.getAllTasks()

    }

    private val taskReady: (Task, Boolean) -> Unit = { task, forUpdate ->

        if (forUpdate) {
            viewModel.updateTask(task)

        } else {
            viewModel.uploadTask(task)
        }

    }
}