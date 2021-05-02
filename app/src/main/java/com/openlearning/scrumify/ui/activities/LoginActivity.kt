package com.openlearning.scrumify.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.openlearning.scrumify.R
import com.openlearning.scrumify.databinding.ActivityLoginBinding
import com.openlearning.scrumify.databinding.ActivityRegistrationBinding
import com.openlearning.scrumify.dialogues.LoadingDialogue
import com.openlearning.scrumify.interfaces.CustomHooks
import com.openlearning.scrumify.sealed.State
import com.openlearning.scrumify.utils.SPLASH_SKIP_ANIMATION
import com.openlearning.scrumify.utils.common.changeActivity
import com.openlearning.scrumify.utils.common.showLoading
import com.openlearning.scrumify.utils.extensions.ifAllTrue
import com.openlearning.scrumify.viewmodels.LoginVM

class LoginActivity : AppCompatActivity(), CustomHooks {

    private lateinit var mBinding: ActivityLoginBinding
    private lateinit var viewModel: LoginVM

    private var loader: LoadingDialogue? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        callHooks()
    }

    override fun callHooks() {

        initViews()
        initListeners()
        observe()

    }

    override fun initViews() {

        viewModel = ViewModelProvider(this).get(LoginVM::class.java)
        mBinding.viewModel = viewModel

    }

    override fun initListeners() {

        mBinding.register.setOnClickListener {

            changeActivity(this, RegistrationActivity::class.java, true)

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
                    onLogInSuccess()
                }
            }
        })

        viewModel.groupValid.observe(this, {

            mBinding.ready = it.ifAllTrue()

        })
    }

    private fun onLogInSuccess() {

        val intent = Intent(this, Splash::class.java)
        intent.putExtra(SPLASH_SKIP_ANIMATION, true)
        changeActivity(this, intent, true)
    }
}