package com.openlearning.scrumify.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.openlearning.scrumify.databinding.ActivityRegistrationBinding
import com.openlearning.scrumify.dialogues.LoadingDialogue
import com.openlearning.scrumify.interfaces.CustomHooks
import com.openlearning.scrumify.sealed.State
import com.openlearning.scrumify.utils.SPLASH_SKIP_ANIMATION
import com.openlearning.scrumify.utils.common.changeActivity
import com.openlearning.scrumify.utils.common.showLoading
import com.openlearning.scrumify.utils.extensions.ifAllTrue
import com.openlearning.scrumify.viewmodels.AppVM
import com.openlearning.scrumify.viewmodels.RegistrationVM

class RegistrationActivity : AppCompatActivity(), CustomHooks {

    private val TAG = "RegistrationActivityTAG"

    private lateinit var mBinding: ActivityRegistrationBinding
    private lateinit var viewModel: RegistrationVM
    private lateinit var appViewModel: AppVM

    private var loader: LoadingDialogue? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        callHooks()
    }

    override fun callHooks() {

        initViews()
        initListeners()
        observe()
    }

    override fun initViews() {

        viewModel = ViewModelProvider(this).get(RegistrationVM::class.java)
        appViewModel = ViewModelProvider(this).get(AppVM::class.java)
        mBinding.viewModel = viewModel

    }

    override fun initListeners() {

        mBinding.login.setOnClickListener {
            changeActivity(this, LoginActivity::class.java, true)
        }
    }

    override fun observe() {

        viewModel.userAuthState.observe(this, {


            when (it) {


                is State.Idle -> {
                    loader?.cancel()
                    mBinding.tvError.text = "";
                }
                is State.Failure -> {
                    loader!!.cancel()
                    mBinding.tvError.text = it.value as? String
                }
                is State.Loading -> {
                    loader = showLoading(
                        this,
                        "Please wait !!",
                        it.value as String
                    )
                }
                is State.Success -> {
                    loader?.cancel()
                    onRegistrationSuccess()
                }
            }
        })


        viewModel.groupValid.observe(this, {

            mBinding.ready = it.ifAllTrue()

        })


    }

    private fun onRegistrationSuccess() {

        val intent = Intent(this, SplashActivity::class.java)
        intent.putExtra(SPLASH_SKIP_ANIMATION, true)
        changeActivity(this, intent, true)
    }
}