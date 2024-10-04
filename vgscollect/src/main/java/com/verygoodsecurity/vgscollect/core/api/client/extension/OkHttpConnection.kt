package com.verygoodsecurity.vgscollect.core.api.client.extension

import com.verygoodsecurity.mobile_networking.model.HTTPMethod
import okhttp3.MediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.internal.EMPTY_REQUEST

internal fun String?.toRequestBodyOrNull(mediaType: MediaType?, method: HTTPMethod) =
    when (method) {
        HTTPMethod.GET -> null
        else -> this?.toRequestBody(mediaType) ?: EMPTY_REQUEST
    }
