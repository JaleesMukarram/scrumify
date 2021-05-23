package com.openlearning.scrumify.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.openlearning.scrumify.R
import com.openlearning.scrumify.databinding.ActivityMainBinding
import com.openlearning.scrumify.repo.UserRepo
import com.openlearning.scrumify.utils.common.changeActivity

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.btnSignOut.setOnClickListener {

            UserRepo.signOut()
            changeActivity(this, LoginActivity::class.java, true)

        }

    }
}