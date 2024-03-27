package com.verygoodsecurity.demoapp

import android.os.Bundle
import android.view.View
import android.view.ViewParent
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.airbnb.epoxy.EpoxyRecyclerView


class ExpoDemo: AppCompatActivity(R.layout.expo_demo) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val recyclerView = findViewById<EpoxyRecyclerView>(R.id.recyclerView)

        recyclerView.withModels {
            for (i in 0..5) {
                vGSInput {
                    id(i)
                }
                for (o in 0..5) {
                    defaultInput {
                        id(i + o)
                    }
                }
            }
        }
        recyclerView.setItemSpacingDp(16)
    }
}

@EpoxyModelClass
open class DefaultInput: EpoxyModelWithHolder<DefaultInput.DefaultHolder>() {

    override fun getDefaultLayout(): Int {
        return R.layout.default_input
    }

    override fun createNewHolder(parent: ViewParent): DefaultHolder {
        return DefaultHolder()
    }

    class DefaultHolder : EpoxyHolder() {

        override fun bindView(itemView: View) {
        }
    }
}

@EpoxyModelClass
open class VGSInput: EpoxyModelWithHolder<VGSInput.VGSHolder>() {

    override fun getDefaultLayout(): Int {
        return R.layout.vgs_input
    }

    override fun createNewHolder(parent: ViewParent): VGSHolder {
        return VGSHolder()
    }

    class VGSHolder : EpoxyHolder() {

        override fun bindView(itemView: View) {
        }
    }
}