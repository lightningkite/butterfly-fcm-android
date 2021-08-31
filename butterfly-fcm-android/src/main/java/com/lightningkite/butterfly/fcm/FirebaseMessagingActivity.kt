package com.lightningkite.butterfly.fcm

import android.os.Bundle
import com.lightningkite.rxkotlinproperty.viewgenerators.ViewGeneratorActivity

abstract class FirebaseMessagingActivity: ViewGeneratorActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyFirebaseMessagingService.main = this.main
    }
}