package com.openlearning.scrumify.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.openlearning.scrumify.adapters.AddedUserAdapter
import com.openlearning.scrumify.adapters.NewUserSearchAdapter
import com.openlearning.scrumify.databinding.ActivityProjectDetailsBinding
import com.openlearning.scrumify.dialogues.ProjectUserNewDialogue
import com.openlearning.scrumify.interfaces.CustomHooks
import com.openlearning.scrumify.models.*
import com.openlearning.scrumify.sealed.State
import com.openlearning.scrumify.utils.PROJECT_INTENT
import com.openlearning.scrumify.utils.common.TextWatcherImpl
import com.openlearning.scrumify.utils.common.changeActivity
import com.openlearning.scrumify.utils.common.getMyRole
import com.openlearning.scrumify.utils.common.hideKeyboard
import com.openlearning.scrumify.utils.extensions.value
import com.openlearning.scrumify.viewmodels.ProjectDetailsVM

class ProjectDetailsActivity : AppCompatActivity(), CustomHooks {

    private val TAG = "ProjectDetailsTAG"
    private var project: Project? = null


    private lateinit var mBinding: ActivityProjectDetailsBinding
    private lateinit var viewModel: ProjectDetailsVM

    private lateinit var newUserAdapter: NewUserSearchAdapter
    private lateinit var addedUserAdapter: AddedUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityProjectDetailsBinding.inflate(layoutInflater)
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

        viewModel = ViewModelProvider(this).get(ProjectDetailsVM::class.java)
        viewModel.projectState.value = project!!
        mBinding.viewModel = viewModel

        newUserAdapter = NewUserSearchAdapter(arrayListOf()) {

            onSearchEnd()
            mBinding.etUserSearch.value = ""
            mBinding.etUserSearch.hideKeyboard()

            ProjectUserNewDialogue(
                activity = this,
                user = it,
                added = false,
                onAdded = { index -> viewModel.onAddUserToProject(it, index) },
                onRemove = removeUser
            ).show()

        }
        mBinding.rvAllNewUsers.adapter = newUserAdapter

        addedUserAdapter = AddedUserAdapter(arrayListOf()) {

            ProjectUserNewDialogue(
                activity = this,
                user = it,
                added = true,
                onAdded = { index -> viewModel.onUpdateUserRole(it, index) },
                onRemove = removeUser
            ).show()

        }

        mBinding.rvAllAddedUsers.adapter = addedUserAdapter
    }

    override fun initListeners() {

        mBinding.etUserSearch.addTextChangedListener(object : TextWatcherImpl() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s == null || s.isEmpty()) {
                    onSearchEnd()
                } else {
                    onSearching(s.toString())
                }
            }
        })

        mBinding.cvTasks.setOnClickListener {
            val intent = Intent(this, TasksActivity::class.java)
            intent.putExtra(PROJECT_INTENT, viewModel.projectState.value!!)
            changeActivity(this, intent, false)
        }
    }

    override fun observe() {

        viewModel.projectState.observe(this, {
            getAddedUsers(viewModel.projectState.value!!.projectUsers)

        })

        viewModel.allUsers.observe(this, {
            getAddedUsers(viewModel.projectState.value!!.projectUsers)
        })

        viewModel.newUserAddedState.observe(this, {

            when (it) {

                is State.Success -> viewModel.refreshProject()
                is State.Failure -> Toast.makeText(
                    this,
                    "Scrum Master Already Added in Project",
                    Toast.LENGTH_SHORT
                ).show()
                else -> Unit
            }

        })

        viewModel.getAllUserFromRepo()

        changeUserView(viewModel.getMyId())
    }

    override fun onBackPressed() {
        finish()
    }

    private fun changeUserView(myId: String) {

        when (getMyRole(project!!, myId)) {
            ROLES.ADMINISTRATOR -> {

                makeAdminView()
            }
            ROLES.SCRUM_MASTER -> {

                makeScrumMasterView()
            }
            ROLES.TEAM_MEMBER -> {

                makeTeamMemberView()
            }
        }

    }


    private fun makeAdminView() {

        changeTopSearchVisibility(true)
        Log.d(TAG, "makeAdminView: I am admin")
    }

    private fun makeScrumMasterView() {

        changeTopSearchVisibility(true)
        Log.d(TAG, "makeAdminView: I am scrum master")

    }

    private fun makeTeamMemberView() {
        changeTopSearchVisibility(false)
        Log.d(TAG, "makeAdminView: I am team member")

    }


    private fun onSearching(queryStirng: String) {

        val users = viewModel.getFilteredUsers(queryStirng)

        if (users != null) {

            Log.d(TAG, "onSearching: new adapter user $users")
            mBinding.rvAllNewUsers.visibility = View.VISIBLE
            newUserAdapter.users = users
            newUserAdapter.notifyDataSetChanged()

        } else {
            mBinding.rvAllNewUsers.visibility = View.GONE
        }

    }

    private fun onSearchEnd() {
        newUserAdapter.users = arrayListOf()
        newUserAdapter.notifyDataSetChanged()
        mBinding.rvAllNewUsers.visibility = View.GONE
    }

    private fun getAddedUsers(projectUsers: List<ProjectUser>) {

        val adapterUsers: MutableList<ProjectUserData> = arrayListOf()
        for (pUser in projectUsers) {

            val user = viewModel.getUserOfThisId(pUser.userId)
            if (user != null) {
                adapterUsers.add(ProjectUserData(user, pUser.role))
            }
        }

        addedUserAdapter.projectUserDatas = adapterUsers
        addedUserAdapter.notifyDataSetChanged()
    }

    private fun changeTopSearchVisibility(visible: Boolean) {

        val visibility = when (visible) {
            true -> View.VISIBLE
            false -> View.GONE
        }
        mBinding.mcvTopSearchBar.visibility = visibility
        mBinding.rvAllNewUsers.visibility = visibility
    }

    private val removeUser: (User) -> Unit = {

        viewModel.deleteRoleOfThisUser(it)
    }
}