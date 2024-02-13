package com.verygoodsecurity.demoapp.plaid

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.verygoodsecurity.api.plaid.BUNDLE_KEY_PUBLIC_TOKEN
import com.verygoodsecurity.api.plaid.KEY_EXTRA_LINK_TOKEN
import com.verygoodsecurity.api.plaid.VGSPlaidActivity
import com.verygoodsecurity.demoapp.R
import com.verygoodsecurity.demoapp.StartActivity
import com.verygoodsecurity.demoapp.plaid.api.RemoteService
import com.verygoodsecurity.vgscollect.core.VGSCollect
import com.verygoodsecurity.vgscollect.widget.CardVerificationCodeEditText
import com.verygoodsecurity.vgscollect.widget.ExpirationDateEditText
import com.verygoodsecurity.vgscollect.widget.PersonNameEditText
import com.verygoodsecurity.vgscollect.widget.VGSCardNumberEditText
import com.verygoodsecurity.vgscollect.widget.VGSTextInputLayout
import io.github.kbiakov.codeview.CodeView
import io.github.kbiakov.codeview.adapters.Options
import io.github.kbiakov.codeview.highlight.ColorThemeData
import io.github.kbiakov.codeview.highlight.SyntaxColors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

private const val TAG = "VGSCollectPlaid"

class PlaidIntegrationActivity : AppCompatActivity() {

    private val collect: VGSCollect by lazy {
        VGSCollect(
            this,
            getStringExtra(StartActivity.KEY_BUNDLE_VAULT_ID, ""),
            getStringExtra(StartActivity.KEY_BUNDLE_ENVIRONMENT, "")
        )
    }

    private val vgsPlaidActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ::handlePlaidResult
    )

    private val cardNumberEt by lazy { findViewById<VGSCardNumberEditText>(R.id.vgsTiedNumber) }
    private val nameEt by lazy { findViewById<PersonNameEditText>(R.id.vgsTiedName) }
    private val expiryEt by lazy { findViewById<ExpirationDateEditText>(R.id.vgsTiedExpiry) }
    private val cvcEt by lazy { findViewById<CardVerificationCodeEditText>(R.id.vgsTiedCvc) }
    private val codeExampleView by lazy { findViewById<CodeView>(R.id.cvResponse) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plaid_integration)
        initCodeExampleView()
        collect.bindView(cardNumberEt, nameEt, expiryEt, cvcEt)

        findViewById<Button>(R.id.btnNext).setOnClickListener {
            cardNumberEt.getState()?.bin?.let { bin -> launchPlaid(bin) }
        }
    }

    private fun initCodeExampleView() {
        val syntaxColor = ContextCompat.getColor(this, R.color.dimGrey)
        val bgColor = ContextCompat.getColor(this, R.color.pattensBlue)
        val lineNumberColor = ContextCompat.getColor(this, R.color.nobel)
        codeExampleView.setOptions(
            Options(
                context = this.applicationContext, theme = ColorThemeData(
                    SyntaxColors(
                        string = syntaxColor,
                        punctuation = syntaxColor,
                    ),
                    numColor = lineNumberColor,
                    bgContent = bgColor,
                    bgNum = bgColor,
                    noteColor = syntaxColor,
                )
            )
        )
        codeExampleView.alpha = 1f
        codeExampleView.findViewById<RecyclerView>(R.id.rv_code_content).isNestedScrollingEnabled =
            false
        codeExampleView.setCode("Plaid Result")
    }

    // Request link token from backend and launch VGS Plaid activity
    private fun launchPlaid(bin: String) {
        lifecycleScope.launch {
            val token = RemoteService.getLinkToken(bin = bin, havePlaidAcc = true)
            if (token.isNullOrEmpty()) {
                Log.d(TAG, "Link token is null or empty")
                return@launch
            }

            withContext(Dispatchers.Main) {
                vgsPlaidActivityResultLauncher.launch(
                    Intent(
                        this@PlaidIntegrationActivity,
                        VGSPlaidActivity::class.java
                    ).apply {
                        putExtra(KEY_EXTRA_LINK_TOKEN, token)
                    }
                )
            }
        }
    }

    // Handle VGS Plaid activity result and load balance data
    private fun handlePlaidResult(result: ActivityResult) {
        Log.d(TAG, result.toString())
        if (result.resultCode == Activity.RESULT_OK) {
            val publicToken = result.data?.getStringExtra(BUNDLE_KEY_PUBLIC_TOKEN)
            if (publicToken.isNullOrEmpty()) {
                Log.d(TAG, "Public token is null or empty")
                return
            }

            lifecycleScope.launch {
                val balanceData = RemoteService.getBalanceData(publicToken)
                val formattedBalanceData = JSONObject(balanceData).toString(4)
                Log.d(TAG, "balanceData = $formattedBalanceData")
                codeExampleView.setCode(formattedBalanceData)
            }
        } else {
            Toast.makeText(this, "Error/Canceled", Toast.LENGTH_SHORT).show()
        }
    }

    private fun AppCompatActivity.getStringExtra(key: String, defaultValue: String = ""): String {
        return intent.extras?.getString(key) ?: defaultValue
    }
}