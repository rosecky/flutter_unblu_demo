package com.unblu.flutter.demo.api

import android.app.Activity
import android.app.Application
import android.view.View
import com.unblu.flutter.demo.Pigeon
import com.unblu.livekitmodule.LiveKitModuleProvider
import com.unblu.sdk.core.Unblu
import com.unblu.sdk.core.callback.InitializeExceptionCallback
import com.unblu.sdk.core.callback.InitializeSuccessCallback
import com.unblu.sdk.core.configuration.UnbluClientConfiguration
import com.unblu.sdk.core.configuration.UnbluCookie
import com.unblu.sdk.core.configuration.UnbluDownloadHandler
import com.unblu.sdk.core.configuration.UnbluPreferencesStorage
import com.unblu.sdk.core.links.UnbluPatternMatchingExternalLinkHandler
import com.unblu.sdk.core.module.call.CallModuleProviderFactory
import com.unblu.sdk.core.notification.UnbluNotificationApi
import com.unblu.sdk.core.visitor.UnbluVisitorClient
import com.unblu.sdk.module.call.CallModule
import com.unblu.sdk.module.mobilecobrowsing.MobileCoBrowsingModule
import com.unblu.sdk.module.mobilecobrowsing.MobileCoBrowsingModuleProvider
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.util.*
import com.unblu.sdk.module.call.CallModuleProvider as VonageModule

class UnbluSingleton {
    companion object {

        internal var customCookies: MutableMap<String, String> = hashMapOf()
        internal var accessToken: String? = null
        lateinit var unbluHostToFlutterApiService: Pigeon.UnbluVisitorHostToFlutterApi

        //Store uiShowRequests as you may receive them when you don't have a client running
        //or the Unblu Ui isn't attached
        private var onUiShowRequest = BehaviorSubject.createDefault(false)
        var visitorClient: UnbluVisitorClient? = null
        private lateinit var callModule: CallModule
        private lateinit var coBrowsingModule: MobileCoBrowsingModule
        private var unbluNotificationApi: UnbluNotificationApi =
            UnbluNotificationApi.createNotificationApi()

        lateinit var unbluPreferencesStorage: UnbluPreferencesStorage

        init {

        }

        fun start(
            uApplication: Application,
            activity: Activity,
            successVoidCallback: InitializeSuccessCallback<UnbluVisitorClient>,
            deinitializeExceptionCallback: InitializeExceptionCallback
        ) {
            //create your Visitor Client Instance
            createClient(uApplication, activity, successVoidCallback, deinitializeExceptionCallback)
        }

        private fun createClient(
            uApplication: Application,
            activity: Activity,
            successCallback: InitializeSuccessCallback<UnbluVisitorClient>,
            initializeExceptionCallback: InitializeExceptionCallback
        ) {
            val unbluClientConfiguration = createUnbluClientConfiguration(uApplication)
            if (unbluClientConfiguration == null) {
                initializeExceptionCallback.onConfigureNotCalled()
                return
            }
            Unblu.createVisitorClient(
                uApplication,
                activity,
                unbluClientConfiguration,
                unbluNotificationApi,
                {
                    visitorClient = it
                    successCallback.onSuccess(it)
                },
                initializeExceptionCallback
            )
        }

        private fun createUnbluClientConfiguration(uApplication: Application): UnbluClientConfiguration? {
            val url = ""
            val apiKey = ""
            callModule = CallModuleProviderFactory.createDynamic(
                VonageModule.createForDynamic(),
                LiveKitModuleProvider.createForDynamic()
            )
            coBrowsingModule = MobileCoBrowsingModuleProvider.create()
            val builder = UnbluClientConfiguration.Builder(
                url,
                apiKey,
                unbluPreferencesStorage,
                UnbluDownloadHandler.createExternalStorageDownloadHandler(uApplication),
                UnbluPatternMatchingExternalLinkHandler()
            )
                .setCameraUploadsEnabled(true)
                .setPhotoUploadsEnabled(true)
                .registerModule(callModule)
                .registerModule(coBrowsingModule)
            if (!accessToken.isNullOrEmpty())
                builder.setAccessToken(accessToken!!)
            if (customCookies.isNotEmpty())
                builder.setCustomCookies(UnbluCookie.from(customCookies))
            return builder.build()
        }

        fun getClient(): UnbluVisitorClient? {
            return if (Objects.isNull(visitorClient) || (visitorClient!!.isDeInitialized)) null else visitorClient
        }

        fun getCallModule(): CallModule {
            return callModule
        }

        fun getCoBrowsingModule(): MobileCoBrowsingModule {
            return coBrowsingModule
        }

        fun getUnbluUi(): View? {
            return visitorClient?.mainView
        }

        fun setRequestedUiShow() {
            onUiShowRequest.onNext(true)
        }

        fun hasUiShowRequest(): Observable<Boolean> {
            return onUiShowRequest
        }

        fun getHasUiShowRequestValue(): Boolean {
            return onUiShowRequest.value!!
        }


        fun clearUiShowRequest() {
            onUiShowRequest.onNext(false)
        }

        fun getHasUiShowRequestValueAndReset(): Boolean {
            val showUiVal = onUiShowRequest.value!!
            clearUiShowRequest()
            return showUiVal
        }

        fun attachFlutterApi(
            unbluHostToFlutterApiService: Pigeon.UnbluVisitorHostToFlutterApi
        ) {
            this.unbluHostToFlutterApiService = unbluHostToFlutterApiService
        }
    }
}