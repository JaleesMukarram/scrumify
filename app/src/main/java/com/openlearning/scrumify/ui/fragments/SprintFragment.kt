package com.openlearning.scrumify.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.openlearning.scrumify.adapters.SprintAdapter
import com.openlearning.scrumify.databinding.FragmentSprintsBinding
import com.openlearning.scrumify.dialogues.SprintDialogue
import com.openlearning.scrumify.dialogues.SprintTaskDialogue
import com.openlearning.scrumify.interfaces.CustomHooks
import com.openlearning.scrumify.models.Sprint
import com.openlearning.scrumify.sealed.State
import com.openlearning.scrumify.viewmodels.SprintsVM

class SprintFragment : Fragment(), CustomHooks {

    private val TAG = "SprintFragmentTAG"

    private lateinit var mBinding: FragmentSprintsBinding
    private val viewModel: SprintsVM by activityViewModels()

    private lateinit var sprintAdapter: SprintAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        mBinding = FragmentSprintsBinding.inflate(layoutInflater)
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

        sprintAdapter = SprintAdapter(
            requireContext(),
            arrayListOf(),
            viewModel.allProjectUsers.value,
            // Sprint Edit
            {},
            // Sprint Clicked
            {

                viewModel.sprintSelected.value = viewModel.allSprints.value!![it]

            },
            // Sprint Task Clicked
            { sprint, sprintTask ->

                SprintTaskDialogue(
                    requireActivity(),
                    sprint,
                    sprintTask,
                    viewModel.allProjectUsers.value,
                    // Sprint Task Update
                    {

                        viewModel.updateSprintTask(it)
//                        sprintAdapter.notifyDataSetChanged()

                    },
                    // Sprint Task Delete
                    {
                        sprint.sprintTasks = sprint.sprintTasks?.filter { filter ->
                            filter != it
                        }
                        viewModel.deleteSprintTask(it)
                        sprintAdapter.notifyDataSetChanged()

                    },
                ).show()
            }
        )

        mBinding.rvAllSprints.adapter = sprintAdapter
    }

    override fun initListeners() {

        mBinding.fabNewSprint.setOnClickListener {

            SprintDialogue(
                requireActivity(),
                viewModel.project
            ) { viewModel.addNewSprint(it) }.show()

        }

    }

    override fun observe() {

        viewModel.sprintUploadProgress.observe(viewLifecycleOwner, {

            when (it) {
                is State.Success -> {

                    viewModel.getAllSprints()

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

        viewModel.allSprints.observe(viewLifecycleOwner, {

            onSprintsReady(it)

        })

        viewModel.enableSprintSelection.observe(viewLifecycleOwner, {

            if (it) {

                sprintAdapter.sprintSelection = true
                sprintAdapter.notifyDataSetChanged()
                viewModel.enableSprintSelection.value = false
            }
        })

        viewModel.sprintTaskUploadProgress.observe(viewLifecycleOwner, {

            when (it) {
                is State.Success -> {
                    viewModel.getAllSprintTasks(viewModel.allSprints.value!!)
                }
                is State.Failure -> Toast.makeText(
                    requireContext(),
                    "Failed to add sprint task",
                    Toast.LENGTH_SHORT
                ).show()
                else -> Unit
            }
        })

        viewModel.allProjectUsers.observe(viewLifecycleOwner, {

            sprintAdapter.projectUsers = it
            sprintAdapter.notifyDataSetChanged()

        })

        viewModel.refreshAdapters.observe(viewLifecycleOwner, {

            sprintAdapter.notifyDataSetChanged()

        })
    }

    private fun onSprintsReady(sprints: List<Sprint>?) {

        if (sprints != null) {

            sprintAdapter.projectSprints = sprints
            sprintAdapter.notifyDataSetChanged()
        }

        mBinding.apply {

            mcvMainContainer.visibility = View.VISIBLE
            progressBar.visibility = View.GONE

        }
    }

}