package com.quanticheart.biometric.extentions

import android.app.Activity
import android.content.Intent
import com.quanticheart.biometric.LoginActivity
import com.quanticheart.biometric.biometric.EnableBiometricLoginActivity
import com.quanticheart.biometric.home.HomeActivity
import com.quanticheart.biometric.secret.SecretDataActivity

fun Activity.goBiometricInsert() =
    startActivity(Intent(this, EnableBiometricLoginActivity::class.java))

fun Activity.goHome() =
    startActivity(Intent(this, HomeActivity::class.java))

fun Activity.goLogin() =
    startActivity(Intent(this, LoginActivity::class.java))

fun Activity.goSecret() =
    startActivity(Intent(this, SecretDataActivity::class.java))