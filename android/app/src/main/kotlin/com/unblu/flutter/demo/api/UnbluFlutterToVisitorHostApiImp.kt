package com.unblu.flutter.demo.api

import android.app.Activity
import com.unblu.flutter.demo.MainActivity
import com.unblu.flutter.demo.Pigeon
import com.unblu.sdk.core.application.UnbluApplication
import com.unblu.sdk.core.callback.InitializeExceptionCallback
import com.unblu.sdk.core.callback.OpenConversationExceptionCallback
import com.unblu.sdk.core.callback.OpenConversationOverviewExceptionCallback
import com.unblu.sdk.core.errortype.OpenConversationErrorType
import com.unblu.sdk.core.errortype.OpenConversationOverviewErrorType
import com.unblu.sdk.core.errortype.UnbluClientErrorType
import java.lang.IllegalStateException

class UnbluFlutterToVisitorHostApiImp(
    val application: UnbluApplication,
    val activity: Activity
) : Pigeon.UnbluFlutterToVisitorHostApi {
    override fun createVisitor(
        result: Pigeon.Result<Boolean>
    ) {
        UnbluSingleton.start(application, activity, {
            result.success(true)
        }, object : InitializeExceptionCallback {
            override fun onConfigureNotCalled() {
                result.success(false)
                UnbluSingleton.unbluHostToFlutterApiService.displayMessage("Unblu API not configured") {}
            }

            override fun onInErrorState() {
                result.success(false)
                UnbluSingleton.unbluHostToFlutterApiService.displayMessage("Unblu API in error state") {}
            }

            override fun onInitFailed(errorType: UnbluClientErrorType, details: String?) {
                result.success(false)
                UnbluSingleton.unbluHostToFlutterApiService.displayMessage("Unblu API init failed") {}
            }
        })

    }

    override fun deinitializeVisitor(result: Pigeon.Result<Boolean>) {
        UnbluSingleton.getClient()?.apply {
            deinitClient({
                result.success(true)
            }, { failure ->
                result.success(false)
                failure?.let { message->  UnbluSingleton.unbluHostToFlutterApiService.displayMessage(message){} }
            })
        } ?: result.success(true)
    }

    override fun isInitialized(result: Pigeon.Result<Boolean>) {
        UnbluSingleton.getClient()?.let { unbluVisitorClient ->
            result.success(!unbluVisitorClient.isDeInitialized)
        } ?: kotlin.run {
            result.success(false)
        }
    }

    override fun getPersonInfo(): Pigeon.PersonInfo? {
        return UnbluSingleton.getClient()?.personInfo?.let { personInfo ->
            Pigeon.PersonInfo(
                personInfo.id,
                personInfo.displayName
            )
        }
    }

    override fun getUnreadMessagesCount(): Long {
        return UnbluSingleton.getClient()?.unreadMessagesCount?.toLong() ?: 0
    }

    override fun isDeInitialized(): Boolean {
        return UnbluSingleton.getClient()?.isDeInitialized ?: true
    }

    override fun openConversation(conversationId: String, result: Pigeon.Result<Boolean>) {
        UnbluSingleton.getClient()?.let { unbluVisitorClient ->
            unbluVisitorClient.openConversation(conversationId, {
                result.success(true)
            }, object : OpenConversationExceptionCallback {
                override fun onNotInitialized() {
                    result.success(false)
                    UnbluSingleton.unbluHostToFlutterApiService.displayMessage("Unblu not initialized") {}
                }

                override fun onFailedToOpen(type: OpenConversationErrorType, details: String?) {
                    result.success(false)
                    UnbluSingleton.unbluHostToFlutterApiService.displayMessage("Unblu not initialized") {}
                }
            })
        } ?: run {
            result.success(false)
            UnbluSingleton.unbluHostToFlutterApiService.displayMessage("Unblu not initialized") {}
        }
    }

    override fun openConversationOverview(result: Pigeon.Result<Boolean>) {
        UnbluSingleton.getClient()?.let { unbluVisitorClient ->
            unbluVisitorClient.openConversationOverview({
                result.success(true)
            }, object : OpenConversationOverviewExceptionCallback {
                override fun onNotInitialized() {
                    result.success(false)
                    UnbluSingleton.unbluHostToFlutterApiService.displayMessage("Unblu not initialized") {}
                }

                override fun onFailedToOpenOverview(
                    type: OpenConversationOverviewErrorType,
                    details: String?
                ) {
                    result.success(false)
                    UnbluSingleton.unbluHostToFlutterApiService.displayMessage("${type.name}: $details") {}
                }
            })
        } ?: run {
            result.success(false)
            UnbluSingleton.unbluHostToFlutterApiService.displayMessage("Unblu not initialized") {}
        }
    }

    override fun setAccessToken(token: String) {
        UnbluSingleton.accessToken = token
    }

    override fun setCustomCookies(customCookies: MutableMap<String, String>) {
        UnbluSingleton.customCookies = customCookies
    }

}
