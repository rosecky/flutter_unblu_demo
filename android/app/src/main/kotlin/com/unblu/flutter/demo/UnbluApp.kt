package com.unblu.flutter.demo

import android.widget.Toast
import com.unblu.flutter.demo.api.UnbluSingleton
import com.unblu.sdk.core.Unblu
import com.unblu.sdk.core.application.UnbluApplication
import com.unblu.sdk.core.configuration.UnbluPreferencesStorage
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class UnbluApp : UnbluApplication() {
    override fun onCreate() {
        super.onCreate()
        UnbluSingleton.unbluPreferencesStorage = UnbluPreferencesStorage.createSharedPreferencesStorage(this)
        Unblu.onUiVisibilityRequest()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    UnbluSingleton.setRequestedUiShow()
                },
                {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT)
                }
            )
    }
}