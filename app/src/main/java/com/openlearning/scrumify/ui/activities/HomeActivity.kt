package com.openlearning.scrumify.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.openlearning.scrumify.databinding.ActivityHomeBinding
import com.openlearning.scrumify.interfaces.CustomHooks
import com.openlearning.scrumify.utils.SPLASH_SKIP_ANIMATION
import com.openlearning.scrumify.utils.common.changeActivity
import com.openlearning.scrumify.viewmodels.AddNewProjectVM
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
        viewModel = ViewModelProvider(this).get(HomeVM::class.java)

    }

    override fun initListeners() {

        mBinding.cvCreateProject.setOnClickListener {

            changeActivity(this, AddNewProject::class.java, false)

        }

        mBinding.cvViewProject.setOnClickListener {

            changeActivity(this, ShowProjectsActivity::class.java, false)

        }

        mBinding.ivAccount.setOnClickListener {

            val alertConf = AlertDialog.Builder(this)

            alertConf.setTitle("Confirmation")
            alertConf.setMessage("Are you sure you want to log out?")
            alertConf.setPositiveButton(
                "Logout"
            ) { _, _ ->

                viewModel.signOut()
                val intent = Intent(this, SplashActivity::class.java)
                intent.putExtra(SPLASH_SKIP_ANIMATION, true)
                changeActivity(this, intent, true)

            }
            alertConf.setNegativeButton(
                "Cancel"
            ) { _, _ -> }

            val dialog = alertConf.create()
            dialog.show()


        }
    }
}