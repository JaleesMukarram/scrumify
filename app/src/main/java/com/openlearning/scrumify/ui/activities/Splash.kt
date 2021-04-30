package com.openlearning.scrumify.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.openlearning.scrumify.databinding.ActivitySplashBinding
import com.openlearning.scrumify.interfaces.CustomHooks
import com.openlearning.scrumify.utils.common.changeActivity
import kotlinx.coroutines.*

class Splash : AppCompatActivity(), CustomHooks {

    lateinit var mBinding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        callHooks()
    }

    override fun callHooks() {

        moveToRegistration()

    }

    override fun initViews() {

    }

    private fun moveToRegistration() {

        lifecycleScope.launch {

            delay(500)
            withContext(Dispatchers.Main) {

                changeActivity(this@Splash, RegistrationActivity::class.java, true)
            }
        }
    }
}