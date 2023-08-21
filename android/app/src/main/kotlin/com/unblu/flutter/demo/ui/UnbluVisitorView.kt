package com.specitec.ebanking.flutter.unblu

import android.content.Context
import android.view.View
import com.unblu.flutter.demo.api.UnbluSingleton
import io.flutter.plugin.platform.PlatformView


internal class UnbluVisitorView(context: Context, id: Int, creationParams: Map<String?, Any?>?) : PlatformView {

    override fun getView(): View? {
        return UnbluSingleton.getClient()?.mainView!!
    }

    override fun dispose() {}

    init {

    }
}