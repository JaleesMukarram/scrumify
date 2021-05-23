package com.openlearning.scrumify.interfaces

interface CustomHooks {

    fun callHooks()

    fun handleIntent() {}

    fun initViews()

    fun initListeners() {}

    fun observe() {}

    fun refresh() {}

    fun loaded(){}


}