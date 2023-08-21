package com.unblu.flutter.demo

import android.content.Intent
import com.unblu.flutter.demo.activity.VisitorActivity
import com.unblu.sdk.core.application.UnbluApplicationHelper
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine

class MainActivity: VisitorActivity() {
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        UnbluApplicationHelper.onNewIntent(intent.extras)
    }

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
    }
}
