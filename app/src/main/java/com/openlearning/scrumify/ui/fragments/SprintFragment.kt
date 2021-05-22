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
import com.openlearning.scrumify.interfaces.CustomHooks
import com.openlearning.scrumify.models.Sprint
import com.openlearning.scrumify.sealed.State
import com.openlearning.scrumify.viewmodels.SprintsVM

class SprintFragment : Fragment(), CustomHooks {

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

        sprintAdapter = SprintAdapter(arrayListOf()) { sprintEdit ->

        }

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

//                    viewModel.getAllTasks()

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
        viewModel.getAllSprints()

    }

    private fun onSprintsReady(sprints: List<Sprint>?) {

        if (sprints != null) {

            sprintAdapter.projectSprints = sprints
            sprintAdapter.notifyDataSetChanged()
        }
    }

}