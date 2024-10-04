package com.verygoodsecurity.vgscollect.core.api.client.extension

import com.verygoodsecurity.mobile_networking.model.NetworkResponse
import com.verygoodsecurity.mobile_networking.model.VGSError
import com.verygoodsecurity.vgscollect.core.model.network.VGSResponse

fun NetworkResponse.toVGSResponse(): VGSResponse {
    return when {
        this.isSuccessful -> VGSResponse.SuccessResponse(
            successCode = this.code,
            rawResponse = this.body
        )

        this.error != null -> VGSResponse.ErrorResponse(
            localizeMessage = error!!.code.toString(),
            errorCode = error!!.code,
            rawResponse = this.body
        )

        else -> VGSResponse.ErrorResponse(
            localizeMessage = this.message ?: "",
            errorCode = this.code,
            rawResponse = this.body
        )
    }
}

fun VGSError.toVGSResponse() = VGSResponse.ErrorResponse(
    localizeMessage = code.toString(),
    errorCode = code,
)