package me.ajiew.jithub.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import me.ajiew.core.util.SPUtils
import me.ajiew.jithub.common.Constants

/**
 *
 * @author aJIEw
 * Created on: 2021/10/28 14:39
 */
class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*if (!SPUtils.instance.getBoolean(Constants.SP_EULA_PASS, false)) {
            showEulaDialog()
            return
        }*/

        startActivity(Intent(this, MainActivity::class.java))

        finish()
    }
}