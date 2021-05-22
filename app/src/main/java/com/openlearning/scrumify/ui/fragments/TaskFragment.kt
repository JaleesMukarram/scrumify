package com.openlearning.scrumify.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.openlearning.scrumify.adapters.TaskAdapter
import com.openlearning.scrumify.databinding.FragmentTaskBinding
import com.openlearning.scrumify.dialogues.TaskAssignDialogue
import com.openlearning.scrumify.dialogues.TaskDialogue
import com.openlearning.scrumify.interfaces.CustomHooks
import com.openlearning.scrumify.models.Task
import com.openlearning.scrumify.sealed.State
import com.openlearning.scrumify.viewmodels.TasksVM

class TaskFragment : Fragment(), CustomHooks {

    private lateinit var mBinding: FragmentTaskBinding
    private val viewModel: TasksVM by activityViewModels()

    private lateinit var taskAdapter: TaskAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        mBinding = FragmentTaskBinding.inflate(layoutInflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callHooks()
    }

    override fun callHooks() {

        initViews()
        initListeners()
        observe()

    }

    override fun initViews() {

        mBinding.viewModel = viewModel

        taskAdapter = TaskAdapter(
            arrayListOf(),
            {
                TaskDialogue(
                    requireActivity(),
                    taskReady,
                    it,
                    { viewModel.deleteTask(it) }).show()
            },
            { openTask(it) }
        )

        mBinding.rvAllTasks.adapter = taskAdapter
    }

    override fun initListeners() {

        mBinding.fabNewProject.setOnClickListener {

            val taskDialogue = TaskDialogue(requireActivity(), taskReady)
            taskDialogue.show()

        }

    }

    override fun observe() {

        viewModel.allTasks.observe(viewLifecycleOwner, {

            onTasksReady(it)

        })

        viewModel.taskUploadProgress.observe(viewLifecycleOwner, {

            when (it) {
                is State.Success -> {

                    viewModel.getAllTasks()

                }

                is State.Failure -> {

                    Toast.makeText(
                        requireContext(),
                        "`Filed to upload task ${(it.value as Exception).localizedMessage}`",
                        Toast.LENGTH_SHORT
                    ).show()

                }
                else -> Unit
            }
        })

        viewModel.getAllTasks()

    }

    private fun onTasksReady(tasks: List<Task>?) {

        if (tasks != null) {
            taskAdapter.projectTasks = tasks
            taskAdapter.notifyDataSetChanged()
        }
    }

    private fun openTask(it: Task) {

        TaskAssignDialogue(requireActivity()).show()

    }

    private val taskReady: (Task, Boolean) -> Unit = { task, forUpdate ->

        if (forUpdate) {
            viewModel.updateTask(task)

        } else {
            viewModel.uploadTask(task)
        }

    }
}