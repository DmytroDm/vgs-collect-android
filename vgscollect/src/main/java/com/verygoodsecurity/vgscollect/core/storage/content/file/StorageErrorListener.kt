package com.verygoodsecurity.vgscollect.core.storage.content.file

import com.verygoodsecurity.mobile_networking.model.VGSError

internal interface StorageErrorListener {

    fun onStorageError(error: VGSError)
}