package com.verygoodsecurity.vgscollect.core.api.client

import com.verygoodsecurity.mobile_networking.model.NetworkRequest
import com.verygoodsecurity.mobile_networking.model.NetworkResponse
import com.verygoodsecurity.vgscollect.BuildConfig
import com.verygoodsecurity.vgscollect.core.api.VgsApiTemporaryStorage
import com.verygoodsecurity.vgscollect.core.api.analityc.CollectActionTracker

internal interface ApiClient {

    fun enqueue(request: NetworkRequest, callback: ((NetworkResponse) -> Unit)? = null)

    fun execute(request: NetworkRequest): NetworkResponse

    fun cancelAll()

    fun getTemporaryStorage(): VgsApiTemporaryStorage

    companion object {

        private const val AGENT = "vgs-client"
        private const val TEMPORARY_AGENT_TEMPLATE =
            "source=androidSDK&medium=vgs-collect&content=%s&vgsCollectSessionId=%s&tr=%s"


        fun generateAgentHeader(isAnalyticsEnabled: Boolean): Pair<String, String> =
            AGENT to String.format(
                TEMPORARY_AGENT_TEMPLATE,
                BuildConfig.VERSION_NAME,
                CollectActionTracker.Sid.id,
                if (isAnalyticsEnabled) "default" else "none"
            )
    }
}