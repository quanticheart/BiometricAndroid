package com.quanticheart.biometric.secret

import android.os.Bundle
import android.widget.Toast
import com.quanticheart.biometric.R
import com.quanticheart.core.base.BaseActivity
import com.quanticheart.core.biometrics.biometricDialogForGetData
import com.quanticheart.core.biometrics.biometricDialogForSaveData
import com.quanticheart.core.biometrics.biometricRemoveData
import kotlinx.android.synthetic.main.activity_secret.*

class SecretDataActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_secret)

        encrypt.setOnClickListener {
            biometricDialogForSaveData("TEXT", secretText.text.toString())
        }

        decrypt.setOnClickListener {
            biometricDialogForGetData("TEXT") { secret ->
                secret?.let { secretText.setText(it) } ?: run {
                    Toast.makeText(this, "NENHUM DADO", Toast.LENGTH_SHORT).show()
                }
            }
        }

        remove.setOnClickListener {
            biometricRemoveData("TEXT") {
                if (it) {
                    Toast.makeText(this, "REMOVIDO", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "ERRO AO REMOVER", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}