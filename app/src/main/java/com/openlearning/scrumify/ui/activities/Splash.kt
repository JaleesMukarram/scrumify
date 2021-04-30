package com.openlearning.scrumify.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.ViewPropertyAnimatorListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.firebase.firestore.auth.User
import com.openlearning.scrumify.databinding.ActivitySplashBinding
import com.openlearning.scrumify.interfaces.CustomHooks
import com.openlearning.scrumify.sealed.UserState
import com.openlearning.scrumify.utils.common.changeActivity
import com.openlearning.scrumify.viewmodels.SplashVM
import kotlinx.coroutines.*
import java.lang.Exception

class Splash : AppCompatActivity(), CustomHooks {

    private lateinit var mBinding: ActivitySplashBinding
    private lateinit var viewModel: SplashVM


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        callHooks()
    }

    override fun callHooks() {

        initViews()
        observe()

    }

    override fun initViews() {

        viewModel = ViewModelProvider(this).get(SplashVM::class.java)

        ViewCompat.animate(mBinding.ivAppIcon)
                .translationY(-200f)
                .setStartDelay(512L)
                .setDuration(1600L)
                .setInterpolator(DecelerateInterpolator(1.2f))

                .setListener(object : ViewPropertyAnimatorListener {

                    override fun onAnimationStart(view: View?) {}

                    override fun onAnimationEnd(view: View?) {
                        mBinding.tvAppName.visibility = View.VISIBLE
                        mBinding.pbrLoading.visibility = View.VISIBLE

                        viewModel.getUserStateFromServer()

                    }

                    override fun onAnimationCancel(view: View?) {}
                })
                .start()

    }

    override fun observe() {

        viewModel.userState.observe(this, {

            when (it) {

                is UserState.UserSignedIn -> changeActivity(this, MainActivity::class.java, true)
                is UserState.NoUserSignedIn -> changeActivity(this, LoginActivity::class.java, true)
                is UserState.Error -> Toast.makeText(this, "Error ${(it.value as Exception).localizedMessage}", Toast.LENGTH_SHORT).show()
            }

        })

    }
}