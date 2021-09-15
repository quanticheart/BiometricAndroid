package com.quanticheart.biometric

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.quanticheart.biometric.databinding.ActivityLoginBinding
import com.quanticheart.biometric.extentions.goBiometricInsert
import com.quanticheart.biometric.extentions.goHome
import com.quanticheart.core.biometrics.biometricGetSession
import com.quanticheart.core.biometrics.biometrics
import com.quanticheart.core.biometrics.deviceHaveBiometricsHardware
import com.quanticheart.core.fakeLogin
import com.quanticheart.core.fakeLoginWithToken
import com.quanticheart.core.verifySession

/**
 * After entering "valid" username and password, login button becomes enabled
 */
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        deviceHaveBiometricsHardware {
            binding.useBiometrics.apply {
                visibility = View.VISIBLE
                setOnClickListener {
                    biometrics?.let {
                        showBiometricPrompt()
                    } ?: run {
                        goBiometricInsert()
                    }
                }
            }
        }

        binding.password.setFinishActionListener {
            loginWithUserAndPassword()
        }

        binding.login.setOnClickListener {
            loginWithUserAndPassword()
        }

    }

    private fun loginWithUserAndPassword() {
        fakeLogin(
            binding.username.text.toString(),
            binding.password.text.toString()
        ) { tokenForAuth ->
            goHome()
        }
    }

    private fun showBiometricPrompt() {
        biometricGetSession {
            it?.let { it1 ->
                fakeLoginWithToken(it1) { loginStatus ->
                    if (loginStatus) {
                        goHome()
                    } else {
                        Toast.makeText(this, "Error on login", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    /**
     * The logic is kept inside onResume instead of onCreate so that authorizing biometrics takes
     * immediate effect.
     */
    override fun onResume() {
        super.onResume()
        if (verifySession()) {
            goHome()
        }
//        else {
//            biometrics?.let {
//                showBiometricPrompt()
//            }
//        }
    }
}