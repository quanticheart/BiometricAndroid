package com.quanticheart.biometric.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.quanticheart.biometric.R
import com.quanticheart.biometric.extentions.goLogin
import com.quanticheart.biometric.extentions.goSecret
import com.quanticheart.core.biometrics.*
import com.quanticheart.core.finishSession
import com.quanticheart.core.session
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        sair.setOnClickListener {
            finishSession()
            removeBiometricData("TEXT")
            goLogin()
        }

        sessionText.text = session()

        segredo.setOnClickListener {
            goSecret()
        }
    }

    override fun onResume() {
        super.onResume()
        deviceHaveBiometricsHardware {
            biometrics?.let {
                desvincular.apply {
                    visibility = View.VISIBLE
                    setOnClickListener {
                        biometricRemoveSession {
                            Toast.makeText(
                                this@HomeActivity,
                                "Sessão removida da biometria",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }
                }
            }

            vincular.apply {
                visibility = View.VISIBLE
                setOnClickListener {
                    session()?.let {
                        biometricSession(it, false)
                    }
                }
            }
        }
    }
}