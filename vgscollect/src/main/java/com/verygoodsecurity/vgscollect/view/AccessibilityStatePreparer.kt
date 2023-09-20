package com.verygoodsecurity.vgscollect.view

import android.view.View
import com.verygoodsecurity.vgscollect.core.api.analityc.AnalyticTracker
import com.verygoodsecurity.vgscollect.core.storage.DependencyListener

/** @suppress */
interface AccessibilityStatePreparer {

    fun getId(): Int

    fun getView(): View // TODO: Remove

    fun unsubscribe()

    fun getDependencyListener(): DependencyListener

    fun setAnalyticTracker(tr: AnalyticTracker)
}