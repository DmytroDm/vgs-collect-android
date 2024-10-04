package com.verygoodsecurity.vgscollect.core.storage.content.field

import com.verygoodsecurity.mobile_networking.model.VGSError
import com.verygoodsecurity.vgscollect.VGSCollectLogger
import com.verygoodsecurity.vgscollect.core.model.state.VGSFieldState
import com.verygoodsecurity.vgscollect.core.storage.StorageContractor
import com.verygoodsecurity.vgscollect.view.InputFieldView

/** @suppress */
internal class FieldStateContractor : StorageContractor<VGSFieldState> {

    override fun checkState(state: VGSFieldState): Boolean {
        return if (state.fieldName?.trim().isNullOrEmpty()) {
            VGSCollectLogger.warn(InputFieldView.TAG, VGSError.FIELD_NAME_NOT_SET.code.toString())
            false
        } else {
            true
        }
    }

}