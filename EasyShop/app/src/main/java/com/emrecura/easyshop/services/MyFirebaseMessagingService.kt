package com.emrecura.easyshop.services

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService: FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        Log.d("newToken", token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        Log.d("Message from", "${message.from}")
        Log.d("Message messageId", "${message.messageId}")
        Log.d("Message data", "${message.data}")
        message.notification?.let {
            Log.d("Message body", "${it.body}")
        }
    }

}