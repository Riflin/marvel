package com.entelgy.marvel.app.splash

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.entelgy.marvel.app.characterslist.CharactersListActivity
import com.entelgy.marvel.app.utils.base.BaseActivity
import com.entelgy.marvel.databinding.ActivitySplashBinding

@SuppressLint("CustomSplashScreen")
class SplashActivity: BaseActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun init() {
        //NOTHING
    }

    override fun initViews() {
        Handler().postDelayed({
            for (i in 0..100) {
                if (i.rem(10) == 0) {
                    binding.ivProgress.setImageLevel(100 * i)
                    Thread.sleep(500)
                }
            }
            startActivity(CharactersListActivity.createNewIntent(this))
            finish()
        }, 2000)

    }

    override fun attachListenersToTheViews() {

    }

    override fun showLoading() {

    }

    override fun hideLoading() {

    }
}