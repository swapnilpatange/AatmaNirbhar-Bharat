package com.aatmanirbhar.bharat

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.os.Handler


class WelcomeActivity : AppCompatActivity() {
    /** Duration of wait  */


    private val SPLASH_DISPLAY_LENGTH:Long = 1000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        Handler().postDelayed(Runnable {
            /* Create an Intent that will start the Menu-Activity.*/
            val mainIntent = Intent(this@WelcomeActivity, ReplaceAppActivity::class.java)
            this@WelcomeActivity.startActivity(mainIntent)
            this@WelcomeActivity.finish()
        }, SPLASH_DISPLAY_LENGTH)


    }
}
