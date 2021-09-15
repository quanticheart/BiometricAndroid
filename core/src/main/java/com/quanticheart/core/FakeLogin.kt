package com.quanticheart.core

import android.app.Activity

fun fakeUserToken() =
    java.util.UUID.randomUUID().toString()

fun Activity.fakeLogin(user: String, password: String, callback: (String) -> Unit) {
    val session = "$user:::${fakeUserToken()}"
    startSession(session)
    callback(session)
}

fun Activity.fakeLoginWithToken(tokenForAuth: String, callback: (Boolean) -> Unit) {
    startSession(tokenForAuth)
    callback(true)
}