package com.quanticheart.biometric.biometric

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.quanticheart.biometric.databinding.ActivityEnableBiometricLoginBinding
import com.quanticheart.core.biometrics.biometricSession
import com.quanticheart.core.extentions.setFinishActionListener
import com.quanticheart.core.fakeLogin

class EnableBiometricLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEnableBiometricLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEnableBiometricLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.cancel.setOnClickListener { finish() }

        binding.password.setFinishActionListener {
            loginWithUserAndPassword()
        }

        binding.authorize.setOnClickListener {
            loginWithUserAndPassword()
        }
    }

    private fun loginWithUserAndPassword() {
        fakeLogin(
            binding.username.text.toString(),
            binding.password.text.toString()
        ) { tokenForAuth ->
            biometricSession(tokenForAuth, true)
        }
    }
}
