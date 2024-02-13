package com.verygoodsecurity.api.plaid

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.plaid.link.OpenPlaidLink
import com.plaid.link.linkTokenConfiguration
import com.plaid.link.result.LinkExit
import com.plaid.link.result.LinkSuccess

private const val TAG = "VGSCollectPlaid"

const val BUNDLE_KEY_PUBLIC_TOKEN = "com.verygoodsecurity.api.plaid.public_token"

const val KEY_EXTRA_LINK_TOKEN = "com.verygoodsecurity.api.plaid.link_token"

class VGSPlaidActivity : AppCompatActivity() {

    private val linkAccountToPlaid = registerForActivityResult(OpenPlaidLink()) {
        when (it) {
            is LinkSuccess -> {
                setResult(
                    RESULT_OK,
                    Intent().apply {
                        putExtra(BUNDLE_KEY_PUBLIC_TOKEN, it.publicToken)
                    }
                )
            }

            is LinkExit -> {
                Log.d(TAG, it.toString())
                setResult(RESULT_CANCELED)
            }
        }
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launchPlaid(intent.extras?.getString(KEY_EXTRA_LINK_TOKEN))
    }

    private fun launchPlaid(linkToken: String?) {
        linkAccountToPlaid.launch(
            linkTokenConfiguration {
                this.token = linkToken
            }
        )
    }
}