package com.lightningkite.butterfly.fcm

import android.os.Bundle
import com.lightningkite.butterfly.android.KhrysalisActivity
import com.lightningkite.butterfly.views.ViewGenerator

abstract class FirebaseMessagingActivity: KhrysalisActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyFirebaseMessagingService.main = this.main
    }
}