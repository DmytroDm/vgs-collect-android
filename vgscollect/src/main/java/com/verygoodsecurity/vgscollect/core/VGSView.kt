package com.verygoodsecurity.vgscollect.core

import com.verygoodsecurity.vgscollect.view.AccessibilityStatePreparer
import com.verygoodsecurity.vgscollect.view.card.FieldType

interface VGSView {

    val statePreparer: AccessibilityStatePreparer

    fun getFieldName(): String?

    fun getFieldType(): FieldType

    fun addStateListener(listener: OnVgsViewStateChangeListener)
}