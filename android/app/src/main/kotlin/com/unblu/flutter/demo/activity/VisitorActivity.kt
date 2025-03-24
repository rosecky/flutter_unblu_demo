package com.unblu.flutter.demo.activity

import com.specitec.ebanking.flutter.unblu.UnbluVisitorViewFactory
import com.unblu.flutter.demo.Pigeon
import com.unblu.flutter.demo.api.UnbluFlutterToVisitorHostApiImp
import com.unblu.flutter.demo.api.UnbluSingleton
import com.unblu.sdk.core.Unblu
import com.unblu.sdk.core.application.UnbluApplication
import com.unblu.sdk.core.visitor.UnbluVisitorClient
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.android.FlutterFragmentActivity
import io.flutter.embedding.engine.FlutterEngine
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber

abstract class VisitorActivity : FlutterFragmentActivity() {
    protected var globalResources = CompositeDisposable()

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        val unbluHostToFlutterApiService =
            Pigeon.UnbluVisitorHostToFlutterApi(flutterEngine.dartExecutor.binaryMessenger)
        Pigeon.UnbluFlutterToVisitorHostApi.setup(
            flutterEngine.dartExecutor.binaryMessenger,
            UnbluFlutterToVisitorHostApiImp(application as UnbluApplication, this )
        )
        UnbluSingleton.attachFlutterApi(unbluHostToFlutterApiService)

        flutterEngine
            .platformViewsController
            .registry
            .registerViewFactory("UnbluVisitorView", UnbluVisitorViewFactory())

    }

    override fun onResume() {
        super.onResume()
        Timber.i("onResume")
        if (UnbluSingleton.getClient() != null) {
            observeInstanceEvents(UnbluSingleton.getClient()!!)
        }
    }

    private fun observeUnbluGlobalEvents() {
        globalResources = CompositeDisposable()
        globalResources.addAll(
            Unblu
                .onVisitorInitialized()
                .subscribe { visitorClient ->
                    runOnUiThread {
                        UnbluSingleton.unbluHostToFlutterApiService.onVisitorInitChanged(true){ }
                    }
                },
            Unblu.onUiHideRequest()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    UnbluSingleton.unbluHostToFlutterApiService.hideUnblu { }
                },
            Unblu
                .onError()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    UnbluSingleton.unbluHostToFlutterApiService.onUnbluError(it.errorType.name, it.message) { }
                    Timber.e("errorType: ${it.errorType} m: ${it.message}")
                },
            UnbluSingleton
                .hasUiShowRequest()
                .filter { hasrequest -> hasrequest }
                .subscribe {
                    UnbluSingleton.unbluHostToFlutterApiService.showUnblu { }
                }
        )
    }

    fun observeInstanceEvents(visitorClient: UnbluVisitorClient) {
        Timber.i("observing instanceEvents")
        runOnUiThread {

        }
    }

    override fun onPause() {
        super.onPause()
        Timber.i("onPause")
    }


    override fun onStart() {
        super.onStart()
        observeUnbluGlobalEvents()
    }

    override fun onStop() {
        super.onStop()
        globalResources.dispose()
    }
}